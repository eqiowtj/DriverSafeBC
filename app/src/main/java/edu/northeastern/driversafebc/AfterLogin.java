package edu.northeastern.driversafebc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AfterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Button logoutButton = findViewById(R.id.logout_button);
        TextView userNameTextView = findViewById(R.id.username_textview);
        Spinner spinner = findViewById(R.id.receiver_spinner);


        Intent intent = getIntent();
        String txt_UserName = intent.getStringExtra(LoginUserName.EXTRA_USERNAME);
        userNameTextView.setText("Welcome: " + txt_UserName);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ArrayList<String> nameArraylist = new ArrayList<>();


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("UserNames");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameArraylist.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    if (snapshot1.getValue().toString().equals(txt_UserName)){

                    }else {

                        nameArraylist.add(snapshot1.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(AfterLogin.this, android.R.layout.simple_spinner_dropdown_item,  nameArraylist);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(AfterLogin.this, "spinner chose", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });

//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(adapterView.getContext(), "Sending sticker to " + item , Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
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
                            if (task.isSuccessful()) {
                                Toast.makeText(AfterLogin.this, "Logged out", Toast.LENGTH_SHORT).show();
                            } else {
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