package edu.northeastern.driversafebc.a7atyourservice;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;
import edu.northeastern.driversafebc.a7atyourservice.service.OpenTriviaService;
import edu.northeastern.driversafebc.databinding.ActivityAtYourServiceBinding;

public class AtYourServiceActivity extends AppCompatActivity {

    private ActivityAtYourServiceBinding binding;
    private OpenTriviaService openTriviaService;
    private Handler uiHandler;
    private TriviaItemAdapter triviaItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAtYourServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openTriviaService = new OpenTriviaService();
        uiHandler = new Handler();

        binding.recyclerViewTriviaList.setLayoutManager(new LinearLayoutManager(this));
        triviaItemAdapter = new TriviaItemAdapter(this);
        binding.recyclerViewTriviaList.setAdapter(triviaItemAdapter);
    }

    public void getTriviaButtonClicked(View view) {
        int amount = Integer.parseInt(binding.editTextNumberOfTrivia.getText().toString());
        String type = findViewById(binding.radioGroupTriviaType.getCheckedRadioButtonId()).getTag().toString();
        String difficulty = findViewById(binding.radioGroupDifficulty.getCheckedRadioButtonId()).getTag().toString();

        triviaItemAdapter.clearTriviaList();
        openTriviaService.getTrivia(amount, difficulty, type, triviaResponse -> {
            List<Trivia> triviaList = new ArrayList<>();
            for (Trivia trivia : triviaResponse.getResults()) {
                triviaList.add(trivia.initialize());
            }
            uiHandler.post(() -> triviaItemAdapter.updateTriviaList(triviaList));
        });
    }

}