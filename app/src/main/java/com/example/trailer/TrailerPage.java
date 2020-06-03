package com.example.trailer;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrailerPage extends AppCompatActivity {
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    final String username="gulraiz";
    ImageView image;
    ImageView image2;
    boolean fav_available=false;
    boolean wlater_available=true;
    HashMap<String,String>watchlater;

    int remove=0;
    //PlayerView videoView;
    //SimpleExoPlayer exoPlayer;
    VideoView videoView;
    static Context trailercontext;
    static String title;
    private AdView mAdView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
   static boolean active=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trailercontext=this;
        setContentView(R.layout.activity_trailer_page);
         videoView =findViewById(R.id.video);
        // exoPlayer= ExoPlayerFactory.newSimpleInstance(this);
         //videoView.setPlayer(exoPlayer);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MediaController mediaController= new MediaController(this);
        mediaController.setPadding(0,0,0,950);
        mediaController.setAnchorView(videoView);
          image=findViewById(R.id.favt);
          image2=findViewById(R.id.watch_lat);
        String name=getIntent().getStringExtra("Name");
        title=name;
        getFavandWatchLater();
       videoView.setMediaController(mediaController);
        getLink(name);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }
    void getFavandWatchLater(){

         DatabaseReference databaseReference=Movie_Database.getReference("UserInfo").child(username).child("Fav");
         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String>favs;
                favs=(HashMap<String, String>) dataSnapshot.getValue();
               fav_available= favs.containsKey(title);
                if(fav_available)
                    image.setBackgroundResource(R.drawable.fav_icon_fill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference1=Movie_Database.getReference("UserInfo").child(username).child("WatchLater");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String>favs;
                favs=(HashMap<String, String>) dataSnapshot.getValue();
                wlater_available= favs.containsKey(title);
                if(wlater_available)
                    image2.setBackgroundResource(R.drawable.watch_later);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onStop() {
        active=false;
        super.onStop();
    }

    @Override
    protected void onStart() {
        active=true;
        super.onStart();
    }
    void getLink(String name){
       // String table="Information/"+name+"/Trailer";
        String table="Information/"+name;

        DatabaseReference video_link = Movie_Database.getReference(table);
        video_link.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Movie movie=dataSnapshot.getValue(Movie.class);
                Uri uri=Uri.parse(movie.Trailer);
                if(uri!=null){
                    RatingBar ratingBar=findViewById(R.id.ratingBar);
                    TextView text=findViewById(R.id.rate);
                    TextView text1=findViewById(R.id.genre);
                    String genre="";
                    for(String s:movie.Genre){
                        genre=genre+s;
                    }
                    text1.setText(genre);
                    ratingBar.setRating(movie.Rating);
                    String rat=Float.toString(movie.Rating)+"/10";
                    text.setText(rat);
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();

                }
                else
                    Toast.makeText(TrailerPage.this,"Server is Busy",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    void getAdditional(String name){
        String table="Information/"+name;

        DatabaseReference information = Movie_Database.getReference(table);
        information.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, List<String>> data=new HashMap<>();
                data=(HashMap<String, List<String>>) dataSnapshot.getValue();
                RatingBar ratingBar=findViewById(R.id.ratingBar);
                TextView text=findViewById(R.id.rate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void addToFav(View view) {
        boolean login=true;// to be in login class to check either user is loged in or not



        if(login){
            String table="UserInfo";
            final DatabaseReference user_info= Movie_Database.getReference(table).child(username).child("Fav");
            if(fav_available)
            {
                user_info.child(title).setValue(null);
                fav_available=!fav_available;
                image.setBackgroundResource(R.drawable.fav_icon_empty);
                Toast.makeText(this,"Removed From Liked Videos",Toast.LENGTH_LONG).show();
            }

            else
            {

                user_info.child(title).setValue("True");
                fav_available=true;
                image.setBackgroundResource(R.drawable.fav_icon_fill);
                Toast.makeText(this,"Added From Liked Videos",Toast.LENGTH_LONG).show();
            }







        }
    }
    public void addToLater(View view) {
        boolean login=true;// to be in login class to check either user is loged in or not



        if(login){
            String table="UserInfo";
            final DatabaseReference user_info= Movie_Database.getReference(table).child(username).child("WatchLater");
            if(wlater_available)
            {
                user_info.child(title).setValue(null);
                wlater_available=!wlater_available;
                image2.setBackgroundResource(R.drawable.watch_later1);
                Toast.makeText(this,"Removed From Watch Later",Toast.LENGTH_LONG).show();
            }

            else
            {

                user_info.child(title).setValue("True");
                wlater_available=true;
                image2.setBackgroundResource(R.drawable.watch_later);
                Toast.makeText(this,"Added to Watch Later",Toast.LENGTH_LONG).show();
            }







        }
    }
}
