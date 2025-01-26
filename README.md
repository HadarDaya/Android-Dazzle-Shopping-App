# DAZZLE - Jewellery Shopping App ✨💍

**DAZZLE** is a mobile shopping application developed for jewelry sales. It provides users with a unique wide range of jewellery items, such as necklaces, rings, bracelets, and earrings, which they can browse and add to their shopping cart. 
The app is built using **Android SDK** with **Firebase** integration for user authentication and data storage.

---

### Features:
- **User Authentication 🔐:**
   Register and log in securely via Firebase Authentication. A unique email is generated for each user, ensuring a safe sign-in experience.
- **Persistent Shopping Cart 🛒:**
  The app ensures that users' shopping carts remain intact even after logging out. When the user logs back in, their cart will be restored, including items they had previously added before logging out. 
- **Jewellery Catalog 💎:**
  Users can browse various categories of jewellery items (necklaces, rings, bracelets, earrings), with data fetched from Firebase Database.
-	**Shopping Cart  🛍️:**
  Logged-in users can add jewellery items to their cart, view total prices, and proceed with purchases. Items in the cart are dynamically updated with Firebase, allowing users to change item quantities and remove items.
-	**Purchase Flow  💳:**
   Users can place orders, which updates stock quantities in the Firebase database and removes items from their cart.
- **UI and Navigation  🔄:**
   The app uses a combination of fragments and activities for smooth navigation between the catalog, cart, and user login/registration screens.
- **Modular Code Design ⚙️:**
   The app is designed with modular adapters and helpers to manage data and UI updates efficiently.
---
### Technologies Used  💻:
-	 **Java ☕:** Main programming language for the Android app.
-	**Firebase 🔥:** For user authentication, real-time database management, and data storage.
-	**Android SDK 📱:** Used for building the app's user interface, managing views, and handling user interactions.
-	**RecyclerView 🔄:** Used to display the list of jewellery items and shopping cart items dynamically.
-	**Toast 🍞:** For providing user feedback (error messages, success notifications).
---

### Setup ⚙️:
1.	Clone the repository.
2.	Open the project in **Android Studio**.
3.	 Set up Firebase in your project by adding the google-services.json file.
4.	Build and run the app on an emulator or a physical device.
---

### Screenshots and Descriptions of the App: 📱 ✨ 📸:
#### 1. **Login Screen  🔑**
The screen where users log into the app using a username and password.

- **Key Components:**
  -	EditText fields for entering the username and password.
  -	A "Login" button.
  -	A "Sign Up" button to navigate to the registration screen.
  -	
- **Interactions:**
  -	On successful login, the user is redirected to the **Home Screen**.
  -	Error message displayed for incorrect login details.
  
![image](https://github.com/user-attachments/assets/7323b6f4-d109-4b6b-9769-426ccc0424e3)
---

#### 2. **Registration Screen ✍️**
New users can create an account by providing necessary details.

- **Key Components:**
  -	Fields to input personal details: username,  password, password confirmation and phone.
  -	A "Register" button.
  - Option to return to the login screen.

- **Interactions:**
  - Validation to ensure the username is unique.
  -	On successful registration, users are directed to the **Shopping Screen**.
  -	Error messages for invalid input.
  
![image](https://github.com/user-attachments/assets/2b4fcb2e-c246-421d-8505-e84943137ef7)
---

#### 3. **Shopping Screen  🛍️**
The main screen of the app, showcasing the available jewelry categories and items.

- **Key Components:**
  - A toolbar with navigation options (categories, cart, logout).
  -	Jewelry categories (necklaces, rings, bracelets, earrings).
  -	A logo that resets the view to show all items.
  -	A RecyclerView for displaying jewelry items dynamically.
  -	
- **Interactions:**
  - Clicking on a category filters the items.
  -	Clicking on the logo resets the view to show all items.
  -	Selecting an add to cart icon will add the item to the shopping cart only once. 
    To add the product more than once, edit the Shopping Cart Screen.
   	
![image](https://github.com/user-attachments/assets/3133c8c1-24fb-45e3-b2a9-09b497d653cf)
---

#### 4. **Shopping Cart Screen  🛒**
Displays the user's cart items with adjustable quantities and total price.

- **Key Components:**
  - RecyclerView listing the jewelry items added to the cart.
  -	EditText fields per item for entering the quantity (between 1-5).
  - "Delete" button to return to remove specific item from the cart.
  - TextView showing the total price of the item based on its quantity.
  - TextView showing the total price of the cart.
  - "Back" button to return to the Home Screen.
  - "Purchase" button to place an order.
  - 
- **Interactions:**
  -	Users can update item quantities or remove items from the cart.
  -	Total price is updated dynamically based on cart contents.
  -	On successful purchase, the cart is cleared, and a success message is displayed.

![image](https://github.com/user-attachments/assets/15878540-e26f-475e-9224-fce867db22d8)
---

Feel free to explore the app and test its features. Enjoy your shopping experience with **DAZZLE**! 💎✨









