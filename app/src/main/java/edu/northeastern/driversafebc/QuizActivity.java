package edu.northeastern.driversafebc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import edu.northeastern.driversafebc.databinding.ActivityQuizBinding;

public class QuizActivity extends AppCompatActivity {

    private Handler uiHandler;
    private ActivityQuizBinding binding;

    private QuizSet quizSet;
    private int currentQuestionIndex;
    private ArrayList<Integer> answers;
    private int currentScore;
    private boolean hasFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        uiHandler = new Handler();
        setContentView(binding.getRoot());
        quizSet = getIntent().getParcelableExtra("quizSet");

        if (savedInstanceState == null) {
            currentQuestionIndex = 0;
            answers = new ArrayList<>();
            currentScore = 0;
            hasFinished = false;
        } else {
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
            answers = savedInstanceState.getIntegerArrayList("answers");
            currentScore = savedInstanceState.getInt("currentScore");
            hasFinished = savedInstanceState.getBoolean("hasFinished");
        }

        loadQuizQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
        outState.putIntegerArrayList("answers", answers);
        outState.putInt("currentScore", currentScore);
        outState.putBoolean("hasFinished", hasFinished);
    }

    private void loadQuizQuestion() {
        if (currentQuestionIndex < quizSet.getQuestions().size()) {
            showQuiz();
            binding.textViewQuestionSetName.setText(quizSet.getName());
            binding.textViewQuestionSetProgress.setText(String.format(Locale.ENGLISH, "%d/%d", currentQuestionIndex + 1, quizSet.getQuestions().size()));
            binding.progressBarQuestionSet.setProgress(Math.round((currentQuestionIndex + 1) * 100f / quizSet.getQuestions().size()));
            binding.textViewQuestionText.setText(quizSet.getQuestions().get(currentQuestionIndex).getQuestion());
            refreshPrevNextButtons();
            refreshRadioButtons(answers.size() > currentQuestionIndex ? answers.get(currentQuestionIndex) : -1, false);
        } else {
            hasFinished = true;
            showResult();
        }
    }

    private void answerButtonClicked(int answerIndex) {
        if (answers.size() == currentQuestionIndex) {
            answers.add(answerIndex);
            if (answerIndex == quizSet.getQuestions().get(currentQuestionIndex).getCorrectAnswerIndex()) {
                currentScore++;
            }
        }
        refreshPrevNextButtons();
        refreshRadioButtons(answerIndex, true);
    }

    public void prevButtonClicked(View view) {
        currentQuestionIndex--;
        loadQuizQuestion();
    }

    public void reviewButtonClicked(View view) {
        prevButtonClicked(view);
    }

    public void nextButtonClicked(View view) {
        currentQuestionIndex++;
        loadQuizQuestion();
    }

    public void continueButtonClicked(View view) {
        setResult(RESULT_OK, new Intent().putExtra("score", currentScore).putExtra("id", quizSet.getId()));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (hasFinished) {
            setResult(RESULT_OK, new Intent().putExtra("score", currentScore).putExtra("id", quizSet.getId()));
            super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to exit? Your progress of this take will be lost.")
                    .setPositiveButton("Yes",
                            (dialog, id) -> super.onBackPressed())
                    .setNegativeButton("No", null)
                    .setCancelable(false)
                    .show();
        }
    }

    private void showQuiz() {
        binding.quizContainer.setVisibility(View.VISIBLE);
        binding.resultContainer.setVisibility(View.GONE);
    }

    private void showResult() {
        binding.quizContainer.setVisibility(View.GONE);
        binding.resultContainer.setVisibility(View.VISIBLE);
        binding.textViewProgressQuizFinish.setText(String.valueOf(Math.round(currentScore * 100f / quizSet.getQuestions().size())));
        binding.progressCircleQuizFinish.setProgress(Math.round(currentScore * 100f / quizSet.getQuestions().size()));
        if (currentScore == quizSet.getQuestions().size()) {
            binding.textViewProgressQuizFinish.setText("✵");
            binding.textViewProgressQuizFinish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 130);
            binding.textViewProgressQuizFinish.setTextColor(this.getColorStateList(R.color.golden));
            binding.progressCircleQuizFinish.setProgressTintList(this.getColorStateList(R.color.golden));
            binding.textViewResultDetail.setText("Congratulations!\nYou aced this quiz!");
        } else if (currentScore >= quizSet.getQuestions().size() - quizSet.getErrorsAllowed()) {
            binding.textViewProgressQuizFinish.setTextColor(this.getColorStateList(R.color.green_correct));
            binding.progressCircleQuizFinish.setProgressTintList(this.getColorStateList(R.color.green_correct));
            binding.textViewResultDetail.setText("You passed this quiz!\nNext time, try to get 100!");
        } else {
            binding.textViewProgressQuizFinish.setTextColor(this.getColorStateList(R.color.red_incorrect));
            binding.progressCircleQuizFinish.setProgressTintList(this.getColorStateList(R.color.red_incorrect));
            binding.textViewResultDetail.setText("You did not pass this quiz.\nPlease review and try again.");
        }
    }

    private void refreshPrevNextButtons() {
        binding.buttonPrev.setEnabled(currentQuestionIndex > 0);
        binding.buttonNext.setEnabled(currentQuestionIndex < quizSet.getQuestions().size() && answers.size() >= currentQuestionIndex + 1);
    }

    private void refreshRadioButtons(int checkedIndex, boolean clicked) {
        binding.quizRadioGroup.removeAllViews();
        QuizQuestion quizQuestion = quizSet.getQuestions().get(currentQuestionIndex);
        boolean hasAnswered = checkedIndex >= 0;
        for (int i = 0; i < quizQuestion.getAnswers().size(); i++) {
            boolean isChecked = checkedIndex == i;
            boolean isCorrect = quizQuestion.getCorrectAnswerIndex() == i;
            boolean useCorrectTheme = hasAnswered && isCorrect;
            boolean useIncorrectTheme = isChecked && !isCorrect;
            RadioButton button = new RadioButton(this);

            if (useCorrectTheme) {
                button.setButtonTintList(this.getColorStateList(R.color.green_correct));
                button.setTextColor(this.getColorStateList(R.color.green_correct));
            } else if (useIncorrectTheme) {
                button.setButtonTintList(this.getColorStateList(R.color.red_incorrect));
                button.setTextColor(this.getColorStateList(R.color.red_incorrect));
            }

            if (clicked) {
                uiHandler.post(() -> button.setChecked(isChecked));
            } else {
                button.setChecked(isChecked);
            }

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 16, 0, 16);
            button.setLayoutParams(layoutParams);
            button.setTextSize(16);
            button.setEnabled(!hasAnswered);
            button.setText(quizQuestion.getAnswers().get(i));
            int finalI = i;
            button.setOnClickListener(view -> answerButtonClicked(finalI));
            binding.quizRadioGroup.addView(button);
        }
    }
}