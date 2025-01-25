package com.example.shoppingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.adapter.JewelryAdapter;
import com.example.shoppingapp.model.JewelryItem;
import com.example.shoppingapp.adapter.UserCartAdapter;
import com.example.shoppingapp.model.UserCartItem;
import com.example.shoppingapp.repository.InitDB;
import com.example.shoppingapp.repository.JewelryRepository;
import com.example.shoppingapp.repository.UserCartRepository;
import com.example.shoppingapp.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private JewelryAdapter jewelryAdapter;
    private UserCartAdapter userCartAdapter;
    private final UserRepository userRepository = new UserRepository();
    private final JewelryRepository jewelryRepository = new JewelryRepository();
    private final UserCartRepository userCartRepository = new UserCartRepository();
    private final InitDB initDB = new InitDB();

    private RecyclerView recyclerView;
    private String username; // will be initialized during login/registration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets for proper padding on system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize FirebaseAuth and JewelryHelper
        mAuth = FirebaseAuth.getInstance();

        initDB.loadTablesInDataBase();

        // Handle menu and logo actions
        menuAndLogoHandler();

        // Prepare UI components for display
        preparationForDisplay(false, false);

        // Set logout button functionality
        Button logoutButton= findViewById(R.id.logoutBtn);
        // Set click listener for the register button
        logoutButton.setOnClickListener(v -> {
            Navigation.findNavController(MainActivity.this, R.id.fragmentContainerViewDefault).navigate(R.id.action_shoppingFragment_to_entranceFragment);
            preparationForDisplay(false, false);
        });

        // Set shopping cart button functionality
        Button cartBtn = findViewById(R.id.shoppingCartButton);
        cartBtn.setOnClickListener(v -> {
            /* Navigate to user cart fragment with username
               The logged in username will be added to the fragment
            */
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            getUserData();
            Navigation.findNavController(MainActivity.this, R.id.fragmentContainerViewDefault).navigate(R.id.action_shoppingFragment_to_userCartFragment, bundle);
            preparationForDisplay(false, true);
            // Fetch user cart data from Firebase
            userCartRepository.getCartByUsername(username).addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    ArrayList<UserCartItem> dataSet = new ArrayList<>();
                    DataSnapshot cartForUserSnapshot = task.getResult();

                    // Add items to the dataset
                    Utils.addUserCartToDataset(cartForUserSnapshot, dataSet);

                    TextView totalCartPrice = findViewById(R.id.regUserCart_totalCartPrice);
                    // Update the RecyclerView with the combined dataSet
                    userCartAdapter = new UserCartAdapter(dataSet, username, totalCartPrice);
                    userCartAdapter.updateTotalPrice(); // display the initial total price
                    recyclerView = findViewById(R.id.regUserCart_recyclerViewResult);
                    recyclerView.setAdapter(userCartAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    userCartAdapter.notifyDataSetChanged();

                    // Set back button in cart fragment
                    Button backButton= findViewById(R.id.regUserCart_backBtn);
                    backButton.setOnClickListener(v1 -> {
                        // the logged in username will be added to the fragment
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("username", username);
                        preparationForDisplay(true, false);
                        Navigation.findNavController(MainActivity.this, R.id.fragmentContainerViewDefault).navigate(R.id.action_userCartFragment_to_shoppingFragment, bundle1);
                    });

                    // Set purchase button functionality
                    Button purchaseButton= findViewById(R.id.regUserCart_purchaseBtn);
                    purchaseButton.setOnClickListener(v2 -> {
                        if (userCartAdapter.getItemCount() > 0) {
                            Toast.makeText(MainActivity.this, "Processing your order...", Toast.LENGTH_LONG).show();

                            // Update stock and clear cart after purchase
                            jewelryRepository.updateQuantitiesInStock(username, MainActivity.this).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    userCartRepository.removeAllFromUserCart(username).addOnCompleteListener(removeTask -> {
                                        if (removeTask.isSuccessful()) {
                                            dataSet.clear();
                                            userCartAdapter.notifyDataSetChanged();
                                            totalCartPrice.setText("0.0");
                                            Toast.makeText(MainActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Error removing items: " + removeTask.getException(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this, "The purchase was not completed.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            purchaseButton.setEnabled(false);
                        }
                    });

                } else {
                    // Handle error loading data
                    Toast.makeText(MainActivity.this, "Error loading data: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }


    /**
     * The function manages all actions of menu display of category options.
     * Sets listeners for each category button (necklaces, rings, bracelets, earrings).
     */
    private void menuAndLogoHandler()
    {
        // Set menu display will be invisible
        View menu = findViewById(R.id.menu_all);
        menu.setVisibility(View.INVISIBLE);

        // ==================== handle clicking on menu options ====================
        // ---- necklaces ----
        Button menu_necklaces_btn = menu.findViewById(R.id.menu_necklaces_btn);
        menu_necklaces_btn.setOnClickListener(v -> handleCategoryButtonClick("necklaces"));

        // ---- rings ----
        Button menu_rings_btn = menu.findViewById(R.id.menu_rings_btn);
        menu_rings_btn.setOnClickListener(v -> handleCategoryButtonClick("rings"));

        // ---- bracelets ----
        Button menu_bracelets_btn = menu.findViewById(R.id.menu_bracelets_btn);
        menu_bracelets_btn.setOnClickListener(v -> handleCategoryButtonClick("bracelets"));

        // ---- earrings ----
        Button menu_earrings_btn = menu.findViewById(R.id.menu_earrings_btn);
        menu_earrings_btn.setOnClickListener(v -> handleCategoryButtonClick("earrings"));

        // ==================== handle clicking logo ====================
        handleLogoClick();

    }

    /**
     * This is a function for click on logo
     * 1. performs a task which fetches all items in database
     * 2. changes the content of recycle view accordingly
     */
    private void handleLogoClick()
    {
        View logo = findViewById(R.id.logo);
        logo.setOnClickListener(v -> jewelryRepository.getRowsByCategoryName("all").addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                ArrayList<JewelryItem> dataSet = new ArrayList<>();
                DataSnapshot allCategoriesSnapshot = task.getResult();

                // Iterate over all categories and add items to the dataSet
                for (DataSnapshot categorySnapshot : allCategoriesSnapshot.getChildren()) {
                    Utils.addItemsFromDatasetByCategory(categorySnapshot, dataSet);
                }

                // Update the RecyclerView with the combined dataSet
                jewelryAdapter = new JewelryAdapter(dataSet, username);
                recyclerView = findViewById(R.id.regShop_recyclerViewResult);
                recyclerView.setAdapter(jewelryAdapter);
                jewelryAdapter.notifyDataSetChanged();
            } else {
                // Handle error
                Toast.makeText(MainActivity.this, "Error loading data: " + task.getException(), Toast.LENGTH_LONG).show();
            }
        }));
    }

    /**
     * This is a general function for click on menu options:
     * 1. performs a task which fetches items related to the given category
     * 2. changes the content of recycle view accordingly
     * @param categoryName The category to fetch items for
     */
    private void handleCategoryButtonClick(String categoryName) {
        jewelryRepository.getRowsByCategoryName(categoryName).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null)
            {
                ArrayList<JewelryItem> dataSet = new ArrayList<>();
                DataSnapshot categorySnapshot = task.getResult();
                Utils.addItemsFromDatasetByCategory(categorySnapshot, dataSet);
                // Update the RecyclerView with the category data
                jewelryAdapter = new JewelryAdapter(dataSet, username);
                recyclerView = findViewById(R.id.regShop_recyclerViewResult);
                recyclerView.setAdapter(jewelryAdapter);
                jewelryAdapter.notifyDataSetChanged();
            } else {
                // Handle error loading data
                Toast.makeText(MainActivity.this, "Error loading data: " + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Login functionality with Firebase Authentication
    public void logInFunc(View viewScreen) {

        EditText usernameEditText = findViewById(R.id.entranceFrag_username);
        EditText passwordEditText = findViewById(R.id.entranceFrag_password);
        username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            // error message
            Toast.makeText(MainActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Firebase Authentication requires email to create new users by default
            Therefore, create a fictitious email for each user */
        String email = username + "@example.com";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success:
                        // update UI with the signed-in user's information

                        // the logged in username will be added to the fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        getUserData();

                        Navigation.findNavController(viewScreen).navigate(R.id.action_entranceFragment_to_shoppingFragment, bundle);

                        preparationForDisplay(true, false);

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Register functionality with Firebase Authentication
    public void registerFunc(View viewScreen) {
        EditText usernameEditText = findViewById(R.id.regFrag_username);
        EditText passwordEditText = findViewById(R.id.regFrag_password);
        username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        /* Firebase Authentication requires email to create new users by default
            Therefore, create a fictitious email for each user */
        String email = username + "@example.com";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success:
                        // update UI with the signed-in user's information

                        username = usernameEditText.getText().toString();
                        String phone = ((EditText) findViewById(R.id.regFrag_phone)).getText().toString();
                        // get the username and phone from the registration
                        userRepository.insertUsername(username, phone);

                        // The logged in username will be added to the fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);

                        getUserData();
                        Navigation.findNavController(viewScreen).navigate(R.id.action_registerationFragment_to_shoppingFragment, bundle);

                        preparationForDisplay(true, false);

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Retrieves user data from Firebase and displays it.
     * Fetches the user's phone number and sets a welcome message.
     */
    public void getUserData() {
        // Reference to the "users" node in the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Querying the database to find the user with the given username
        Query query = usersRef.orderByChild("username").equalTo(username);

        // Find the TextView to display the welcome message
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);

        // Adding a listener to handle the query result
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If user data exists, fetch the details
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Get the phone and username of the user
                        String phone = userSnapshot.child("phone").getValue(String.class);
                        String usernameDisplay = userSnapshot.child("username").getValue(String.class);

                        // Set the welcome message
                        String text = "Hi, " + usernameDisplay + "!";
                        welcomeMessage.setText(text);
                        Log.d("UserDetails", "Phone: " + phone);
                    }
                }
                else {
                    Log.d("UserDetails", "Not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });

    }

    /**
     * Prepares UI for display based on whether the header is shown or if the cart is visible.
     * @param isHeaderShown     True if the header should be shown, false if not.
     * @param isCart            True if the cart view should be shown, false if not.
     */
    public void preparationForDisplay(boolean isHeaderShown, boolean isCart)
    {
        View headerImageView = findViewById(R.id.headerImageView);
        View menu = findViewById(R.id.menu_all);
        View logo = findViewById(R.id.logo);
        View shoppingCartButton = findViewById(R.id.shoppingCartButton);
        View logoutButton= findViewById(R.id.logoutBtn);
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);

        if (isHeaderShown)
        {
            headerImageView.setVisibility(View.INVISIBLE);
            menu.setVisibility(View.VISIBLE);
            logo.setEnabled(true);
            shoppingCartButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            welcomeMessage.setVisibility(View.VISIBLE);
        }
        else {
            if (!isCart) {
                headerImageView.setVisibility(View.VISIBLE);
                username = null;
            }
            else {
                headerImageView.setVisibility(View.INVISIBLE);
            }
            menu.setVisibility(View.INVISIBLE);
            logo.setEnabled(false);
            shoppingCartButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
            welcomeMessage.setVisibility(View.INVISIBLE);
        }
    }
}