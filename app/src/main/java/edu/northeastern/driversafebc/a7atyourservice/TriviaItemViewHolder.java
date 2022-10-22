package edu.northeastern.driversafebc.a7atyourservice;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;


public class TriviaItemViewHolder extends RecyclerView.ViewHolder {

    private final Handler uiHandler;
    private final RadioGroup radioGroup;
    private final TextView question;
    private final TextView difficulty;
    private final Context context;
    private List<RadioButton> answerButtons;

    public TriviaItemViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        uiHandler = new Handler();
        radioGroup = itemView.findViewById(R.id.radioGroup);
        question = itemView.findViewById(R.id.textViewQuestion);
        difficulty = itemView.findViewById(R.id.textViewDifficulty);
    }

    public void bind(Trivia triviaItem) {
        question.setText(triviaItem.getQuestion());
        difficulty.setText(triviaItem.getDifficulty());
        radioGroup.removeAllViews();
        answerButtons = new ArrayList<>();

        for (String answer : triviaItem.getAllAnswers()) {
            RadioButton button = new RadioButton(context);
            button.setText(answer);
            button.setOnClickListener(view -> answerButtonClicked(triviaItem, answer));
            answerButtons.add(button);
            radioGroup.addView(button);
        }
        refreshButtonState(triviaItem);
    }

    private void answerButtonClicked(Trivia triviaItem, String answer) {
        triviaItem.select(answer);
        refreshButtonState(triviaItem);
    }

    private void refreshButtonState(Trivia triviaItem) {
        if (triviaItem.hasAnswered()) {
            for (RadioButton button : answerButtons) {
                String buttonText = button.getText().toString();
                boolean isSelected = triviaItem.isSelected(buttonText);
                boolean isCorrect = triviaItem.isCorrect(buttonText);
                if (isSelected) {
                    button.setChecked(false);
                    button.setChecked(true);
                }
                if (isSelected && !isCorrect) {
                    button.setTextColor(context.getColorStateList(R.color.red_incorrect));
                    uiHandler.post(() -> button.setButtonTintList(context.getColorStateList(R.color.red_incorrect)));
                } else if (isCorrect) {
                    button.setTextColor(context.getColorStateList(R.color.green_correct));
                    uiHandler.post(() -> button.setButtonTintList(context.getColorStateList(R.color.green_correct)));
                }
                button.setEnabled(false);
            }
        }
    }

}
