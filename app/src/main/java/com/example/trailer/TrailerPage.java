package com.example.trailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class TrailerPage extends AppCompatActivity {
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
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

        String name=getIntent().getStringExtra("Name");
        title=name;
       videoView.setMediaController(mediaController);
        getLink(name);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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

}
