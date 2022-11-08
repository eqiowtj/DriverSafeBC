package edu.northeastern.driversafebc.a7atyourservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.databinding.ActivitySendStickerBinding;

public class SendStickerActivity extends AppCompatActivity {
    private ActivitySendStickerBinding binding;
    private Spinner dropdown;
    private TextView stickerSelectedTextView;
    private String currentSelectedSticker;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendStickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        stickerSelectedTextView = findViewById(R.id.stickerTextView);
    }

    public void selectStickerOnClick(View view) {
        Button button = (Button) view;
        stickerSelectedTextView.setText("Sticker " + button.getText() + " selected");
        currentSelectedSticker = (String) button.getText();
    }

    //when pressed, save data to firebase
    public void sendButtonOnClick(View view) {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("StickerSentHistory");


        //get the values: sender (current logged-in user), receiver, and sticker-id-to-send
        StickerHelper helper = new StickerHelper("dummy-sender", "dummy-receiver", currentSelectedSticker);
        reference.setValue(helper);
    }
}