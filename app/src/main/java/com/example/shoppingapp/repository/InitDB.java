package com.example.shoppingapp.repository;

import androidx.annotation.NonNull;

import com.example.shoppingapp.model.JewelryItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InitDB {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Initializes the 'jewelry' table in the Firebase Realtime Database.
     * If the table does not exist or has been changed, it will insert jewelry data into the database.
     */
    public void loadTablesInDataBase()
    {
        // initialize 'jewelry' table (if not exists, or was changed)
        DatabaseReference table = database.getReference("jewelry");
        table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    insertJewelryToDatabase();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Inserts jewelry items into the Firebase Realtime Database.
     * Jewelry items are categorized into necklaces, bracelets, earrings, and rings.
     */
    public void insertJewelryToDatabase()
    {
        // Create a JewelryItems
        JewelryItem blueBeachStoneNecklaceNecklace = new JewelryItem(
                1,
                "The Blue Beach Stone necklace",
                "necklaces",
                70.0,
                25,
                "bluebeachstoneneck"
        );

        JewelryItem sparklingSquareNecklace = new JewelryItem(
                2,
                "The Sparkling Square Stone necklace",
                "necklaces",
                70.0,
                25,
                "sparklingsquareneck"
        );
        JewelryItem rosegoldPearlNecklace = new JewelryItem(
                3,
                "The Rose Gold Pearl necklace",
                "necklaces",
                85.0,
                0,
                "rosegoldpearlneck"
        );
        JewelryItem rosesilverPearlNecklace = new JewelryItem(
                4,
                "The Rose Silver Pearl necklace",
                "necklaces",
                70.0,
                10,
                "rosesilverpearlneck"
        );
        JewelryItem rhombusGlowNecklace = new JewelryItem(
                5,
                "The Rhombus Glow necklace",
                "necklaces",
                85.0,
                20,
                "rhombusglowneck"
        );
        JewelryItem goldenPearlNecklace = new JewelryItem(
                6,
                "The Golden Pearl necklace",
                "necklaces",
                140.0,
                20,
                "rosegoldpearlneck"
        );
        JewelryItem silverPearlNecklace = new JewelryItem(
                7,
                "The Silver Pearl necklace",
                "necklaces",
                120.0,
                20,
                "silverpearlneck"
        );
        JewelryItem lightPearlNecklace = new JewelryItem(
                8,
                "The Light Pearl necklace",
                "necklaces",
                90.0,
                25,
                "lightpearlneck"
        );
        JewelryItem threePearlNecklace = new JewelryItem(
                9,
                "The Three Pearl necklace",
                "necklaces",
                105.0,
                25,
                "threepearlneck"
        );
        JewelryItem starlightlNecklace = new JewelryItem(
                10,
                "The Starlight necklace",
                "necklaces",
                70.0,
                25,
                "starlightlnecklace"
        );
        JewelryItem seaPearlNecklace = new JewelryItem(
                11,
                "The Sea Pearl necklace",
                "necklaces",
                105.0,
                10,
                "seapearlnecklace"
        );
        JewelryItem pureLoveNecklace = new JewelryItem(
                12,
                "The Pure Love necklace",
                "necklaces",
                80.0,
                30,
                "pureloveneck"
        );
        JewelryItem luxuryPearlNecklaceSilver = new JewelryItem(
                13,
                "The Luxury Pearl necklace",
                "necklaces",
                150.0,
                10,
                "luxurypearlnecklsilver"
        );
        JewelryItem luxuryPearlNecklaceGold = new JewelryItem(
                14,
                "The Luxury Pearl necklace",
                "necklaces",
                180.0,
                10,
                "luxurypearlneckgold"
        );
        JewelryItem classicNecklaceSilver = new JewelryItem(
                15,
                "The Classic necklace",
                "necklaces",
                60.0,
                30,
                "classicnecksilver"
        );
        JewelryItem classicNecklaceGold = new JewelryItem(
                16,
                "The Classic necklace",
                "necklaces",
                70.0,
                30,
                "classicneckgold"
        );

        JewelryItem silverPearlBracelet = new JewelryItem(
                17,
                "The Silver Pearl bracelet",
                "bracelets",
                70.0,
                0,
                "silverpearlbracelet"
        );
        JewelryItem goldPearlBracelet = new JewelryItem(
                17,
                "The Gold Pearl bracelet",
                "bracelets",
                80.0,
                30,
                "goldpearlbracelet"
        );

        JewelryItem rhombusGlowBracelet = new JewelryItem(
                18,
                "The Rhombus Glow bracelet",
                "bracelets",
                50.0,
                30,
                "rhombusglowbracelet"
        );
        JewelryItem silverBeadedBracelet = new JewelryItem(
                19,
                "The Silver Beaded bracelet",
                "bracelets",
                40.0,
                30,
                "silverbeadedbracelet"
        );
        JewelryItem luxuryBraceletM = new JewelryItem(
                20,
                "The Luxury silver bracelet- size M",
                "bracelets",
                130.0,
                0,
                "luxurybraceletm"
        );
        JewelryItem luxuryBraceletS = new JewelryItem(
                21,
                "The Luxury silver bracelet- size S",
                "bracelets",
                100.0,
                25,
                "luxurybracelets"
        );

        JewelryItem classicSilverEarring = new JewelryItem(
                22,
                "The Classic Silver Earring",
                "earrings",
                80.0,
                25,
                "classicsilverearring"
        );
        JewelryItem classicGoldEarring = new JewelryItem(
                23,
                "The Classic Gold Earring",
                "earrings",
                90.0,
                25,
                "classicgoldearring"
        );
        JewelryItem rosegoldPearlEarring = new JewelryItem(
                24,
                "The Rose Gold Pearl Earring",
                "earrings",
                70.0,
                10,
                "rosegoldpearlearring"
        );
        JewelryItem rosesilverPearlEarring = new JewelryItem(
                25,
                "The Rose Silver Pearl Earring",
                "earrings",
                60.0,
                10,
                "rosesilverpearlearring"
        );
        JewelryItem luxuryPearlEarringSilver = new JewelryItem(
                26,
                "The Luxury Pearl Earring",
                "earrings",
                100.0,
                10,
                "luxurypearlearringsilver"
        );
        JewelryItem luxuryPearlEarringGold = new JewelryItem(
                27,
                "The Luxury Pearl Earring",
                "earrings",
                110.0,
                10,
                "luxurypearlearringgold"
        );
        JewelryItem everydayEarringGold = new JewelryItem(
                28,
                "The earring for everyday",
                "earrings",
                60.0,
                30,
                "everydayearringgold"
        );
        JewelryItem everydayEarringSilver = new JewelryItem(
                29,
                "The earring for everyday",
                "earrings",
                50.0,
                30,
                "everydayearringsilver"
        );
        JewelryItem heartEarring = new JewelryItem(
                30,
                "The Heart earring",
                "earrings",
                40.0,
                40,
                "heartearring"
        );

        JewelryItem blueBeachStoneRing = new JewelryItem(
                31,
                "The Blue Beach Stone ring",
                "rings",
                60.0,
                25,
                "bluebeachstonering"
        );
        JewelryItem classicRing = new JewelryItem(
                32,
                "The Classic ring",
                "rings",
                70.0,
                30,
                "classicring"
        );
        JewelryItem luxuryRing = new JewelryItem(
                33,
                "The Luxury ring",
                "rings",
                90.0,
                30,
                "luxuryring"
        );
        JewelryItem purpleStoneRing = new JewelryItem(
                34,
                "The Purple Stone ring",
                "rings",
                55.0,
                25,
                "purplestonering"
        );
        JewelryItem uniqueFlowerRing = new JewelryItem(
                35,
                "A Unique Flower ring",
                "rings",
                80.0,
                15,
                "uniqueflowerring"
        );
        JewelryItem shellRing = new JewelryItem(
                37,
                "A Shell ring",
                "rings",
                75.0,
                25,
                "shellring"
        );
        JewelryItem everydayRingSilver = new JewelryItem(
                38,
                "The ring for everyday",
                "rings",
                40.0,
                30,
                "everydayringsilver"
        );
        JewelryItem everydayRingGold = new JewelryItem(
                39,
                "The ring for everyday",
                "rings",
                50.0,
                30,
                "everydayringgold"
        );

        // Add jewelry item to Firebase Realtime Database
        // Add to "necklaces" category
        addJewelryItem("necklaces", "entry"
                + blueBeachStoneNecklaceNecklace.getId(), blueBeachStoneNecklaceNecklace);
        addJewelryItem("necklaces", "entry"
                + sparklingSquareNecklace.getId(), sparklingSquareNecklace);
        addJewelryItem("necklaces", "entry"
                + rosegoldPearlNecklace.getId(), rosegoldPearlNecklace);
        addJewelryItem("necklaces", "entry"
                + rosesilverPearlNecklace.getId(), rosesilverPearlNecklace);
        addJewelryItem("necklaces", "entry"
                + rhombusGlowNecklace.getId(), rhombusGlowNecklace );
        addJewelryItem("necklaces", "entry"
                + goldenPearlNecklace.getId(), goldenPearlNecklace );
        addJewelryItem("necklaces", "entry"
                + silverPearlNecklace.getId(), silverPearlNecklace );
        addJewelryItem("necklaces", "entry"
                + lightPearlNecklace.getId(), lightPearlNecklace );
        addJewelryItem("necklaces", "entry"
                + threePearlNecklace.getId(), threePearlNecklace );
        addJewelryItem("necklaces", "entry"
                + starlightlNecklace.getId(), starlightlNecklace );
        addJewelryItem("necklaces", "entry"
                + seaPearlNecklace.getId(), seaPearlNecklace );
        addJewelryItem("necklaces", "entry"
                + pureLoveNecklace.getId(), pureLoveNecklace );
        addJewelryItem("necklaces", "entry"
                + luxuryPearlNecklaceSilver.getId(), luxuryPearlNecklaceSilver );
        addJewelryItem("necklaces", "entry"
                + luxuryPearlNecklaceGold.getId(), luxuryPearlNecklaceGold );
        addJewelryItem("necklaces", "entry"
                + classicNecklaceSilver.getId(), classicNecklaceSilver );
        addJewelryItem("necklaces", "entry"
                + classicNecklaceGold.getId(), classicNecklaceGold );

        // Add to "bracelets" category
        addJewelryItem("bracelets", "entry"
                + silverPearlBracelet.getId(), silverPearlBracelet );
        addJewelryItem("bracelets", "entry"
                + goldPearlBracelet.getId(), goldPearlBracelet );
        addJewelryItem("bracelets", "entry"
                + rhombusGlowBracelet.getId(), rhombusGlowBracelet );
        addJewelryItem("bracelets", "entry"
                + silverBeadedBracelet.getId(), silverBeadedBracelet );
        addJewelryItem("bracelets", "entry"
                + luxuryBraceletM.getId(), luxuryBraceletM );
        addJewelryItem("bracelets", "entry"
                + luxuryBraceletS.getId(), luxuryBraceletS );

        // Add to "earrings" category
        addJewelryItem("earrings", "entry"
                + classicSilverEarring.getId(),classicSilverEarring );
        addJewelryItem("earrings", "entry"
                + classicGoldEarring.getId(),classicGoldEarring );
        addJewelryItem("earrings", "entry"
                + rosesilverPearlEarring.getId(),rosesilverPearlEarring );
        addJewelryItem("earrings", "entry"
                + rosegoldPearlEarring.getId(),rosegoldPearlEarring);
        addJewelryItem("earrings", "entry"
                + luxuryPearlEarringSilver.getId(),luxuryPearlEarringSilver);
        addJewelryItem("earrings", "entry"
                + luxuryPearlEarringGold.getId(),luxuryPearlEarringGold);
        addJewelryItem("earrings", "entry"
                + everydayEarringSilver.getId(),everydayEarringSilver);
        addJewelryItem("earrings", "entry"
                + everydayEarringGold.getId(),everydayEarringGold);
        addJewelryItem("earrings", "entry"
                + heartEarring.getId(),heartEarring);

        // Add to "rings" category
        addJewelryItem("rings", "entry"
                + blueBeachStoneRing.getId(),blueBeachStoneRing );
        addJewelryItem("rings", "entry"
                + classicRing.getId(),classicRing );
        addJewelryItem("rings", "entry"
                + luxuryRing.getId(),luxuryRing );
        addJewelryItem("rings", "entry"
                + purpleStoneRing.getId(),purpleStoneRing );
        addJewelryItem("rings", "entry"
                + uniqueFlowerRing.getId(),uniqueFlowerRing );
        addJewelryItem("rings", "entry"
                + shellRing.getId(), shellRing );
        addJewelryItem("rings", "entry"
                + everydayRingSilver.getId(),everydayRingSilver );
        addJewelryItem("rings", "entry"
                + everydayRingGold.getId(),everydayRingGold );
    }

    /**
     * Adds a jewelry item to the Firebase Realtime Database under a specific category.
     * @param category  The category (e.g., "necklaces", "bracelets").
     * @param entryNum  The entry number of the jewelry item.
     * @param item      The JewelryItem object to be added.
     */
    private void addJewelryItem(String category, String entryNum, JewelryItem item) {
        // Push the jewelry item into the correct category (necklaces, earrings, etc.)
        DatabaseReference table = database.getReference("jewelry");
        table.child(category).child(entryNum).setValue(item);
    }
}
