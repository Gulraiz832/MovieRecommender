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
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String key=dataSnapshot.getKey();
                    Movie movie=dataSnapshot.getValue(Movie.class);
                    movie.Genre.retainAll(ge);
                    if(!movie.Genre.isEmpty()&&dataSnapshot.getKey()!=Title){
                        movies.add(dataSnapshot.getKey());
                        databaseReference.child("UserInfo").child(username).child("Recomendations").child(key).add
                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
