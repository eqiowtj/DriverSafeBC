package edu.northeastern.driversafebc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuizSet implements Parcelable {

    private final int id;
    private final String name;
    private final int errorsAllowed;
    private final List<QuizQuestion> questions;

    protected QuizSet(Parcel in) {
        id = in.readInt();
        name = in.readString();
        errorsAllowed = in.readInt();
        questions = in.createTypedArrayList(QuizQuestion.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(errorsAllowed);
        dest.writeTypedList(questions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizSet> CREATOR = new Creator<QuizSet>() {
        @Override
        public QuizSet createFromParcel(Parcel in) {
            return new QuizSet(in);
        }

        @Override
        public QuizSet[] newArray(int size) {
            return new QuizSet[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getErrorsAllowed() {
        return errorsAllowed;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }
}