package edu.northeastern.driversafebc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuizQuestion implements Parcelable {

    private final String question;
    private final int correctAnswerIndex;
    private final List<String> answers;

    protected QuizQuestion(Parcel in) {
        question = in.readString();
        correctAnswerIndex = in.readInt();
        answers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeInt(correctAnswerIndex);
        dest.writeStringList(answers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizQuestion> CREATOR = new Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public List<String> getAnswers() {
        return answers;
    }

}
