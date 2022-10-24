package edu.northeastern.driversafebc.a7atyourservice;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;
import edu.northeastern.driversafebc.a7atyourservice.service.OpenTriviaService;
import edu.northeastern.driversafebc.a7atyourservice.view.ShowAlertDialog;
import edu.northeastern.driversafebc.databinding.ActivityAtYourServiceBinding;

public class AtYourServiceActivity extends AppCompatActivity {

    private static final int MIN_AMOUNT = 1;
    private static final int MAX_AMOUNT = 10;
    private static final int INITIAL_AMOUNT = 5;

    private ActivityAtYourServiceBinding binding;
    private OpenTriviaService openTriviaService;
    private Handler uiHandler;
    private TriviaItemAdapter triviaItemAdapter;
    private List<Button> allControls;

    private int amount;
    private String type;
    private String difficulty;
    private Dialog dialog;

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

        binding.radioGroupTriviaType.setOnCheckedChangeListener((view, id) -> typeRadioGroupChecked(id));
        binding.radioGroupDifficulty.setOnCheckedChangeListener((view, id) -> difficultyRadioGroupChecked(id));
        binding.buttonIncreaseNumber.setOnClickListener(view -> changeNumberButtonClicked(1));
        binding.buttonDecreaseNumber.setOnClickListener(view -> changeNumberButtonClicked(-1));

        amount = INITIAL_AMOUNT;
        type = "";
        difficulty = "";
        refreshControls();

        allControls = Arrays.asList(
                binding.radioButtonDifficultyAny, binding.radioButtonDifficultyEasy,
                binding.radioButtonDifficultyMedium, binding.radioButtonDifficultyHard,
                binding.radioButtonTypeAny, binding.radioButtonTypeBoolean, binding.radioButtonTypeMultiple,
                binding.buttonDecreaseNumber, binding.buttonIncreaseNumber, binding.buttonGetTrivia);
    }

    private void refreshControls() {
        refreshNumberOfTrivia();
        refreshTypeRadioGroup();
        refreshDifficultyRadioGroup();
    }

    private void refreshNumberOfTrivia() {
        binding.buttonIncreaseNumber.setEnabled(amount < MAX_AMOUNT);
        binding.buttonDecreaseNumber.setEnabled(amount > MIN_AMOUNT);
        binding.textViewNumberOfTrivia.setText(String.valueOf(amount));
    }

    private void refreshTypeRadioGroup() {
        binding.radioButtonTypeAny.setChecked(binding.radioButtonTypeAny.getTag().equals(type));
        binding.radioButtonTypeBoolean.setChecked(binding.radioButtonTypeBoolean.getTag().equals(type));
        binding.radioButtonTypeMultiple.setChecked(binding.radioButtonTypeMultiple.getTag().equals(type));
    }

    private void refreshDifficultyRadioGroup() {
        binding.radioButtonDifficultyAny.setChecked(binding.radioButtonDifficultyAny.getTag().equals(type));
        binding.radioButtonDifficultyEasy.setChecked(binding.radioButtonDifficultyEasy.getTag().equals(type));
        binding.radioButtonDifficultyMedium.setChecked(binding.radioButtonDifficultyMedium.getTag().equals(type));
        binding.radioButtonDifficultyHard.setChecked(binding.radioButtonDifficultyHard.getTag().equals(type));
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            showDialog();
        } else {
            closeDialog();
        }
        for (Button button : allControls) {
            button.setClickable(!isLoading);
        }
    }

    public void changeNumberButtonClicked(int delta) {
        amount += delta;
        refreshNumberOfTrivia();
    }

    public void typeRadioGroupChecked(int id) {
        type = findViewById(id).getTag().toString();
    }

    public void difficultyRadioGroupChecked(int id) {
        difficulty = findViewById(id).getTag().toString();
    }

    public void getTriviaButtonClicked(View view) {
        triviaItemAdapter.clearTriviaList();
        setLoading(true);
        openTriviaService.getTrivia(amount, difficulty, type, triviaResponse -> {
            List<Trivia> triviaList = new ArrayList<>();
            for (Trivia trivia : triviaResponse.getResults()) {
                triviaList.add(trivia.initialize());
            }
            uiHandler.post(() -> {
                setLoading(false);
                triviaItemAdapter.updateTriviaList(triviaList);
            });
        });
    }

    public void showDialog() {
        if (null == dialog) {
            dialog = ShowAlertDialog.loadingDialog(this);
        }
        dialog.show();
    }

    public void closeDialog() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

}