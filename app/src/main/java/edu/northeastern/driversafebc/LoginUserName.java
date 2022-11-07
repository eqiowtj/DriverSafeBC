package edu.northeastern.driversafebc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.northeastern.driversafebc.a7atyourservice.AboutActivity;

public class LoginUserName extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "com.example.application.example.EXTRA_USERNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        EditText userNameEditText = findViewById(R.id.username_edittext);
        Button createUserButton = findViewById(R.id.create_button);
        boolean isLogin = false;



        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_userName = userNameEditText.getText().toString();
                if (txt_userName.isEmpty()){
                    Toast.makeText(LoginUserName.this, "Enter your username", Toast.LENGTH_SHORT).show();
                }else {

                    ArrayList<String> nameArraylist = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference().child("UserNames").child(txt_userName).setValue(true);

                    Toast.makeText(LoginUserName.this, "Sent", Toast.LENGTH_SHORT).show();
                    openAfterLogin();


                }
            }
        });





    }
    public void openAfterLogin(){


        EditText userNameEditText = findViewById(R.id.username_edittext);
        String txt_userName = userNameEditText.getText().toString();

        Intent intent = new Intent(this, AfterLogin.class);
        intent.putExtra(EXTRA_USERNAME,txt_userName);
        startActivity(intent);
    }
}