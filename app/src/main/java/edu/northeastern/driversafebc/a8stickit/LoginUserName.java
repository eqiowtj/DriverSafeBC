package edu.northeastern.driversafebc.a8stickit;

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

import java.util.ArrayList;

import edu.northeastern.driversafebc.R;

public class LoginUserName extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "com.example.application.example.EXTRA_USERNAME";

    ArrayList<String> nameArraylist = new ArrayList<>();
    boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        EditText userNameEditText = findViewById(R.id.username_edittext);
        Button createUserButton = findViewById(R.id.create_button);
        boolean isLogin = false;

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("UserNames");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameArraylist.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    nameArraylist.add(snapshot1.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_userName = userNameEditText.getText().toString();
                if (txt_userName.isEmpty()){
                    Toast.makeText(LoginUserName.this, "Enter your username", Toast.LENGTH_SHORT).show();
                }else {
                    if (nameArraylist.contains(txt_userName)){

                        Toast.makeText(LoginUserName.this, "Username exists, enter another one", Toast.LENGTH_SHORT).show();

                    }else {

                        FirebaseDatabase.getInstance().getReference().child("UserNames").child(txt_userName).setValue(txt_userName);
                        Toast.makeText(LoginUserName.this, "Welcome "  + txt_userName, Toast.LENGTH_SHORT).show();

                        openAfterLogin();
                    }




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