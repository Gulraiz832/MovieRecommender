package com.example.trailer;

import java.util.List;

public class Movie_title {
    String title;
    public List<String> Genre;
    public float Rating;
    public String Trailer;
    public  Movie_title(String title,List<String> Genre,float Rat,String Trailer){
        this.title=title;
        this.Genre=Genre;
        Rating=Rat;
        this.Trailer=Trailer;
    }
    public  Movie_title(String title,Movie m){
        this.title=title;
        this.Genre=m.Genre;
        Rating=m.Rating;
        this.Trailer=m.Trailer;
    }
}
