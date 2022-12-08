package edu.northeastern.driversafebc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import edu.northeastern.driversafebc.a8stickit.TextValidateListener;
import edu.northeastern.driversafebc.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextValidateListener textValidateListener = new TextValidateListener(() -> {
            boolean isUsernameValid = binding.editTextUsernameDashboard.getText().toString().length() > 0;
            boolean isPasswordValid = binding.editTextPasswordDashboard.getText().toString().length() > 0;
            binding.buttonLoginDashboard.setEnabled(isUsernameValid && isPasswordValid);
        });

        binding.editTextUsernameDashboard.addTextChangedListener(textValidateListener);
        binding.editTextPasswordDashboard.addTextChangedListener(textValidateListener);

        if (savedInstanceState != null) {
            loggedInUser = savedInstanceState.getString("loggedInUser");
        }
        DataProvider.loadQuizData(getResources());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("loggedInUser", loggedInUser);
    }

    public void startButtonClicked(View view) {
        Intent intent = new Intent(this, QuizSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loggedInUser != null) {
            initialize();
        }
    }

    private void initialize() {
        binding.dashboardLoginContainer.setVisibility(View.GONE);
        binding.dashboardMainContainer.setVisibility(View.VISIBLE);
        binding.textViewWelcomeLabelUsername.setText(loggedInUser);

        int totalQuestions = DataProvider.getQuizData().getNumberOfQuestions();
        int userTotalScore = DataProvider.getTotalScore();
        int totalScorePercentage = Math.round(userTotalScore * 100f / totalQuestions);
        binding.textViewProgressText.setText(String.format(Locale.ENGLISH, "%d/%d questions passed", userTotalScore, totalQuestions));
        binding.progressBarOverallProgress.setProgress(totalScorePercentage);

        Pair<Integer, Integer> rank = DataProvider.getRank();
        int userRank = rank.first;
        int rankPercentage = rank.second;
        int numberOfUsers = DataProvider.getNumberOfUsers();
        binding.textViewRankText.setText(String.format(Locale.ENGLISH, "%d out of %d users", userRank, numberOfUsers));
        binding.progressBarRank.setProgress(rankPercentage);
    }

    private void login(String username) {
        loggedInUser = username;
        binding.editTextUsernameDashboard.setEnabled(false);
        binding.editTextPasswordDashboard.setEnabled(false);
        DataProvider.loadUserData(username, this::initialize);
    }

    public void loginButtonClicked(View view) {
        login(binding.editTextUsernameDashboard.getText().toString());
    }

}