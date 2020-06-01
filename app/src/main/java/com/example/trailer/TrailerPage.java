package com.example.trailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrailerPage extends AppCompatActivity {
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    PlayerView videoView;
    SimpleExoPlayer exoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_page);
         videoView =findViewById(R.id.video);
         exoPlayer= ExoPlayerFactory.newSimpleInstance(this);
         videoView.setPlayer(exoPlayer);

       // MediaController mediaController= new MediaController(this);
        //mediaController.setAnchorView(videoView);


        String name=getIntent().getStringExtra("Name");
      //  videoView.setMediaController(mediaController);
        getLink(name);



    }
    void getLink(String name){
        String table="Information/"+name+"/Trailer";

        DatabaseReference video_link = Movie_Database.getReference(table);
        video_link.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link=dataSnapshot.getValue(String.class);
                Uri uri=Uri.parse(link);
                if(uri!=null){
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(TrailerPage.this, "exoplayer-codelab");
                    MediaSource mediaSource =  new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                    exoPlayer.setPlayWhenReady(playWhenReady);
                    exoPlayer.seekTo(currentWindow, playbackPosition);
                    exoPlayer.prepare(mediaSource, false, false);
                }
                else
                    Toast.makeText(TrailerPage.this,"Server is Busy",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
