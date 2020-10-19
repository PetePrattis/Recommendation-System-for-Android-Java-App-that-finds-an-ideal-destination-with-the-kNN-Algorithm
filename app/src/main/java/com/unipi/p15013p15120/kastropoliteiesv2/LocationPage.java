package com.unipi.p15013p15120.kastropoliteiesv2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LocationPage extends AppCompatActivity {

    HomePage helper = new HomePage();
    LoginSignUpPage helper2 = new LoginSignUpPage();
    Dialog2 dialog;

    Intent i ;

    ValueEventListener listener;
    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;

    public static String whereID,whereName;
    int image;
    public static Double lat,lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topothesia_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //content
        final TextView rating_stars = findViewById(R.id.rating_stars);
        final ImageView img = findViewById(R.id.header);
        TextView nomos = findViewById(R.id.nomos);
        TextView location_name = findViewById(R.id.location_name);
        final ImageButton help = findViewById(R.id.help);
        ImageButton map = findViewById(R.id.map);
        final TextView synopsis = findViewById(R.id.synopsis);
        final TextView thalassa = findViewById(R.id.thalassa);
        final TextView vouno = findViewById(R.id.vouno);
        final TextView pediada = findViewById(R.id.pediada);
        final TextView popularity = findViewById(R.id.popularity);
        final TextView history = findViewById(R.id.history);
        final TextView attraction = findViewById(R.id.attraction);
        final TextView significant_people = findViewById(R.id.significant_people);
        final TextView night_life = findViewById(R.id.night_life);
        final TextView alternative_tourism = findViewById(R.id.alternative_tourism);
        final TextView stay = findViewById(R.id.stay);
        final TextView wildlife = findViewById(R.id.wildlife);
        final TextView climate = findViewById(R.id.climate);
        final TextView attraction_header = findViewById(R.id.textView6);
        final TextView name_attr = findViewById(R.id.attraction_name);
        final TextView altour_header = findViewById(R.id.textView7);
        final TextView type_alt_tour = findViewById(R.id.type_alternative_tourism);
        final TextView capital_greece_dist = findViewById(R.id.capital_greece_dist);
        TextView capital_nomou_name = findViewById(R.id.capital_nomou_name);
        final TextView capital_nomou_dist = findViewById(R.id.capital_nomou_dist);


        //poio einai to id tis topothesias kai poio to onoma
        if (getIntent()!= null) {
            if (getIntent().hasExtra("whereID"))
                whereID = getIntent().getStringExtra("whereID"); //id
            if (getIntent().hasExtra("whereName"))
                whereName = getIntent().getStringExtra("whereName");
        }

        //to parakatw snippet of code, einai gia na emfanizetai o titlos tis topothesias, MONO otan ginetai scroll down opote
        //kanei collapse to parapanw bar !!
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitle(whereName);
                    isShow = true;
                } else if(isShow) {
                    toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        //mesos oros asteriwn tis topothesias
        for (int i=0; i<helper.loc_top.length;i++){
            if(whereID.equals(helper.loc_top[i]))
                rating_stars.setText(String.format("%.2f",helper.sum_top[i])+"/5");
        }

        //onomasia
        location_name.setText(whereName);

        //nomos
        if (whereID.contains("LAK")) {
            nomos.setText("Λακωνία");
            capital_nomou_name.setText("Σπάρτη");
        }
        else if (whereID.contains("MES")){
            nomos.setText("Μεσσηνία");
            capital_nomou_name.setText("Καλαμάτα");
        }


        FloatingActionButton rate = (FloatingActionButton) findViewById(R.id.rate);

        //aksiologisi en logw topothesias
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog2("Αξιολόγηση τοποθεσίας","rate",whereID);
                dialog.show(getSupportFragmentManager(),"rate");
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //anoigma xarti kai orismos latitude and lontitude
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lat != null && lon != null) {
                    i = new Intent(getApplicationContext(), LocationMap.class);
                    i.putExtra("locname", whereName);
                    startActivity(i);
                }
                else
                    helper2.makeToast(getApplicationContext(),getResources().getString(R.string.wait_txt));


            }
        });

        //help gia ta xrwmara pou emfanizontai
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog2("Βοήθεια","about",getResources().getString(R.string.help_location_page));
                dialog.show(getSupportFragmentManager(),"about");
            }
        });


        //listener pou load olo to content tis selidas ara tis topothesias pou provaletai
        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.child("topothesies").getChildren()) {
                        Topothesia topothesia = ds.getValue(Topothesia.class);
                        if (String.valueOf(ds.getKey()).equals(whereID)) {
                            //an yparxei photo tote emfanise tin alliws vale NO IMAGE
                            if (topothesia.getImage()!= null)
                                image = getResources().getIdentifier(topothesia.getImage() , "drawable", getPackageName());
                            else
                                image = R.drawable.noimage;

                            lat = topothesia.getLatitude();
                            lon = topothesia.getLongitude();

                            //PHOTO
                            img.setImageResource(image);
                            //SYNOPSI
                            synopsis.setText(topothesia.getSynopsis());

                            if (topothesia.getSeaside() == 0)
                                thalassa.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getMountain() == 0)
                                vouno.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getFlat() == 0)
                                pediada.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getPopularity() == 0)
                                popularity.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getHistory() == 0)
                                history.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getAttraction() == 0)
                                attraction.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getSignificant_people() == 0)
                                significant_people.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getNight_life() == 0)
                                night_life.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getAlternative_tourism() == 0)
                                alternative_tourism.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getStay() == 0)
                                stay.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (topothesia.getWildlife() == 0)
                                wildlife.setBackgroundResource(R.drawable.roundedfull_decline);

                            climate.setText(topothesia.getClimate());

                            //an yparxei aksiotheato, tote emfanise extra info sxetika me auto
                            if (topothesia.getAttraction() == 1) {
                                attraction_header.setVisibility(View.VISIBLE);
                                name_attr.setVisibility(View.VISIBLE);
                                name_attr.setText(topothesia.getAttraction_name());
                            }

                            //an i topothesia diatheie enallaktiko tourismo tote emfanise ton typo
                            if (topothesia.getAlternative_tourism() == 1){
                                altour_header.setVisibility(View.VISIBLE);
                                type_alt_tour.setVisibility(View.VISIBLE);
                                type_alt_tour.setText(topothesia.getType_alternative_tourism());}

                            capital_greece_dist.setText(topothesia.getDistance_athens()+" χλμ.");
                            capital_nomou_dist.setText(topothesia.getDistance_kalamata()+" χλμ.");

                        }
                    }


                    //auksanoume episkepsimotita sti sugkekrimeni topothesia. auto praktika ginetai me to pou kanei launch i scrollingActivity
                    for (DataSnapshot ds2 : dataSnapshot.child("ratings").child(mAuth.getCurrentUser().getUid()).getChildren())
                    {
                        try {
                            if (ds2.child("topothesies").getValue().toString().equals(whereID)) {
                                UserRatings user = ds2.getValue(UserRatings.class);
                                Integer ep = user.getEpiskepsimotita();
                                ep++;
                                myRef.child("ratings").child(mAuth.getCurrentUser().getUid()).child(ds2.getKey()).child("episkepsimotita").setValue(ep);

                            }
                        }
                        catch (NullPointerException e){
                            Log.w("LocationPage",e.getMessage());
                        }
                    }
                myRef.removeEventListener(listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
