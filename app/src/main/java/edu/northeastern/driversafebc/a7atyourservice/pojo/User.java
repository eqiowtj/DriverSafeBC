package edu.northeastern.driversafebc.a7atyourservice.pojo;

public class User {
    String userName;
    Integer totalScore;
    Float rate; // total correct answers/total questions answered

    public String getUserName() {
        return userName;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Float getRate() {
        return rate;
    }
}
