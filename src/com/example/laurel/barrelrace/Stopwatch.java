package com.example.laurel.barrelrace;

import java.util.concurrent.TimeUnit;

/*
Created by Laurel
 */
public class Stopwatch {
    private long startTime = 0;
    private boolean running = false;
    long millis = 0;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }


    public void stop() {
        this.running = false;
    }

    //elaspsed time in milliseconds
    public long getElapsedTime() {
        //Calculate milliseconds for the race
        millis =((System.currentTimeMillis() - startTime)/100) % 1000 ;

        return millis;

    }
    

}
