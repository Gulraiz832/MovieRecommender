package com.example.trailer;

import java.util.List;

public class Movie {
    public List<String> Genre;
    public float Rating;
    public String Trailer;
   public  Movie(List<String> Genre,float Rat,String Trailer){
        this.Genre=Genre;
        Rating=Rat;
        this.Trailer=Trailer;
    }

    public Movie(){}
}
