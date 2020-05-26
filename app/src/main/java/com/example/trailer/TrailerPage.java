package com.example.trailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrailerPage extends AppCompatActivity {
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_page);
         videoView =findViewById(R.id.video);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Location of Media File

        String name=getIntent().getStringExtra("Name");
        videoView.setMediaController(mediaController);
        getLink(name);
        //Starting VideView By Setting MediaController and URI


    }
    void getLink(String name){
        String table="Information/"+name+"/Trailer";

        DatabaseReference video_link = Movie_Database.getReference(table);
        video_link.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link=dataSnapshot.getValue(String.class);
                Uri uri=Uri.parse(link);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
