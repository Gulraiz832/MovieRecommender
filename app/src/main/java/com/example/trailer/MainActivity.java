package com.example.trailer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.snackbar.Snackbar;

import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    static List<String> titles=new ArrayList<>();
static List<String>links=new ArrayList<>();
private FirebaseAnalytics mFirebaseAnalytics;

final int LAUNCH_LOG_ACTIVITY=1;
DatabaseReference userReference;
DataBaseHelper db;
DrawerLayout drawer;
String username;
ArrayList<Float> ratingList = new ArrayList<>();
ArrayList<String> usernameList;
ArrayList<String> emailList;
ArrayList<Movie1> movieList;
GridView gridView;
NewAdapter adapter;
String logged_user="gulraiz";
private DrawerLayout drawerLayout;
private ActionBarDrawerToggle actionBarDrawerToggle;
static int watchlater=0;
static Context context;
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

    static boolean  active=false;
ListView list;
int flag=0;
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    DatabaseReference title_ref = Movie_Database.getReference("Title");
    DatabaseReference link_ref = Movie_Database.getReference("Link");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context=this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new Broadcast(), intentFilter);
        //waleed
        db = new DataBaseHelper(this);

        //store all movies from firebase in movie array list
        getMovieData();
        //waleed



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
        returningUser();


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
       // getMenuInflater().inflate(R.menu.main, menu);
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


        }
        if(id==R.id.watch_later_activity){

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(LoginActivity.state==1) {
            logged_user=LoginActivity.global_username;
            if (id == R.id.Fav_drawer) {

                startDisplay("Fav");
            } else if (id == R.id.watch_later_drawer) {
                startDisplay("WatchLater");
            } else if (id == R.id.login) {

                    Logout();
                TextView drawerTitleTextView =findViewById(R.id.titleTextView);
                TextView drawerSubTitleTextView = findViewById(R.id.subTitleTextView);

                drawerTitleTextView.setText("Not LogIn");
                drawerSubTitleTextView.setText(LoginActivity.global_username);
                NavigationMenuItemView menuItem=findViewById(R.id.login);
                menuItem.setTitle("LogIn");
                Toast.makeText(this,"LogOut Successfully!",Toast.LENGTH_SHORT).show();
                    LoginActivity.state=0;



            } else if (id == R.id.Recomendation_drawer)
                startDisplay("Recommendation");
            else if (id == R.id.topRated) {
                displayTopRated();

            }
            else if (id == R.id.genres) {
                displayGenres();

            }

        }
        else if (id == R.id.topRated) {
            displayTopRated();

        }
        else if (id == R.id.genres) {
            displayGenres();

        }
        else if (id == R.id.login) {
            if(item.getTitle().toString().contains("Login")) {
                login();
                Toast.makeText(this,"LogIn Successfully!",Toast.LENGTH_SHORT).show();
            }
            else {
                Logout();
                LoginActivity.state=0;
            }

        }
        else
            Toast.makeText(this,"You are not currently logged in!!",Toast.LENGTH_LONG).show();



        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




  public void startDisplay(String s) {

      DatabaseReference databaseReference=Movie_Database.getReference().child("UserInfo").child(logged_user).child(s);
      databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              HashMap<String,String>map=new HashMap<>();
              int i=0;
              for(String s:titles){
                  map.put(s,links.get(i));
                  i++;
              }
              links.clear();
              titles.clear();
              HashMap<String,String>like;
              like=(HashMap<String, String>) dataSnapshot.getValue();
              if(like!=null){
                  titles.addAll(like.values());
                  for(String s:titles){
                      links.add(map.get(s));

                  }


              }
              Intent intent=new Intent(MainActivity.this,Display.class);
              startActivity(intent);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
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
                startActivityForResult(intent,LAUNCH_LOG_ACTIVITY);
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


        for(Movie1 m: movieList)
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
                LoginActivity.state=1;
                LoginActivity.global_username=username;
                changeText();
            }

        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2){
         changeText();

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
                        titles.add(title);
                        String imageLink = obj.child("TitleImage").getValue().toString();
                        links.add(imageLink);
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
                        movieList.add(m);
                        genreList.clear();

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
    @SuppressLint("RestrictedApi")
    void changeText(){
        TextView drawerTitleTextView =findViewById(R.id.titleTextView);
        TextView drawerSubTitleTextView = findViewById(R.id.subTitleTextView);

        drawerTitleTextView.setText("Welcome!");
        drawerSubTitleTextView.setText(LoginActivity.global_username);
        NavigationMenuItemView menuItem=findViewById(R.id.login);
        menuItem.setTitle("Logout");
    }
}
