package com.example.trailer;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TopRatedMovieActivity extends AppCompatActivity {

    GridView gridView;
    NewAdapter adapter;
    ArrayList<Movie1> movieList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_rated_activity_layout);

        movieList = (ArrayList<Movie1>) getIntent().getSerializableExtra("movieList");

        gridView = (GridView) findViewById(R.id.movieGridView);
        adapter = new NewAdapter(this, R.layout.image_adapter_view_layout, movieList);
        gridView.setAdapter(adapter);
    }
}


