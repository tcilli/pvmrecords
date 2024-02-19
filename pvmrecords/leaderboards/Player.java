package com.phukka.pvmrecords.leaderboards;

public class Player {

    public String username;
    public int points;

    public Player(String username, int points) {
        this.username = username.trim().toLowerCase();
        this.points = points;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    @Override
    public String toString() {
        return username + " " + points;
    }
}
