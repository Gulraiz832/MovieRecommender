package com.example.trailer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Display extends AppCompatActivity {
     ListView listView;
     List<String> titles;
     List<String>links;
    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.watchlater=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        listView=findViewById(R.id.display);
        this.titles=MainActivity.titles;
        this.links=MainActivity.links;
        Adapter adapter=new Adapter(titles,links,this);
        listView.setAdapter(adapter);

    }

}
