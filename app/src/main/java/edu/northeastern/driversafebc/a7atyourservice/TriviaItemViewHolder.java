package edu.northeastern.driversafebc.a7atyourservice;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;


public class TriviaItemViewHolder extends RecyclerView.ViewHolder {

    private final Handler uiHandler;
    private final RadioGroup radioGroup;
    private final TextView question;
    private final TextView category;
    private final TextView difficulty;
    private final Context context;

    public TriviaItemViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        uiHandler = new Handler();
        radioGroup = itemView.findViewById(R.id.triviaRadioGroup);
        question = itemView.findViewById(R.id.textViewQuestion);
        category = itemView.findViewById(R.id.textViewCategory);
        difficulty = itemView.findViewById(R.id.textViewDifficulty);
    }

    public void bind(Trivia triviaItem) {
        category.setText(triviaItem.getCategory());
        String difficultyText = triviaItem.getDifficulty().substring(0, 1).toUpperCase() + triviaItem.getDifficulty().substring(1);
        difficulty.setText(difficultyText);
        question.setText(triviaItem.getQuestion());
        refreshButtons(triviaItem, false);
    }

    private void answerButtonClicked(Trivia triviaItem, String answer) {
        triviaItem.select(answer);
        refreshButtons(triviaItem, true);
    }

    private void refreshButtons(Trivia triviaItem, boolean clicked) {
        radioGroup.removeAllViews();
        for (String answer : triviaItem.getAllAnswers()) {
            boolean isChecked = triviaItem.isSelected(answer);
            boolean isCorrect = triviaItem.isCorrect(answer);
            boolean useCorrectTheme = triviaItem.hasAnswered() && isCorrect;
            boolean useIncorrectTheme = isChecked && !isCorrect;
            RadioButton button = new RadioButton(context);

            if (useCorrectTheme) {
                button.setButtonTintList(context.getColorStateList(R.color.green_correct));
                button.setTextColor(context.getColorStateList(R.color.green_correct));
            } else if (useIncorrectTheme) {
                button.setButtonTintList(context.getColorStateList(R.color.red_incorrect));
                button.setTextColor(context.getColorStateList(R.color.red_incorrect));
            }

            if (clicked) {
                uiHandler.post(() -> button.setChecked(isChecked));
            } else {
                button.setChecked(isChecked);
            }

            button.setLayoutParams(
                    new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, 150));
            button.setTextSize(16);
            button.setEnabled(!triviaItem.hasAnswered());
            button.setText(answer);
            button.setOnClickListener(view -> answerButtonClicked(triviaItem, answer));
            radioGroup.addView(button);
        }
    }

}
