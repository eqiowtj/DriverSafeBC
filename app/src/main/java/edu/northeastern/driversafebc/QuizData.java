package edu.northeastern.driversafebc;

import java.util.List;

public class QuizData {

    private List<QuizSet> quizSets;

    public List<QuizSet> getQuizSets() {
        return quizSets;
    }

    public int getNumberOfSets() {
        return quizSets.size();
    }

    public int getNumberOfQuestions() {
        int result = 0;
        for (int i = 0; i < getNumberOfSets(); i++) {
            result += quizSets.get(i).getQuestions().size();
        }
        return result;
    }

}
