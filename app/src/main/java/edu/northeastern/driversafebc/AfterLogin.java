package edu.northeastern.driversafebc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AfterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Button logoutButton = findViewById(R.id.logout_button);
        TextView userNameTextView = findViewById(R.id.username_textview);
        Intent intent = getIntent();
        String txt_UserName = intent.getStringExtra(LoginUserName.EXTRA_USERNAME);
        userNameTextView.setText(txt_UserName);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoginUser");


        myRef.setValue(txt_UserName);
        ArrayList<String> nameArraylist = new ArrayList<>();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                nameArraylist.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                    nameArraylist.add(snapshot1.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference myRef = database.getReference("UserNames");
                myRef.child(txt_UserName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AfterLogin.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AfterLogin.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                openLoginUserName();

            }
        });
    }
    public void openLoginUserName(){

        Intent intent = new Intent(this, LoginUserName.class);
        startActivity(intent);
    }
}