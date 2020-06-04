package com.example.trailer;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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





    DatabaseReference userReference;
    DataBaseHelper db;
    DrawerLayout drawer;
    String username;
    ArrayList<String> usernameList;
    ArrayList<String> emailList;
    ArrayList<Float> ratingList = new ArrayList<>();
    ArrayList<Movie> movieList;
    GridView gridView;
    NewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DataBaseHelper(this);
        returningUser();

        //store all movies from firebase in movie array list
        getMovieData();


        ImageView image = null;
        //This is used to load pictures

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        final Intent intent =new Intent(this,TrailerPage.class);

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

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.topRated) {
            displayTopRated();

        } else if (id == R.id.genres) {
            displayGenres();

        } else if (id == R.id.login) {
            login();

        }


        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void login()
    {
        //storing list of usernames so that we can check in RegisterActivity whether username already in use or not
        usernameList = new ArrayList<>();
        emailList = new ArrayList<>();

        userReference = FirebaseDatabase.getInstance().getReference().child("Account");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot obj: dataSnapshot.getChildren())
                    {
                        String usernameFromDB = obj.getKey().toString();
                        String emailFromDB = obj.child("email").getValue().toString();

                        usernameList.add(usernameFromDB);
                        emailList.add(emailFromDB);
                    }

                }

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putStringArrayListExtra("usernameList", usernameList);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void displayTopRated()
    {

        Collections.sort(movieList);
        int num = 5;

        if(ratingList != null)
        {
            ratingList.clear();
        }

        if(links != null)
        {
            links.clear();
        }

        if(titles != null)
        {
            titles.clear();
        }


        for(Movie m: movieList)
        {
            ratingList.add(m.getRating());
            links.add(m.getImageLink());
            titles.add(m.getTitle());
        }

        Intent intent = new Intent(MainActivity.this, TopRatedMovieActivity.class);
        intent.putExtra("movieList", movieList);
        startActivity(intent);

//        Adapter adapter=new Adapter(titles,links,MainActivity.this);
//        list.setAdapter(adapter);

    }

    private void displayGenres()
    {
        Intent intent = new Intent(MainActivity.this, GenreActivity.class);
        intent.putExtra("movieList", movieList);
        startActivity(intent);
    }

    private void returningUser()
    {
        Cursor cursor = db.getData();
        if(cursor.getCount() != 0)
        {
            boolean userFound = false;

            //finding out which user had logged in before and logging them into account
            while(cursor.moveToNext() && userFound == false)
            {
                if(cursor.getInt(1) == 1)
                {
                    username = cursor.getString(0);
                    userFound = true;
                }
            }

            if(userFound == true)
            {
                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }

        }
    }


    private void getMovieData()
    {
        movieList = new ArrayList<>();
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

                        Movie m = new Movie(title, imageLink, trailerLink, rating, genreList);
                        movieList.add(m);

                    }

                    gridView = (GridView) findViewById(R.id.movieGridView);
                    adapter = new NewAdapter(MainActivity.this, R.layout.image_adapter_view_layout, movieList);
                    gridView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
