package com.example.trailer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity {
    GridView gridView;
    List<String> titles;
    List<String>links;
    NewAdapter adapter;
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.watchlater=0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<Movie1>list=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        gridView=findViewById(R.id.display);
        this.titles=MainActivity.titles;
        this.links=MainActivity.links;
        int i=0;
         for(String s:titles){
           list.add(new Movie1(s,links.get(i)));
           i++;
         }

        gridView = (GridView) findViewById(R.id.display);
        adapter = new NewAdapter(this, R.layout.image_adapter_view_layout,list);
        gridView.setAdapter(adapter);

    }

}