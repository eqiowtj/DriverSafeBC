package edu.northeastern.driversafebc.a7atyourservice.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trivia {

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("question")
    private String question;

    @SerializedName("correct_answer")
    private String correctAnswer;

    @SerializedName("incorrect_answers")
    private List<String> incorrectAnswers;

    private String selectedAnswer;

    private List<String> allAnswers;

    public Trivia initialize() {
        allAnswers = new ArrayList<>(incorrectAnswers);
        allAnswers.add(correctAnswer);
        Collections.shuffle(allAnswers);
        return this;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAllAnswers() {
        return allAnswers;
    }

    public void select(String answer) {
        selectedAnswer = answer;
    }

    public boolean isSelected(String answer) {
        return answer.equals(selectedAnswer);
    }

    public boolean isCorrect(String answer) {
        return answer.equals(correctAnswer);
    }

    public boolean hasAnswered() {
        return selectedAnswer != null;
    }

}
