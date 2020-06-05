package com.example.trailer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewAdapter extends ArrayAdapter<Movie> {
    List<String>added=new ArrayList<>();
    List<String>titles;
    List<String>links;

    ArrayList<Movie1> movieList;

    static Intent intent;
    Context myContext;

    int myResource;


    public NewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Movie1> objects) {
        super(context, resource);
        this.myContext = context;
        this.myResource = resource;

        //storing values in students so they can be retrieved in update function
        this.movieList = new ArrayList<>();
        this.movieList.addAll(objects);

    }


    public int getCount() {
        return movieList.size();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View myview = view;
        if (myview == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myview = inflater.inflate(R.layout.image_adapter_view_layout, null);
        }

        ImageView movieImageView;
        TextView titleTextView ;
        TextView ratingTextView;

        movieImageView = myview.findViewById(R.id.movieImageView);
        titleTextView = myview.findViewById(R.id.titleTextView);
        //ratingTextView = myview.findViewById(R.id.ratingTextView);


        String imageLink = movieList.get(i).getImageLink();
        String title = movieList.get(i).getTitle();
        float rating = movieList.get(i).getRating();

        titleTextView.setText(title);
        //ratingTextView.setText(Float.toString(rating));


        Picasso.get().load(imageLink).resize(120, 180).into(movieImageView);


        final Intent intent=new Intent(myContext, TrailerPage.class);

        movieImageView.setTag(title);

        intent.putExtra("Name",(String) movieImageView.getTag());
        movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myContext.startActivity(intent);
            }
        });

        return myview;
    }


}
