package edu.northeastern.driversafebc;

public class QuizSelectionItem {

    private final int bestScore;
    private final QuizSet quizSet;

    public QuizSelectionItem(QuizSet quizSet, int bestScore) {
        this.quizSet = quizSet;
        this.bestScore = bestScore;
    }

    public int getPercentage() {
        return Math.round(bestScore * 100f / getNumberOfQuestions());
    }

    public String getName() {
        return quizSet.getName();
    }

    public int getId() {
        return quizSet.getId();
    }

    public int getNumberOfQuestions() {
        return quizSet.getQuestions().size();
    }

    public int getErrorsAllowed() {
        return quizSet.getErrorsAllowed();
    }

    public int getBestScore() {
        return bestScore;
    }

    public QuizSet getQuizSet() {
        return quizSet;
    }
}
