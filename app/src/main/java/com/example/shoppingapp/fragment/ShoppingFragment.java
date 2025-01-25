package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.JewelryAdapter;
import com.example.shoppingapp.model.JewelryItem;
import com.example.shoppingapp.Utils;
import com.example.shoppingapp.repository.JewelryRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<JewelryItem> dataSet;
    private RecyclerView recyclerView;
    private JewelryAdapter jewelryAdapter;
    private final JewelryRepository jewelryRepository = new JewelryRepository();

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /* Inflates the fragment layout, initializes the RecyclerView,
       and sets up Firebase listeners to fetch and display jewelry items.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewScreen = inflater.inflate(R.layout.fragment_shopping, container, false);

        /* dataSet is a new ArrayList object that will contain all the jewelry that are available in the store and display them in the RecyclerView.
           Each object in the dataset will be a JewelryItem object.*/
        dataSet = new ArrayList<>();
        recyclerView = viewScreen.findViewById(R.id.regShop_recyclerViewResult); // initialization to recyclerView

        // Set up GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        /* setItemAnimator determines the animation to be performed when items in the RecyclerView are added or removed.
            DefaultItemAnimator specifies to use default animations, such as adding and removing items.*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Get the username from the Bundle arguments
        Bundle arguments = getArguments();
        String username;
        if (arguments != null) {
            username = arguments.getString("username", "");
        } else {
            username = null;
        }
        /* Adding jewellery from database to the dataSet
           Reference to the "users" node in the Firebase Database */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference jewelryRef = database.getReference("jewelry");

        // Adding a listener to read data from database
        jewelryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Call getDatasetByCategoryName with "all" to get all categories
                jewelryRepository.getRowsByCategoryName("all").addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // If the task was successful, process the dataSnapshot
                        ArrayList<JewelryItem> dataSet = new ArrayList<>();
                        DataSnapshot allCategoriesSnapshot = task.getResult();

                        // Iterate over each category in the dataSnapshot and add items to the dataset
                        for (DataSnapshot categorySnapshot : allCategoriesSnapshot.getChildren()) {
                            Utils.addItemsFromDatasetByCategory(categorySnapshot, dataSet);
                        }

                        // Set up the RecyclerView Adapter with the updated dataset
                        jewelryAdapter = new JewelryAdapter(dataSet, username);
                        recyclerView = recyclerView.findViewById(R.id.regShop_recyclerViewResult);
                        recyclerView.setAdapter(jewelryAdapter);
                        jewelryAdapter.notifyDataSetChanged(); // Notify the adapter that the dataset has changed
                    } else {
                        // Handle error
                        Log.e("FirebaseError", "Error loading data: " + task.getException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

        return viewScreen;
    }
}