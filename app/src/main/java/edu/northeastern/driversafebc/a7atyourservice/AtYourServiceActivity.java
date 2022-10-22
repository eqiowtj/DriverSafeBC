package edu.northeastern.driversafebc.a7atyourservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.List;

import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;
import edu.northeastern.driversafebc.a7atyourservice.service.OpenTriviaService;
import edu.northeastern.driversafebc.databinding.ActivityAtYourServiceBinding;

public class AtYourServiceActivity extends AppCompatActivity {

    private ActivityAtYourServiceBinding binding;
    private OpenTriviaService openTriviaService;
    Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAtYourServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openTriviaService = new OpenTriviaService();
        uiHandler = new Handler();
    }

    public void getTriviaButtonClicked(View view) {

        openTriviaService.getTrivia(10, "easy", "multiple", triviaResponse -> {
            uiHandler.post(() -> {
                binding.textView.setText("");
                for (Trivia t : triviaResponse.results) {
                    binding.textView.append(t.question + "\n");
                }
            });
        });
    }

}