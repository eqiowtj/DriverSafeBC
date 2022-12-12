package edu.northeastern.driversafebc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import edu.northeastern.driversafebc.databinding.ActivityQuizSelectionBinding;

public class QuizSelectionActivity extends AppCompatActivity {

    private ActivityQuizSelectionBinding binding;
    private List<QuizSelectionItem> quizSelectionItemList;
    private QuizSelectionItemAdapter quizSelectionItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        int score = extras.getInt("score");
                        int id = extras.getInt("id");
                        DataProvider.logScore(id, score);
                        refreshProgress();
                    }
                }
        );

        quizSelectionItemList = new ArrayList<>();
        binding.recyclerViewQuizSelectionList.setLayoutManager(new LinearLayoutManager(this));
        quizSelectionItemAdapter = new QuizSelectionItemAdapter(this, quizSelectionItemList, launcher);
        binding.recyclerViewQuizSelectionList.setAdapter(quizSelectionItemAdapter);
        refreshProgress();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshProgress() {
        int numberOfSetsPassed = DataProvider.getSetsPassed();
        int numberOfSets = DataProvider.getQuizData().getNumberOfSets();
        int percentage = Math.round(numberOfSetsPassed * 100f / numberOfSets);
        binding.textViewSetsPassed.setText(String.format(Locale.ENGLISH, "%d/%d Sets Passed", numberOfSetsPassed, numberOfSets));
        binding.progressBarSetsCompleted.setProgress(percentage);

        quizSelectionItemList.clear();
        quizSelectionItemList.addAll(DataProvider.getQuizData().getQuizSets()
                .stream()
                .map(quizSet -> new QuizSelectionItem(quizSet, DataProvider.getBestScore(quizSet.getId())))
                .collect(Collectors.toList()));
        quizSelectionItemAdapter.notifyDataSetChanged();
    }
}
