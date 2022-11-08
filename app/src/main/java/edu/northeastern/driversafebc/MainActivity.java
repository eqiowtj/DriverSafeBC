package edu.northeastern.driversafebc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.driversafebc.a7atyourservice.AtYourServiceActivity;
import edu.northeastern.driversafebc.a8stickit.LoginUserName;
import edu.northeastern.driversafebc.a8stickit.SendStickerActivity;
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

    public void firebaseButtonClicked1(View view) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }

    public void aboutClicked(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void firebaseButtonClicked2(View view){
        Intent intent = new Intent(this, LoginUserName.class);
        startActivity(intent);
    }
}