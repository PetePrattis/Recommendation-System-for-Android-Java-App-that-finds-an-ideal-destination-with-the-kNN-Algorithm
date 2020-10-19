package com.unipi.p15013p15120.kastropoliteiesv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //xwris leitourgikotita tis ARXIKIS
    private static final String TAG = "HomePage";
    private Dialog2 dialog;

    public static boolean welcome;
    public static boolean executed = true;
    public static boolean first = true;
    Random r = new Random();
    Intent i;

    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    String userID;
    Spinner spinner;

    static boolean active, exit, exitshow, notfirst, showprompt, fromchangeq;

    String cuid,photo;

    //the arraylists, arrays and lists in which the location information from the database will be saved
    public static ArrayList<String> locations = new ArrayList<String>();
    static ArrayList<String> locations_name = new ArrayList<String>();
    static ArrayList<String> locations_image = new ArrayList<String>();

    public static ArrayList<String> lakwnia = new ArrayList<String>();
    public static ArrayList<String> lakwnia_name = new ArrayList<String>();
    public static ArrayList<String> lakwnia_image = new ArrayList<String>();

    public static ArrayList<String> messinia = new ArrayList<String>();
    public static ArrayList<String> messinia_name = new ArrayList<String>();
    public static ArrayList<String> messinia_image = new ArrayList<String>();

    public static ArrayList<Integer> user_history_ratings = new ArrayList<Integer>();
    public static ArrayList<String> user_history_locations_id = new ArrayList<>();
    public static ArrayList<String> user_history_locations_name = new ArrayList<>();
    public static ArrayList<String> user_history_locations_image = new ArrayList<>();


    ArrayList<Integer[]> allepiskepsimotita = new ArrayList<>();
    List<Integer> allep = new ArrayList<>();
    ArrayList<Integer[]> userepiskepsimotita = new ArrayList<>();
    List<Integer> userep = new ArrayList<>();

    ArrayList<Integer[]> allratings = new ArrayList<>();
    List<Integer> allr = new ArrayList<>();

    ArrayList<Integer[]> ratings = new ArrayList<>();
    List<Integer> rn = new ArrayList<>();

    ArrayList<Integer> newr = new ArrayList<>();
    Integer[] cur;

    ArrayList<Integer[]> loccharacteristics = new ArrayList<>();
    List<Integer> locchar = new ArrayList<>();

    ArrayList<Integer> cuquestionnaire = new ArrayList<>();
    Integer[] cuquest;

    static int actualratings;

    //the parameter for knn algorithm
    private static final int k = 19;

    //string array forte spinner
    String[] nomos = {"Επιλέξτε νομό προς αναζήτηση...", "Λακωνία", "Μεσσηνία", "Όλους"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Layout
        setContentView(R.layout.home_page);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setSubtitle("Αρχική");

        //design
        MaterialCardView cardView1 = findViewById(R.id.cardView1);
        MaterialCardView cardView2 = findViewById(R.id.cardView2);
        MaterialCardView cardView3 = findViewById(R.id.cardView3);
        MaterialCardView cardView4 = findViewById(R.id.cardView4);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        userID = mAuth.getCurrentUser().getUid();

        //method that takes the static information of locations
        getStaticContent();

        //we check for intent variable
        if (getIntent() != null) {
            if (getIntent().hasExtra("welcome_message")) {//if intent has extra 'welcome_message'
                //this means that we are at the user's first time in home page after questionnaire
                welcome = true;
                first = true;
                //and we show the welcome dialog using Dialog2
                dialog = new Dialog2("Καλως ήρθατε!", "welcome_message", getResources().getString(R.string.welcome_message));
                dialog.show(getSupportFragmentManager(), "welcome_message");
            }
            if (getIntent().hasExtra("changedq")) {//if intent has extra 'changedq'
                //we came from ChangeQuestionnaire class
                fromchangeq = true;
            }
        }

        //we initialize the on click listeners for each cardview in main page
        //for recommended locations
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc_rec!= null && rec_image != null) {//if the lists needed aren't null
                    i = new Intent(getApplicationContext(), LocationsList.class);
                    i.putExtra("where", "recommended");//we set extra variable 'where'  in intent 'recommended' to know what we want to show
                    if (actualratings > 0)//if user has rated at least one location
                        startActivity(i);
                    else//ealse we show negative message
                        Toast.makeText(getApplicationContext(), "Δεν έχετε βαθμολογήσει κάποια τοποθεσία για να εμφανίσουμε τις προτεινόμενες ακόμα!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //fot matched locations
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc_matched != null && matched_image != null) {//if the lists needed aren't null
                    i = new Intent(getApplicationContext(), LocationsList.class);
                    i.putExtra("where", "matched");//we set extra variable 'where' in intent 'matched' to know what we want to show
                    startActivity(i);
                }
            }
        });

        //for top locations
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc_top != null && top_image != null){//if the lists needed aren't null
                    i = new Intent(getApplicationContext(), LocationsList.class);
                    i.putExtra("where", "top_rated");//we set extra variable 'where' in intent 'top_rated' to know what we want to show
                    startActivity(i);
                }

            }
        });

        //for hot locations
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findHotLocations();
                if (loc_hot != null && hot_image != null) {//if the lists needed aren't null
                    i = new Intent(getApplicationContext(), LocationsList.class);
                    i.putExtra("where", "hot_locations");//we set extra variable 'where' in intent 'hot_locations' to know what we want to show
                    startActivity(i);
                }
            }
        });


        //we initialize the spinner
        spinner = (Spinner) findViewById(R.id.spinner);//(Spinner) itemM.getActionView();//
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nomos);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        //oncreate the default choice is zero
        spinner.setSelection(0);

    }

    long location_size;
    public void getStaticContent() {//method to get information of static locations
        lakwnia.clear();
        lakwnia_name.clear();
        lakwnia_image.clear();
        messinia.clear();
        messinia_name.clear();
        messinia_image.clear();
        locations.clear();
        locations_name.clear();
        locations_image.clear();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //listener to get the information
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                cuid = currentUser.getUid();

                //we go through the data snapshot
                for (DataSnapshot ds : dataSnapshot.child("topothesies").getChildren()) {
                    location_size = dataSnapshot.child("topothesies").getChildrenCount();
                    locations.add(ds.getKey()); //we add the id to arraylist
                    locations_name.add(ds.child("region_name_gr").getValue().toString());//we add name to arraylist
                    locations_image.add(ds.child("image").getValue().toString());//we add image name to arraylist

                    //for each of the tw ocounties we do the same as befora
                    if (ds.getKey().contains("LAK")) {
                        lakwnia.add(ds.getKey()); //ID
                        lakwnia_name.add(ds.child("region_name_gr").getValue().toString());//NAME
                        lakwnia_image.add(ds.child("image").getValue().toString());//image
                    } else if (ds.getKey().contains("MES")) {
                        messinia.add(ds.getKey());//ID
                        messinia_name.add(ds.child("region_name_gr").getValue().toString());//NAME
                        messinia_image.add(ds.child("image").getValue().toString());//image
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getQuestionnaireAnswers(){//method to get user's answers to the questionnaire
        locchar.clear();
        cuquestionnaire.clear();
        loccharacteristics.clear();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                cuid = currentUser.getUid();

                //for each location
                for (String l : locations) {
                    //we get the location's information into an arraylist
                    locchar.clear();
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("seaside").getValue().toString()));
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("mountain").getValue().toString()));
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("flat").getValue().toString()));
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("history").getValue().toString()));
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("alternative_tourism").getValue().toString()));
                    locchar.add(Integer.parseInt(dataSnapshot.child("topothesies").child(l).child("night_life").getValue().toString()));

                    Integer[] locchararray = new Integer[locchar.size()];
                    locchararray = locchar.toArray(locchararray);
                    loccharacteristics.add(locchararray);
                }


                //we get current user's questionnaire answers inot an arraylist
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("thalassa").getValue().toString()));
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("vouno").getValue().toString()));
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("pediada").getValue().toString()));
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("istoria").getValue().toString()));
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("en_tour").getValue().toString()));
                cuquestionnaire.add(Integer.parseInt(dataSnapshot.child("questionnaire").child(cuid).child("nyxt_zwh").getValue().toString()));


                cuquest = new Integer[cuquestionnaire.size()];
                cuquest = cuquestionnaire.toArray(cuquest);

                loccharacteristics.add(cuquest);

                runKNNQ();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void promptLocation(){//method that determines location to prompt
        Random r = new Random();
        int answer1 = r.nextInt(5) + 1;//random number between 1-5
        if (answer1 == 5 && locations.size() != 0){//20% chance to be 5
            int answer2 = r.nextInt(5) + 1;//random number between 1-5
            int x = 0;
            if (actualratings==0 && answer2==5)//if user hasn't voted any location the prompt location will be from the matched list
                answer2 = 4;
            if (actualratings==locations.size() || locations.size()==0)//if user has voted all locations or location list is empty we don't prompt
                answer2 = -1;

            switch (answer2){
                case 1://case where a random location from all location list is prompted
                    while(x<= 10){
                        Integer ran = r.nextInt(locations.size());
                        if(!user_history_locations_id.contains(locations.get(ran))){//user must not have voted the location picked
                            String[] arr={locations.get(ran), locations_name.get(ran)};
                            dialog = new Dialog2("Σ'αρέσει αυτή η τοποθεσία;","rateHomepage",arr,locations_image.get(ran));//through Dialog2 we show message
                            dialog.show(getSupportFragmentManager(),"rateHomepage");
                            break;
                        }
                        else{
                            x++;
                        }
                    }
                    break;
                case 2://case where a random location from all top rated locations list is prompted
                    while(x<= 10){
                        Integer ran = r.nextInt(loc_top.length);
                        if(!user_history_locations_id.contains(loc_top[ran])){
                            String[] arr={loc_top[ran], top_name[ran]};
                            dialog = new Dialog2("Σ'αρέσει αυτή η Top Rated τοποθεσία;","rateHomepage",arr,top_image[ran]);
                            dialog.show(getSupportFragmentManager(),"rateHomepage");
                            break;
                        }
                        else{
                            x++;
                        }
                    }
                    break;
                case 3://case where a random location from hot locations list is prompted
                    while(x<= 10){
                        Integer ran = r.nextInt(loc_hot.length);
                        if(!user_history_locations_id.contains(loc_hot[ran])){
                            String[] arr={loc_hot[ran], hot_name[ran]};
                            dialog = new Dialog2("Σ'αρέσει αυτή η Hot τοποθεσία;","rateHomepage",arr,hot_image[ran]);
                            dialog.show(getSupportFragmentManager(),"rateHomepage");
                            break;
                        }
                        else{
                            x++;
                        }
                    }
                    break;
                case 4://case where a random location from matched locations list is prompted
                    while(x<= 10){
                        Integer ran = r.nextInt(loc_matched.length);
                        if(!user_history_locations_id.contains(loc_matched[ran])){
                            String[] arr={loc_matched[ran], matched_name[ran]};
                            dialog = new Dialog2("Σ'αρέσει αυτή η τοποθεσία που ταιριάζει στις προτιμήσεις σου;","rateHomepage",arr,matched_image[ran]);
                            dialog.show(getSupportFragmentManager(),"rateHomepage");
                            break;
                        }
                        else{
                            x++;
                        }
                    }
                    break;
                case 5://case where a random location from recommended locations list is prompted
                    while(x<= 10){
                        Integer ran = r.nextInt(loc_rec.length);
                        if(!user_history_locations_id.contains(loc_rec[ran])){
                            String[] arr={loc_rec[ran], rec_name[ran]};
                            dialog = new Dialog2("Σ'αρέσει αυτή η τοποθεσία που σου προτείνουμε σύμφωνα με άλλες βαθμολογήσεις σου;","rateHomepage",arr,rec_image[ran]);
                            dialog.show(getSupportFragmentManager(),"rateHomepage");
                            break;
                        }
                        else{
                            x++;
                        }
                    }
                    break;
            }
        }
    }

    public void doThings() {
        allep.clear();
        allepiskepsimotita.clear();
        userep.clear();
        userepiskepsimotita.clear();
        user_history_ratings.clear();
        user_history_locations_id.clear();
        user_history_locations_name.clear();
        user_history_locations_image.clear();
        rn.clear();
        allr.clear();
        newr.clear();
        ratings.clear();
        allratings.clear();
        locchar.clear();
        cuquestionnaire.clear();
        loccharacteristics.clear();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                cuid = currentUser.getUid();

                //we read from data snapshot the visitation for each location
                for (DataSnapshot ds: dataSnapshot.child("ratings").getChildren()) {
                    allep.clear();
                    for (DataSnapshot ds1: ds.getChildren()){
                        allep.add(Integer.parseInt(ds1.child("episkepsimotita").getValue().toString()));
                        if (cuid.equals(ds.getKey())) {
                            userep.add(Integer.parseInt(ds1.child("episkepsimotita").getValue().toString()));
                        }
                    }

                    Integer[] allepiskarray = new Integer[allep.size()];
                    allepiskarray = allep.toArray(allepiskarray);
                    allepiskepsimotita.add(allepiskarray);

                    Integer[] userepiskarray = new Integer[userep.size()];
                    userepiskarray = userep.toArray(userepiskarray);
                    userepiskepsimotita.add(userepiskarray);
                }

                //method to get questionnaire answers
                getQuestionnaireAnswers();

                ///we read from data snapshot the information for each location that the user has rated
                for (DataSnapshot ds : dataSnapshot.child("ratings").child(cuid).getChildren()) {
                    if (!ds.child("rating").getValue().toString().equals("0")) {
                        user_history_ratings.add(Integer.parseInt(ds.child("rating").getValue().toString()));
                        user_history_locations_id.add(ds.child("topothesies").getValue().toString());
                        for (DataSnapshot ds1: dataSnapshot.child("topothesies").getChildren()){
                            if (ds.child("topothesies").getValue().toString().equals(ds1.getKey())){
                                user_history_locations_name.add(ds1.child("region_name_gr").getValue().toString());
                                user_history_locations_image.add(ds1.child("image").getValue().toString());
                            }
                        }
                    }
                }

                //we read from data snapshot all ratings, all ratings except for user's and all user;s ratings
                for (DataSnapshot ds : dataSnapshot.child("ratings").getChildren()) {
                    rn.clear();
                    allr.clear();
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        if (!cuid.equals(ds.getKey())) {
                            rn.add(Integer.parseInt(ds1.child("rating").getValue().toString()));
                        }
                        else {
                            newr.add(Integer.parseInt(ds1.child("rating").getValue().toString()));
                        }
                        allr.add(Integer.parseInt(ds1.child("rating").getValue().toString()));
                    }
                    if (!rn.isEmpty()) {
                        Integer[] ratingarray = new Integer[rn.size()];
                        ratingarray = rn.toArray(ratingarray);
                        ratings.add(ratingarray);
                    }

                    Integer[] allratingarray = new Integer[allr.size()];
                    allratingarray = allr.toArray(allratingarray);
                    allratings.add(allratingarray);
                }
                cur = new Integer[newr.size()];
                //ratings of active user
                cur = newr.toArray(cur);

                actualratings = 0;
                for (int i = 0; i < +cur.length; i++) {
                    if (cur[i] != 0)
                        actualratings++;
                }

                //if user has at least rated one location we run knn algorithm
                if (actualratings > 0)
                    runKNN();

                //methods that sort and create the lists of hot, top, matched and recommended locations
                sortRecommended();
                sortMatched();
                findHotLocations();
                findTopRated();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //handler to prompt a location after one second
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //we will prompt a location only if we are still in HomePage activity, it is not the first time we are in HomePage class
                //if the exit message isn't shown, if th e welcome message isn't shown, if another prompt message isn't shown
                //and if we are haven't entered HomePage activity through ChangeQuestionnaire activity
                if (active && notfirst && !exitshow && !welcome && !showprompt && !fromchangeq) {
                    showprompt = true;
                    promptLocation();
                }
                else if(!notfirst)
                    notfirst = true;
                if(showprompt){
                    showprompt=false;
                }
                if(fromchangeq)
                    fromchangeq = false;
                executed = true;
            }
        }, 1000);
    }

    //arraylists that we will use in knn to create the recommended locations arraylist
    ArrayList<Integer> user = new ArrayList<Integer>();
    ArrayList<Integer> suser = new ArrayList<Integer>(); //sorted
    ArrayList<Double> sim = new ArrayList<Double>();
    ArrayList<Double> ssim = new ArrayList<Double>(); //sorted
    ArrayList<Double> recommended = new ArrayList<Double>();

    private void runKNN() {//method that executes the steps of k-nearest-neighbours algorithm
        recommended.clear();
        ssim.clear();
        sim.clear();
        suser.clear();
        user.clear();

        Integer[][] arr = new Integer[ratings.size() + 1][cur.length]; //ratings without active user
        int x = 0;
        for (Integer[] r : ratings) {
            arr[x] = r;
            x++;
        }

        arr[arr.length - 1] = cur;

        for (int i = 0; i < arr.length; i++) {//the metric we use for each user to find similarity to current user
            euclideanDistance(arr, i, arr.length - 1, user, sim, ssim);
        }

        Collections.sort(ssim, Collections.reverseOrder()); //we sort the similarity

        int remove = ssim.size() - k;
        for (int i = 0; i < remove; i++) {
            ssim.remove(ssim.size() - 1); //we keep only the k nearest neighbours
        }
        boolean once = true;
        for (int i = 0; i < ssim.size(); i++) {
            for (int j = 0; j < sim.size(); j++) {
                if (Math.floor(ssim.get(i) * 10000) == Math.floor(sim.get(j) * 10000) && once && !suser.contains(user.get(j))) {
                    once = false;
                    suser.add(user.get(j));
                }
            }
            once = true;
        }

        double weightedSum = 0;
        double similaritySum = 0;


        for (int i = 0; i < arr[0].length; i++) {//for locations
            for (int j = 0; j < arr.length - 1; j++) {//for users
                if (suser.contains(user.get(j))) {//if a user is one of the nearest neighbours
                    if (arr[arr.length - 1][i] == 0) {//if current user's rating is zero
                        //then we predict the rating using weighted similarity sum
                        weightedSum += arr[j][i] * sim.get(j); //
                        similaritySum += sim.get(j);
                    }
                }
            }
            if (weightedSum == 0) { //if there is no neighbour or all locations are rated
                recommended.add(-1.0);
            } else {
                weightedSum /= similaritySum; //we find the predicted rating and add it to the recommended arraylist
                recommended.add(weightedSum);
            }
            weightedSum = 0;
            similaritySum = 0;
        }
    }

    //method that finds the euclidean distance / similarity between current user and another user according to their ratings
    public static void euclideanDistance(Integer[][] arr, int i, int num, ArrayList<Integer> user, ArrayList<Double> sim, ArrayList<Double> ssim){
        double sumSquares = 0;
        boolean flag = true;
        for(int j=0; j<arr[0].length; j++) { //for each location
            if(i != num && arr[num][j]!=0 && arr[i][j]!=0) {//if it is not hte current user
                double diff = 0;
                diff = arr[num][j] - arr[i][j];
                sumSquares += diff*diff;
                flag = true;
            }
            else if (i == num) //if this is the current user
                flag = false;
        }
        if(flag) {
            double d = Math.sqrt(sumSquares);
            double similarity = 1/d;
            if (Double.isInfinite(similarity)) {
                similarity = 1;
            }
            user.add(i);
            sim.add(similarity);
            ssim.add(similarity);

        }

    }

    //arraylists that we will use in knn to create the matched locations arraylist
    ArrayList<Integer> loc = new ArrayList<Integer>();
    ArrayList<Integer> sloc = new ArrayList<Integer>(); //sorted
    ArrayList<Double> locsim = new ArrayList<Double>();
    ArrayList<Double> slocsim = new ArrayList<Double>(); //sorted

    //we do the same thing as with the runKNN() method but we don't predict any rating, we just want the similarity scores
    private void runKNNQ() {
        loc.clear();
        sloc.clear();
        locsim.clear();
        slocsim.clear();

        Integer[][] arr = new Integer[loccharacteristics.size()][cuquest.length];
        int x = 0;
        for (Integer[] r : loccharacteristics) {
            arr[x] = r;
            x++;
        }

        for (int i = 0; i < arr.length; i++) {
            cosineSimilarityQ(arr, i, arr.length - 1, loc, locsim, slocsim);
        }

        Collections.sort(slocsim, Collections.reverseOrder());
        int remove = slocsim.size() - k;
        for (int i = 0; i < remove; i++) {
            slocsim.remove(slocsim.size() - 1);
        }
        boolean once = true;
        for (int i = 0; i < slocsim.size(); i++) {
            for (int j = 0; j < locsim.size(); j++) {
                if (Math.floor(slocsim.get(i) * 10000) == Math.floor(locsim.get(j) * 10000) && once && !sloc.contains(loc.get(j))) { // x10000 gia na emfanistoun arketa dekadika psifia wste na ginei match
                    once = false;
                    sloc.add(loc.get(j));
                }
            }
            once = true;
        }
    }

    //method that finds the cosine similarity between current user's answers to questionnaire and a location's characteristics
    public static void cosineSimilarityQ(Integer[][] characteristics, int i, int num, ArrayList<Integer> location, ArrayList<Double> sim, ArrayList<Double> ssim) {
        double sumProduct = 0;
        double sumASq = 0;
        double sumBSq = 0;
        double similarity;

        for (int j = 0; j < characteristics[0].length; j++) { //gia oles tis topothesies
            if (i != num) {
                sumProduct += characteristics[num][j] * characteristics[i][j];
                sumASq += characteristics[num][j] * characteristics[num][j];
                sumBSq += characteristics[i][j] * characteristics[i][j];
            }
        }
        if (sumASq == 0 && sumBSq == 0) {
            similarity = 0.0;
        } else {
            similarity = sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));
            if (similarity > 1.0) {
                similarity = 1.0;
            }
        }
        location.add(i);
        sim.add(similarity);
        ssim.add(similarity);
    }

    //arrays used to sort the matched arraylists
    static Double[] smatchedsim;
    static String[] loc_matched, matched_name, matched_image;

    //method to sort the matched locations list
    public void sortMatched() {

        smatchedsim = new Double[locsim.size()];
        smatchedsim = locsim.toArray(smatchedsim);


        loc_matched = new String[locations.size()];
        loc_matched = locations.toArray(loc_matched);


        matched_name = new String[locations_name.size()];
        matched_name = locations_name.toArray(matched_name);


        matched_image = new String[locations_image.size()];
        matched_image = locations_image.toArray(matched_image);

        //basic for loops to sort more than one arrays according to one array's sorting
        for (int i = 0; i < smatchedsim.length; i++) {
            for (int j = i + 1; j < smatchedsim.length; j++) {
                if (i<location_size && j<location_size) {
                    if (smatchedsim[i] < smatchedsim[j]) {
                        Double temp1 = smatchedsim[i];
                        String temp2 = loc_matched[i];
                        String temp3 = matched_name[i];
                        String temp4 = matched_image[i];

                        smatchedsim[i] = smatchedsim[j];
                        loc_matched[i] = loc_matched[j];
                        matched_name[i] = matched_name[j];
                        matched_image[i] = matched_image[j];

                        smatchedsim[j] = temp1;
                        loc_matched[j] = temp2;
                        matched_name[j] = temp3;
                        matched_image[j] = temp4;
                    }
                }
            }
        }

    }

    //Menu (up right) tis selidas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);

        //1o menu item einai to profil tou opoiouou to icon theloume na einai a user pic or THE user's pic
        if (mAuth.getCurrentUser().getPhotoUrl() != null) {
            photo = mAuth.getCurrentUser().getPhotoUrl().toString();
            new DownLoadImageTask(menu.getItem(0), getApplicationContext()).execute(photo);
        }
        else
            menu.getItem(0).setIcon(R.drawable.ic_profile_black_24dp);
        return true;
    }


    //ti ginetai otan epilegetai kapoio menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //rythmiseis
            case R.id.settings:
                i = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                return true;
                //profile
            case R.id.profile:
                dialog = new Dialog2("Προφίλ", "profile", mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getPhoneNumber());
                dialog.show(getSupportFragmentManager(), "profile");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Spinner on Item Selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //0 position does nothing
        //1st position correspondes to LAKWNIA
        if (position==1)
        {

            if(lakwnia_name!= null){
                i = new Intent(getApplicationContext(),LocationsList.class);
                i.putExtra("where","spinner_lak");
                startActivity(i);
            }
        }
        //2nd position corresponds to MESSINIA
        else if (position==2)
        {
            if (messinia_name!= null) {
                i = new Intent(getApplicationContext(), LocationsList.class);
                i.putExtra("where", "spinner_mes");
                startActivity(i);
            }
        }
        //3rd position corresponds to ALL proorismous
        else if (position==3)
        {
            if (locations_name!= null && locations!= null){
                i=new Intent(getApplicationContext(),LocationsList.class);
                i.putExtra("where","spinner_all");
                startActivity(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //arrays used to get hot locations
    static Integer[] sum_ep,sume,sumeN;
    static String[] loc_hot, hot_name, hot_image;

    //method to find the hot locations list
    public void findHotLocations(){
        sume = new Integer[locations.size()];
        sumeN = new Integer[locations.size()];
        sum_ep = new Integer[locations.size()];
        loc_hot = new String[locations_name.size()];
        hot_name = new String[locations_name.size()];
        hot_image = new String[locations_name.size()];
        Arrays.fill(sume,0);
        Arrays.fill(sumeN,0);
        Arrays.fill(sum_ep,0);

        int x =0;
        for(String l : locations) {
            loc_hot[x] = l;
            x++;
        }
        x =0;
        for(String l : locations_name) {
            hot_name[x] = l;
            x++;
        }
        x =0;
        for(String l : locations_image) {
            hot_image[x] = l;
            x++;
        }

        //we find the sum of visitation for each location
        for(Integer[] e : allepiskepsimotita){
            for(int i=0;i<locations.size();i++){
                if (i<65) {
                    if (e[i] != 0) {
                        sume[i] += e[i];
                        sumeN[i]++;
                    }
                }
            }
        }

        //we find the mean value of the sum of visitation
        int y = 0;
        for(Integer e : sume){
            if(sumeN[y] != 0) //an
                sum_ep[y] = e / sumeN[y];
            else
                sum_ep[y] = 0;
            y++;
        }

        //basic for loops to sort more than one arrays according to one array's sorting
        for (int i=0; i<sume.length;i++) {
            for (int j = i + 1; j < sume.length; j++) {
                if (sume[i] < sume[j]) {//if(sum_ep[i] < sum_ep[j]){
                    Integer temp1 = sume[i];
                    String temp2 = loc_hot[i];
                    String temp3 = hot_name[i];
                    String temp4 = hot_image[i];


                    loc_hot[i] = loc_hot[j];
                    hot_name[i] = hot_name[j];
                    hot_image[i] = hot_image[j];
                    sume[i] = sume[j];


                    loc_hot[j] = temp2;
                    hot_name[j] = temp3;
                    hot_image[j] = temp4;
                    sume[j] = temp1;
                }
            }
        }
    }


    //arrays used to get top locations
    static Double[]  sum_top, sumr, sumn;
    static String[] loc_top, top_name, top_image;

    //method to find the top locations list
    public void findTopRated(){
        sumr = new Double[locations.size()];
        sumn = new Double[locations.size()];
        sum_top = new Double[locations_name.size()];
        loc_top = new String[locations_name.size()];
        top_name = new String[locations_name.size()];
        top_image = new String[locations_name.size()];
        Arrays.fill(sumr, 0.0);
        Arrays.fill(sumn, 0.0);
        Arrays.fill(sum_top, 0.0);

        int x =0;
        for (String l : locations) {
            loc_top[x] = l;
            x++;
        }
        x =0;
        for (String l : locations_name) {
            top_name[x] = l;
            x++;
        }
        x =0;
        for (String l : locations_image) {
            top_image[x] = l;
            x++;
        }

        //we get the sum of ratings for each location without zero ratings
        for(Integer[] r : allratings){
            for(int i=0;i<locations.size();i++){
                if (i <65) {
                    if (r[i] != 0) {
                        sumr[i] += r[i];
                        sumn[i]++;
                    }
                }
            }
        }

        //we get the mean value of ratings for each location
        int y = 0;
        for(Double r : sumr){ //gia oles tis vathomologies
            if(sumn[y] != 0.0) //an
                sum_top[y] = r / sumn[y]; //mesos oros
            else
                sum_top[y] = 0.0;
            y++;
        }

        //basic for loops to sort more than one arrays according to one array's sorting
        for (int i=0; i<sum_top.length;i++){
            for (int j=i+1;j<sum_top.length;j++){
                if(sum_top[i] < sum_top[j]){
                    Double temp1 = sum_top[i];
                    String temp2 = loc_top[i];
                    String temp3 = top_name[i];
                    String temp4 = top_image[i];

                    sum_top[i] = sum_top[j];
                    loc_top[i] = loc_top[j];
                    top_name[i] = top_name[j];
                    top_image[i] = top_image[j];

                    sum_top[j] = temp1;
                    loc_top[j] = temp2;
                    top_name[j] = temp3;
                    top_image[j] = temp4;
                }
            }
        }
    }


    //arrays used to sort the recommended arraylists
    static Double[] srecratings;
    static String[] loc_rec, rec_name, rec_image;

    //method to sort the recommended locations list
    public void sortRecommended(){
        srecratings = new Double[recommended.size()];
        srecratings = recommended.toArray(srecratings);

        loc_rec = new String[locations.size()];
        loc_rec = locations.toArray(loc_rec);

        rec_name = new String[locations.size()];
        rec_name = locations_name.toArray(rec_name);

        rec_image = new String[locations.size()];
        rec_image = locations_image.toArray(rec_image);

        //basic for loops to sort more than one arrays according to one array's sorting
        for (int i=0; i<srecratings.length;i++){
            for (int j=i+1;j<srecratings.length;j++){
                if(srecratings[i] < srecratings[j]){
                    Double temp1 = srecratings[i];
                    String temp2 = loc_rec[i];
                    String temp3 = rec_name[i];
                    String temp4 = rec_image[i];

                    srecratings[i] = srecratings[j];
                    loc_rec[i] = loc_rec[j];
                    rec_name[i] = rec_name[j];
                    rec_image[i] = rec_image[j];

                    srecratings[j] = temp1;
                    loc_rec[j] = temp2;
                    rec_name[j] = temp3;
                    rec_image[j] = temp4;
                }
            }
        }
    }

    //kathe fora pou i efarmogi epanerxetai (otan trexei sto background kai epanaferetai)
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart","");
        //content to opoio einai sti vash dedomenwn kai allazei poly spania
        getStaticContent();
        //metabliti pou orizei oti i home page einai i active selida, auti dld pou emfanizetai ston xristi
        active = true;
    }

    @Override
    public void onBackPressed() {
        //an exoume patisei mia fora na pame pisw apo tin arxiki selida tote einai pithano na theloume na ekselthoume autis
        if (!exit) {
            exit = true;
            Toast.makeText(getApplicationContext(), "Πατήστε ξάνα πίσω αν θέλετε να εξέλθετε της εφαρμογής.", Toast.LENGTH_SHORT).show();

            //dinetai ston xrin diastima 2000 ms na apofasisei an thelei na vgei apo tin efarmogh
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (exit)
                        exit = false;
                }
            }, 2000);
        } else {
            //an ehoume epileksei duo fores na pame pisw, tote emfanizetai mhnyma an eimaste sigouroi oti theloume na fygoume apo tin efarmogi
            exit = false;
            exitshow = true;
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Έξοδος")
                    .setMessage("Είστε σίγουρος/η;")
                    .setPositiveButton("Nαι", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            //System.exit(0);
                            finishAffinity();
                        }

                    })
                    .setNegativeButton("Οχι", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exitshow = false;
                        }
                    })
                    .show();
        }
    }

    //otan kaname rotate tin othoni kai prohgoumenos eihame epileksei mia apo tis 3 epiloges, LAKWNIA,MESSINIA,OLOUS
    //tote itan les kai o xristis to ksanapatouse kai emfanizotan i lista me tous antistoixous proorismous
    //gia na apofygoume na epilegetai position 1,2,3 otan o xristis den ehei patisei apo monos tou
    //otan, praktika, i efarmogi kanei RESUME, tote theloume i default timi na einai 0 stin opoia den ginetai kapoia energeia.
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","");
        spinner.setSelection(0);

        //an ehei idi ginei h do things, mhn tin kaneis ksana load (apofevgei tin ektelesh ths doThings dyo fores kata ti diadikasia RESUME
        if(executed) {
            executed=false;
            doThings();
        }

    }


    //ekteleitai akrivws meta tin on create kai kathe fora pou ginetai RESTART i efarmogi
    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","");
        //shmanei oti i ative selida einai i home page
        active = true;

        //an yparxei kapoio intent extra, ektos apo to welcome message, tote kleise ton dialogo, den to theloume
        if (getIntent() != null) {
            if (!getIntent().hasExtra("welcome_message"))
                if (dialog != null)
                    dialog.dismiss();
        }

    }

    //otan stamatisei i efarmogi, tote den yparxei kapoia active activity, kai tin epomenh fora pou tha boume kai tha patisoume to back
    //apo tin home page, tha mas vgalei mhnyma an theloume na ekselthoume tiss efarmogis
    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop","");
        active = false;
        exit = false;
    }
}