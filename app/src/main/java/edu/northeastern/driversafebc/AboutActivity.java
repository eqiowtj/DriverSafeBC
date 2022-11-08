package edu.northeastern.driversafebc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private edu.northeastern.driversafebc.databinding.ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = edu.northeastern.driversafebc.databinding.ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
