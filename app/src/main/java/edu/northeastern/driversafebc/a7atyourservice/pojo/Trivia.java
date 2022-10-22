package edu.northeastern.driversafebc.a7atyourservice.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trivia {

    @SerializedName("type")
    public String type;

    @SerializedName("difficulty")
    public String difficulty;

    @SerializedName("question")
    public String question;

    @SerializedName("correct_answer")
    public String correctAnswer;

    @SerializedName("incorrect_answers")
    public List<String> incorrectAnswers;

    @NonNull
    @Override
    public String toString() {
        return "Trivia{" +
                "type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}
