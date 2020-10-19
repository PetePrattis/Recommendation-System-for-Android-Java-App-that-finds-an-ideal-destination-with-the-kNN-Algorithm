package com.unipi.p15013p15120.kastropoliteiesv2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.Arrays;

public class LocationsList extends AppCompatActivity {

    //initializing object of HomePage class
    HomePage helper = new HomePage();

    GridView gridView;

    //arrays which we pass at GridAdapter
    String[] nomoi;
    int[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations_list);

        gridView = findViewById(R.id.gridview);

        //we check for Intent to see which list of locations we need to pass to GridAdapter
        if (getIntent() != null) {
            if (getIntent().hasExtra("where")) {
                if (getIntent().getStringExtra("where").equals("top_rated")) {//we need to show the top rated locations list
                    getSupportActionBar().setSubtitle("Top Προορισμοί");

                    //we will only show the top 10 top rated locations
                    nomoi = new String[10];
                    int x =0;
                    for(String l : helper.loc_top) {//we determine the nomos of each location
                        if (x != nomoi.length) {
                            if (l.contains("LAK"))
                                nomoi[x] = "Λακωνία";
                            else if (l.contains("MES"))
                                nomoi[x] = "Μεσσηνία";
                            x++;
                        }
                        else
                            break;
                    }

                    images = new int[10];
                    Arrays.fill(images, 0);
                    x =0;
                    for(String l : helper.top_image) {//we determine the name of the drawable image for each location
                        if (x != images.length)
                        {
                            images[x] = getResources().getIdentifier(l, "drawable", getPackageName());
                            x++;
                        }
                        else
                            break;
                    }

                    //we call GridAdapter constructor passing the context, the counties, the names and the image names of the locations and a string 'images'
                    //the string 'images' means that the GridAdapter will set the grid view with images
                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, Arrays.copyOfRange(helper.top_name,0,10), images, "images");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("hot_locations")) {//we need to show the hot locations list
                    getSupportActionBar().setSubtitle("Hot Προορισμοί");

                    //we work the same way

                    nomoi = new String[10];
                    int x = 0;
                    for (String l : helper.loc_hot) {
                        if (x != nomoi.length)
                        {
                            if (l.contains("LAK"))
                                nomoi[x] = "Λακωνία";
                            else if (l.contains("MES"))
                                nomoi[x] = "Μεσσηνία";
                            x++;
                        }
                        else break;

                    }

                    images = new int[10];
                    Arrays.fill(images, 0);
                    x = 0;
                    for (String l : helper.hot_image) {
                        if (x != images.length)
                        {
                            images[x] = getResources().getIdentifier(l, "drawable", getPackageName());
                            x++;
                        }
                        else break;
                    }


                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, Arrays.copyOfRange(helper.hot_name,0,10), images, "images");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("recommended")) {//we need to show the recommended locations list
                    getSupportActionBar().setSubtitle("Προτεινόμενα");

                    //we work the same way
                    nomoi = new String[10];
                    int x = 0;
                    for (String l : helper.loc_rec) {
                        if (x != nomoi.length) {
                            if (l.contains("LAK"))
                                nomoi[x] = "Λακωνία";
                            else if (l.contains("MES"))
                                nomoi[x] = "Μεσσηνία";
                            x++;
                        }
                        else break;
                    }

                    images = new int[10];
                    Arrays.fill(images, 0);
                    x = 0;
                    for (String l : helper.rec_image) {
                        if (x != images.length) {
                            images[x] = getResources().getIdentifier(l, "drawable", getPackageName());
                            x++;
                        }
                        else break;
                    }

                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, Arrays.copyOfRange(helper.rec_name,0,10), images, "images");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("user_ratings")) {//we need to show the rated by the user locations list
                    getSupportActionBar().setSubtitle("Βαθμολογίες Χρήστη");

                    //now we pass all the locations of the rated by the user locations list
                    nomoi = new String[helper.user_history_locations_id.size()];
                    int x = 0;
                    for (String l : helper.user_history_locations_id) {
                        if (l.contains("LAK"))
                            nomoi[x] = "Λακωνία";
                        else if (l.contains("MES"))
                            nomoi[x] = "Μεσσηνία";
                        x++;
                    }

                    //the string 'ratings' means that the GridAdapter will set the grid view with rating bar
                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, helper.user_history_locations_name.toArray(new String[0]), helper.user_history_ratings.toArray(new Integer[0]), "ratings");
                    gridView.setAdapter(gridAdapter);

                }
                else if (getIntent().getStringExtra("where").equals("matched")) {//we need to show the matched locations list
                    getSupportActionBar().setSubtitle("Matched Προορισμοί");

                    //we work the same way as previously
                    nomoi = new String[10];
                    int x = 0;
                    for (String l : helper.loc_matched) {
                        if (x !=nomoi.length) {
                            if (l.contains("LAK"))
                                nomoi[x] = "Λακωνία";
                            else if (l.contains("MES"))
                                nomoi[x] = "Μεσσηνία";
                            x++;
                        }
                        else break;
                    }

                    images = new int[10];
                    Arrays.fill(images, 0);
                    x = 0;
                    for (String l : helper.matched_image) {
                        if (x!=images.length) {
                            images[x] = getResources().getIdentifier(l, "drawable", getPackageName());
                            x++;
                        }
                        else break;
                    }

                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, Arrays.copyOfRange(helper.matched_name, 0, 10) , images, "images");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("spinner_lak")) {//we need to show the list of locations that are in Lakonia county
                    getSupportActionBar().setSubtitle("Λακωνία");

                    //the string 'noimage' means that the GridAdapter will set the grid view without image
                    GridAdapter gridAdapter = new GridAdapter(this, helper.lakwnia_name.toArray(new String[0]), "noimage");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("spinner_mes")) {//we need to show the list of locations that are in Messinia county
                    getSupportActionBar().setSubtitle("Μεσσηνία");

                    GridAdapter gridAdapter = new GridAdapter(this, helper.messinia_name.toArray(new String[0]), "noimage");
                    gridView.setAdapter(gridAdapter);
                }
                else if (getIntent().getStringExtra("where").equals("spinner_all")){//we need to show all locations list
                    getSupportActionBar().setSubtitle("Όλοι οι προορισμοί");

                    nomoi = new String[helper.locations.size()];
                    //we determine the county of each location
                    int x = 0;
                    for (String l : helper.locations) {
                        if (l.contains("LAK"))
                            nomoi[x] = "Λακωνία";
                        else if (l.contains("MES"))
                            nomoi[x] = "Μεσσηνία";
                        x++;
                    }

                    GridAdapter gridAdapter = new GridAdapter(this, nomoi, helper.locations_name.toArray(new String[0]),"noimage");
                    gridView.setAdapter(gridAdapter);
                }
            }
        }

        //this is a listener which is called when an item of the grid view is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//we can know which item is clicked by its position
                Intent i = new Intent(getApplicationContext(), LocationPage.class);//initialize intent for LocationPage class
                if (getIntent() != null) {//we check the intent 'where' in order to know in which list to search for the specific location
                    if (getIntent().hasExtra("where")) {
                        if (getIntent().getStringExtra("where").equals("top_rated")) {//if intent 'where' is 'top_rated'
                            //we put an extra variable at the intent for the class which are the id and the name of the specific location
                            //by using the HomePage object helper and the specific location's arrays which the 'where' intent specifies
                            i.putExtra("whereID", helper.loc_top[position]);
                            i.putExtra("whereName", helper.top_name[position]);
                            startActivity(i);
                        }
                        else if(getIntent().getStringExtra("where").equals("hot_locations")){//if intent 'where' is 'hot_locations'
                            //we work the same way as before
                            i.putExtra("whereID", helper.loc_hot[position]);
                            i.putExtra("whereName", helper.hot_name[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("recommended")) {//if intent 'where' is 'recommended'
                            //we work the same way as before
                            i.putExtra("whereID", helper.loc_rec[position]);
                            i.putExtra("whereName", helper.rec_name[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("matched")) {//if intent 'where' is 'matched'
                            //we work the same way as before
                            i.putExtra("whereID", helper.loc_matched[position]);
                            i.putExtra("whereName", helper.matched_name[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("spinner_lak")) {//if intent 'where' is 'spinner_lak'
                            //we work the same way as before only this time we work with location's arraylist
                            i.putExtra("whereID", helper.lakwnia.toArray(new String[0])[position]);
                            i.putExtra("whereName", helper.lakwnia_name.toArray(new String[0])[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("spinner_mes")) {//if intent 'where' is 'spinner_mes'
                            //we work the same way as before
                            i.putExtra("whereID", helper.messinia.toArray(new String[0])[position]);
                            i.putExtra("whereName", helper.messinia_name.toArray(new String[0])[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("spinner_all")) {//if intent 'where' is 'spinner_all'
                            //we work the same way as before
                            i.putExtra("whereID", helper.locations.toArray(new String[0])[position]);
                            i.putExtra("whereName", helper.locations_name.toArray(new String[0])[position]);
                            startActivity(i);
                        }
                        else if (getIntent().getStringExtra("where").equals("user_ratings")){//if intent 'where' is 'user_ratings'
                            //we work the same way as before
                            i.putExtra("whereID", helper.user_history_locations_id.toArray(new String[0])[position]);
                            i.putExtra("whereName", helper.user_history_locations_name.toArray(new String[0])[position]);
                            startActivity(i);
                        }
                    }
                }
            }
        });


    }
}

