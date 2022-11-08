package edu.northeastern.driversafebc.a8stickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

import edu.northeastern.driversafebc.R;

public class AfterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Button logoutButton = findViewById(R.id.logout_button);
        TextView userNameTextView = findViewById(R.id.username_textview);
        Spinner spinner = findViewById(R.id.receiver_spinner);
        EditText addresseeInput = findViewById(R.id.addressee_editText2);
        Button send_button = findViewById(R.id.send_button);
        EditText messsageToSendEdit = findViewById(R.id.message_to_send);
        TextView messageTextView = findViewById(R.id.message_textView);




        Intent intent = getIntent();
        String txt_UserName = intent.getStringExtra(LoginUserName.EXTRA_USERNAME);
        userNameTextView.setText("Welcome: " + txt_UserName);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ArrayList<String> nameArraylist = new ArrayList<>();


        //reading the loged in userName
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

        //receiving message
        String senderName = "";
        String message = "";
//        DatabaseReference myRefReceive = FirebaseDatabase.getInstance().getReference().child("Message");


        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(AfterLogin.this, android.R.layout.simple_spinner_dropdown_item,  nameArraylist);
        spinner.setAdapter(myAdapter);

        //send a message
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressee_string = addresseeInput.getText().toString();
                String messageToSendString = messsageToSendEdit.getText().toString();

                if (addressee_string.isEmpty()){
                    Toast.makeText(AfterLogin.this, "Please enter your addressee", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(AfterLogin.this, "sending", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Message").child("Sender").setValue(txt_UserName);
                    FirebaseDatabase.getInstance().getReference().child("Message").child("receiver").setValue(addressee_string);
                    FirebaseDatabase.getInstance().getReference().child("Message").child("Message:").setValue(messageToSendString);
                }
            }
        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            /**
//             * Called when a new item is selected (in the Spinner)
//             */
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//
//                Toast.makeText(AfterLogin.this, "Hello Toast",Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//
//            }
//
//        });



//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        String addressee = spinner.getSelectedItem().toString();
//                        Toast.makeText(AfterLogin.this, "sending to " + addressee, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        Toast.makeText(AfterLogin.this, "sending to no one", Toast.LENGTH_SHORT).show();
//                    }
//                });
//

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