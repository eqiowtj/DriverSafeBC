package edu.northeastern.driversafebc;

import android.content.res.Resources;
import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataProvider {

    private static final DatabaseReference databaseReference;
    private static Map<String, Long> totalScores;
    private static List<Long> bestScores;
    private static QuizData quizData;
    private static String username;

    static {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static void loadQuizData(Resources resources) {
        InputStream inputStream = resources.openRawResource(R.raw.quiz_data);
        Gson gson = new Gson();
        quizData = gson.fromJson(new InputStreamReader(inputStream), QuizData.class);
    }

    public static QuizData getQuizData() {
        return quizData;
    }

    public static int getTotalScore() {
        return totalScores.get(username).intValue();
    }

    public static int getBestScore(int id) {
        return bestScores.get(id).intValue();
    }

    public static Pair<Integer, Integer> getRank() {
        int userTotalScore = getTotalScore();
        ArrayList<Integer> allTotalScores = totalScores.values().stream().map(Long::intValue).sorted().collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(allTotalScores);
        int index = allTotalScores.indexOf(userTotalScore);
        return new Pair<>(index + 1, Math.round((getNumberOfUsers() - index) * 100f / getNumberOfUsers()));
    }

    public static int getSetsPassed() {
        int result = 0;
        for (QuizSet quizSet : getQuizData().getQuizSets()) {
            int totalQuestions = quizSet.getQuestions().size();
            int errorsAllowed = quizSet.getErrorsAllowed();
            int score = bestScores.get(quizSet.getId()).intValue();
            if (score + errorsAllowed >= totalQuestions) {
                result++;
            }
        }
        return result;
    }

    public static int getNumberOfUsers() {
        return totalScores.size();
    }

    public static void loadUserData(String username, Runnable callback) {
        DataProvider.username = username;
        databaseReference.child("users").child(username).setValue(true);
        databaseReference.child("totalScore").child(username).get()
                .addOnSuccessListener(task1 -> {
                    if (task1.getValue() == null) {
                        databaseReference.child("totalScore").child(username).setValue(0);
                        for (int i = 0; i < quizData.getNumberOfSets(); i++) {
                            databaseReference.child("bestScore").child(username).child(String.valueOf(quizData.getQuizSets().get(i).getId())).setValue(0);
                        }
                    }
                    databaseReference.child("totalScore").get()
                            .addOnSuccessListener(task2 -> {
                                totalScores = (Map<String, Long>) task2.getValue();
                                databaseReference.child("bestScore").child(username).get()
                                        .addOnSuccessListener(task3 -> {
                                            bestScores = (List<Long>) task3.getValue();
                                            System.out.println(bestScores);
                                            callback.run();
                                        });
                            });
                });
    }

    public static void logScore(int id, int score) {
        if (score > bestScores.get(id)) {
            int delta = (int) (score - bestScores.get(id));
            bestScores.set(id, (long) score);
            databaseReference.child("bestScore").child(username).child(String.valueOf(id)).setValue(score);
            long newTotal = totalScores.get(username) + delta;
            totalScores.put(username, newTotal);
            databaseReference.child("totalScore").child(username).setValue(newTotal);
        }
    }
}
