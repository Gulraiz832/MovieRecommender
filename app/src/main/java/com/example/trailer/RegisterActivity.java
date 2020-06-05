package com.example.trailer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button registerButton;

    ProgressBar progressBar;

    DatabaseReference databaseReference;

    DataBaseHelper db;

    private static final String TAG = "RegisterActivity";

    UserAccount userAccount;

    ArrayList<String> usernameList;
    ArrayList<String> emailList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        usernameList = getIntent().getStringArrayListExtra("usernameList");
        emailList = getIntent().getStringArrayListExtra("emailList");


        db = new DataBaseHelper(this);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    usernameEditText.setError("Username is required!");
                    return;
                }

                if(TextUtils.isEmpty(email))
                {
                    emailEditText.setError("Email is required!");
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

                if(!authenticateEmail(email))
                {
                    return;
                }

                if(!authenticatePassword(password))
                {
                    return;
                }

                addUserToLocalDatabase(username);
                registerUserToFirebase(username, email, password);

                progressBar.setVisibility(View.VISIBLE);
                LoginActivity.state=1;
                LoginActivity.global_username=username;
               // Intent intent = new Intent(RegisterActivity.this, LoggedInActivity.class);
                //intent.putExtra("username", username);
                Intent returnIntent = new Intent();
                setResult(2, returnIntent);
                finish();

            }
        });

    }


    private boolean authenticateUsername(final String username)
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

        if(usernameList != null)
        {
            for(String s: usernameList)
            {
                if(s.equals(username))
                {
                    usernameEditText.setError("Username already in use!");
                    usernameEditText.requestFocus();
                    return false;
                }
            }
        }


        return true;
    }

    private boolean authenticateEmail(String email)
    {
        if(!email.contains("@"))
        {
            emailEditText.setError("@ sign not found!");
            emailEditText.requestFocus();
            return false;
        }


        if(email.contains(" "))
        {
            emailEditText.setError("White spaces not allowed!!");
            emailEditText.requestFocus();
            return false;
        }

        if(emailList != null)
        {
            for(String s: emailList)
            {
                if(s.equals(email))
                {
                    emailEditText.setError("Email already in use!");
                    emailEditText.requestFocus();
                    return false;
                }
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

    private void registerUserToFirebase(String username, String email, String password)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Account");
        userAccount = new UserAccount(email, password);
        databaseReference.child(username).setValue(userAccount);

        Toast.makeText(RegisterActivity.this, "Data inserted!", Toast.LENGTH_LONG).show();

    }

    private boolean addUserToLocalDatabase(String username)
    {
        //when user registers, they will be automatically logged in. Hence, loginStatus = 1.
        if(db.insertData(username, 1))
        {
            return true;
        }
        else
        {
            //maybe old user is registering again
            return false;
        }
    }
}
