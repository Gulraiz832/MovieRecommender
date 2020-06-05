package com.example.trailer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button notRegisteredButton;

    ProgressBar progressBar;

    DatabaseReference databaseReference;

    DataBaseHelper db;

    ArrayList<String> usernameList;
    ArrayList<String> emailList;

    static int state=0;
    static String global_username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        usernameList = getIntent().getStringArrayListExtra("usernameList");
        emailList = getIntent().getStringArrayListExtra("emailList");

        db = new DataBaseHelper(this);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        notRegisteredButton = (Button) findViewById(R.id.notRegisteredButton);

        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    usernameEditText.setError("Username is required!");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    passwordEditText.setError("Password is required!");
                    return;
                }


                if(!authenticateUsername(username))
                {
                    return;
                }

                if(!authenticatePassword(password))
                {
                    return;
                }

                searchFirebaseDatabase(username,password);

            }
        });



        notRegisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putStringArrayListExtra("usernameList", usernameList);
                intent.putStringArrayListExtra("emailList", emailList);
                startActivity(intent);
            }
        });
    }

    private boolean authenticateUsername(String username)
    {
        if(username.length() < 5)
        {
            usernameEditText.setError("Username must be at least 5 characters long!");
            usernameEditText.requestFocus();
            return false;
        }

        if(username.length() > 30)
        {
            usernameEditText.setError("Username must not exceed 30 characters!");
            usernameEditText.requestFocus();
            return false;
        }

        //checking for characters that are unacceptable as keys for firebase

        if(username.contains(".")  || username.contains("#") || (username.contains("/"))
                || username.contains("$") || username.contains("]") || username.contains("["))
        {
            usernameEditText.setError("Username has unacceptable character!");
            usernameEditText.requestFocus();
            return false;
        }

        //checking unacceptable ASCII characters
        for(int i=0; i!=username.length(); i++)
        {
            if((int)username.charAt(i) >= 0 && (int)username.charAt(i) <= 31)
            {
                usernameEditText.setError("Username has ASCII control character 0-31!");
                usernameEditText.requestFocus();
                return false;
            }

            if((int)username.charAt(i) == 127)
            {
                usernameEditText.setError("Username has ASCII control character 127!");
                usernameEditText.requestFocus();
                return false;
            }

            if(username.charAt(i) == ' ')
            {
                usernameEditText.setError("White spaces are not allowed!");
                usernameEditText.requestFocus();
                return false;
            }
        }

        return true;
    }

    private boolean authenticatePassword(String password)
    {
        if(password.length() < 10)
        {
            passwordEditText.setError("Password must be atleast 10 characters long!");
            passwordEditText.requestFocus();
            return false;
        }

        if(password.length() > 30)
        {
            passwordEditText.setError("Password must not exceed 30 characters!");
            passwordEditText.requestFocus();
            return false;
        }

        if(password.contains(" "))
        {
            passwordEditText.setError("White spaces not allowed!!");
            passwordEditText.requestFocus();
            return false;
        }

        return true;

    }

    private void searchFirebaseDatabase(final String username, final String password)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Account/" + username);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    // usernameEditText.setError(null);

                    String passwordFromDB = dataSnapshot.child("password").getValue(String.class);

                    if(passwordFromDB.equals(password))
                    {
                        setLoginStatusInLocalDB(username);
                        progressBar.setVisibility(View.VISIBLE);
                       // Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                        //intent.putExtra("username", username);
                        state=1;
                        global_username=username;
                        Intent returnIntent = new Intent();
                        setResult(2, returnIntent);
                        finish();
                    }
                    else
                    {
                        passwordEditText.setError("Incorrect password!");
                        passwordEditText.requestFocus();
                    }
                }
                else
                {
                    usernameEditText.setError("Username not found!");
                    usernameEditText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setLoginStatusInLocalDB(String username)
    {
        Cursor cursor = db.getData();

        while(cursor.moveToNext())
        {
            if(cursor.getString(0).equals(username))
            {
                db.updateData(username, 1);
            }
        }
    }


}
