package com.example.trailer;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie1 implements Serializable, Comparable<Movie1> {
    private String title;
    private String imageLink="";
    private String trailerLink="";
    private float rating=0;
    ArrayList<String> genreList;

    public Movie1(String title, String imageLink, String trailerLink, float rating, ArrayList<String> genreList) {
        this.title = title;
        this.imageLink = imageLink;
        this.trailerLink = trailerLink;
        this.rating = rating;
        this.genreList = genreList;
    }
    public Movie1(String title, String imageLink) {
        this.title = title;
        this.imageLink = imageLink;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<String> genreList) {
        this.genreList = genreList;
    }

    @Override
    public int compareTo(Movie1 o) {

        if(rating < o.rating)
        {
            return 1;
        }
        else if(rating > o.rating)
        {
            return -1;
        }
        else
        {
            return 0;
        }

    }
}
