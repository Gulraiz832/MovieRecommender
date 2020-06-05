package com.example.trailer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoggedInActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<String> titles=new ArrayList<>();
    List<String>links=new ArrayList<>();
    ListView list;
    int flag=0;
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    DatabaseReference title_ref = Movie_Database.getReference("Title");
    DatabaseReference link_ref = Movie_Database.getReference("Link");

    //for drawer layout
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    NavigationView navigationView;

    DataBaseHelper db;
    DrawerLayout drawer;
    static String username;
    TextView drawerTitleTextView;
    TextView drawerSubTitleTextView;

    ArrayList<Movie1> movies;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_activity_layout);

        getMovieData();

        username = getIntent().getStringExtra("username");
        db = new DataBaseHelper(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        drawerTitleTextView = (TextView) headerView.findViewById(R.id.titleTextView);
        drawerSubTitleTextView = (TextView) headerView.findViewById(R.id.subTitleTextView);

        drawerTitleTextView.setText("Welcome!");
        drawerSubTitleTextView.setText(username);


        ImageView image=null;
        //This is used to load pictures

        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }



    static void start(Intent intent){

    }


    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.Logout) {
            Logout();
        }


        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout()
    {
        Cursor cursor = db.getData();

        while(cursor.moveToNext())
        {
            if(cursor.getString(0).equals(username))
            {
                db.updateData(username, 0);

            }
        }
    }

    private void getMovieData()
    {
        movies = new ArrayList<>();
        final ArrayList<String> genreList = new ArrayList<>();

        userReference = FirebaseDatabase.getInstance().getReference().child("Information");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot obj: dataSnapshot.getChildren())
                    {
                        String title = obj.getKey().toString();
                        String imageLink = obj.child("TitleImage").getValue().toString();
                        String trailerLink = obj.child("Trailer").getValue().toString();
                        float rating = Float.valueOf(obj.child("Rating").getValue().toString());

                        for(DataSnapshot genre: obj.child("Genre").getChildren())
                        {
                            //since we don't want value of specific attribute
                            //we can just use .getValue and it will get the first value, then the next
                            //and so on
                            genreList.add(genre.getValue().toString());
                        }

                        Movie1 m = new Movie1(title, imageLink, trailerLink, rating, genreList);
                        movies.add(m);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
