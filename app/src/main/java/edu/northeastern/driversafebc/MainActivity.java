package edu.northeastern.driversafebc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.driversafebc.a7atyourservice.AtYourServiceActivity;
import edu.northeastern.driversafebc.a7atyourservice.SendStickerActivity;
import edu.northeastern.driversafebc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void atYourServiceButtonClicked(View view) {
        Intent intent = new Intent(this, AtYourServiceActivity.class);
        startActivity(intent);
    }

    //"Firebase button clicked"
    public void sendStickerButtonClicked(View view) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }

}