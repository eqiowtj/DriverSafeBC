package edu.northeastern.driversafebc.a7atyourservice.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trivia implements Parcelable {

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

    protected Trivia(Parcel in) {
        difficulty = in.readString();
        question = in.readString();
        correctAnswer = in.readString();
        incorrectAnswers = in.createStringArrayList();
        selectedAnswer = in.readString();
        allAnswers = in.createStringArrayList();
    }

    public static final Creator<Trivia> CREATOR = new Creator<Trivia>() {
        @Override
        public Trivia createFromParcel(Parcel in) {
            return new Trivia(in);
        }

        @Override
        public Trivia[] newArray(int size) {
            return new Trivia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(difficulty);
        parcel.writeString(question);
        parcel.writeString(correctAnswer);
        parcel.writeStringList(incorrectAnswers);
        parcel.writeString(selectedAnswer);
        parcel.writeStringList(allAnswers);
    }
}
