package com.example.trailer;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Recomendation {
    String username="gulraiz";
    String Title;
    Movie movie;
    List<String> movies=new ArrayList<>();

    FirebaseDatabase Movie_Database = FirebaseDatabase.getInstance();
    Recomendation(Movie m,String title){
        movie=m;
        Title=title;
        addRecomendations();

    }
    void addRecomendations(){
        final List<String> ge=movie.Genre;
        DatabaseReference databaseReference=Movie_Database.getReference().child("Information");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference databaseReference1=Movie_Database.getReference().child("UserInfo").child(username).child("Recommendation");
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String key=data.getKey();
                    Movie movie=data.getValue(Movie.class);
                    if(movie!=null) {
                        movie.Genre.retainAll(ge);
                        if (!movie.Genre.isEmpty() && !data.getKey().contains(Title)) {
                            movies.add(dataSnapshot.getKey());
                            databaseReference1.child(key).setValue(key);
                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}