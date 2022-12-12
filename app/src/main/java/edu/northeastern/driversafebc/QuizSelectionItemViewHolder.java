package edu.northeastern.driversafebc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;


public class QuizSelectionItemViewHolder extends RecyclerView.ViewHolder {

    private final Handler uiHandler;
    private final View container;
    private final TextView title;
    private final TextView numberOfQuestions;
    private final TextView errorsAllowed;
    private final TextView progressText;
    private final ProgressBar progressBar;
    private final Context context;
    private final ActivityResultLauncher<Intent> launcher;

    public QuizSelectionItemViewHolder(@NonNull View itemView, Context context, ActivityResultLauncher<Intent> launcher) {
        super(itemView);
        this.container = itemView;
        this.context = context;
        this.launcher = launcher;
        uiHandler = new Handler();
        title = itemView.findViewById(R.id.textViewQuizSetTitle);
        numberOfQuestions = itemView.findViewById(R.id.textViewNumberOfQuestions);
        errorsAllowed = itemView.findViewById(R.id.textViewErrorsAllowed);
        progressText = itemView.findViewById(R.id.textViewProgress);
        progressBar = itemView.findViewById(R.id.progressCircle);
    }

    public void bind(QuizSelectionItem quizSelectionItem) {
        title.setText(quizSelectionItem.getName());
        numberOfQuestions.setText(String.format(Locale.ENGLISH, "%d Questions", quizSelectionItem.getNumberOfQuestions()));
        errorsAllowed.setText(String.format(Locale.ENGLISH, "%d Errors Allowed", quizSelectionItem.getErrorsAllowed()));
        progressText.setText(String.valueOf(quizSelectionItem.getPercentage()));
        progressBar.setProgress(quizSelectionItem.getPercentage());
        if (quizSelectionItem.getBestScore() == quizSelectionItem.getNumberOfQuestions()) {
            progressText.setText("✵");
            progressText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            progressText.setTextColor(context.getColorStateList(R.color.golden));
            progressBar.setProgressTintList(context.getColorStateList(R.color.golden));
        } else if (quizSelectionItem.getBestScore() == 0) {
            progressText.setTextColor(context.getColorStateList(R.color.gray));
            progressBar.setProgressTintList(context.getColorStateList(R.color.gray));
        } else if (quizSelectionItem.getBestScore() >= quizSelectionItem.getNumberOfQuestions() - quizSelectionItem.getErrorsAllowed()) {
            progressText.setTextColor(context.getColorStateList(R.color.green_correct));
            progressBar.setProgressTintList(context.getColorStateList(R.color.green_correct));
        } else {
            progressText.setTextColor(context.getColorStateList(R.color.red_incorrect));
            progressBar.setProgressTintList(context.getColorStateList(R.color.red_incorrect));
        }
        container.setOnClickListener(view -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("quizSet", quizSelectionItem.getQuizSet());
            launcher.launch(intent);
        });
    }

}
