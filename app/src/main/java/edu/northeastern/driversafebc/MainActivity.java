package edu.northeastern.driversafebc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.driversafebc.a7atyourservice.AtYourServiceActivity;
import edu.northeastern.driversafebc.a8stickit.StickitActivity;
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

    public void firebaseButtonClicked(View view) {
        Intent intent = new Intent(this, StickitActivity.class);
        startActivity(intent);
    }

    public void aboutClicked(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

}