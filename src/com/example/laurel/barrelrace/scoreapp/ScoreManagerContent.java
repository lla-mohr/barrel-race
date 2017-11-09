package com.example.laurel.barrelrace.scoreapp;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/*
Created by Laurel
 */


public class ScoreManagerContent {

    public static List<Score> ITEMS = new ArrayList<Score>();

    public ScoreManagerContent(BufferedReader br) {
        try {
            //Parse each input line from the internal file
            String inputString = "";
            String token[];
            while ((inputString = br.readLine()) != null) {
                //Statically call addItem(Score)
                token = inputString.split(",");
                addItem(new ScoreManagerContent.Score(token[0], token[1]));
            }
        } catch (Exception e) {
        }

    }


    public static void addItem(Score item) {
        //add Score object to corresponding data structures

        //I know this is dumb, but I'm tired
        //Add items in order, then remove any extras from the end
        if(ITEMS.size() <= 0) {
            ITEMS.add(item);
        } else {
            int size = ITEMS.size() - 1;
            for (int i = 0; i <= size; i++) {
                if (Integer.parseInt(item.getScore()) < Integer.parseInt(ITEMS.get(i).getScore())) {
                    ITEMS.add(i, item);
                    break;
                }
            }
            ITEMS.add(item);
        }

        if (ITEMS.size() > 10) {
            for (int i = ITEMS.size() - 1; i > 9; i--) ITEMS.remove(i);
        }
    }




public static class Score {

    //Score object
    public String score;
    public String user;

    public Score(String user, String score) {
        this.score = score;
        this.user = user;
    }

    public String getScore() {
        return this.score;
    }

    @Override
    public String toString() {

        //Display time in ScoreActivity ArrayList as a formatted time
        long millis = Long.parseLong(score);

        return user + " " + convertMillisToMMSSmm(millis);
    }
    
    public static String convertMillisToMMSSmm(long millis) {
    	long mi = millis % 10;
    	long s = (millis / 10) % 60;
        long m = (millis / (10*60)) % 60;
        return String.format("%02d:%02d.%01d", m,s,mi);
    }

}
}

