package com.example.laurel.barrelrace;
/*
Created by Laurel
 */
import android.os.Environment;
import com.example.laurel.barrelrace.scoreapp.ScoreManagerContent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileIO {
    String filename = "input.txt";
    FileInputStream inputStream;
    static File file = null;
    ScoreManagerContent inStream;
    boolean bExists;
    PrintWriter pw;
    final String LINE_SEPARATOR = "\r\n";

    public FileIO() {
        //Open new directory and file, if need be
        try {
            File newFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "MsgFolder");
            if (!newFolder.exists()) {
                bExists = newFolder.mkdirs();
            }
            try {
                file = new File(newFolder, filename);
                file.createNewFile();
            } catch (Exception ex) {
                System.out.println("ex: " + ex);
            }
        } catch (Exception e) {
            System.out.println("e: " + e);
        }

        try {
            //Open a stream to add items from file, if it already exists

            inputStream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            inStream = new ScoreManagerContent(br);

        } catch (Exception e) {

        }

    }
    public void save() {

        //Save is called in order to output the ArrayList to a file
        try {

            File newFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "MsgFolder");
            if (!newFolder.exists()) {
                bExists = newFolder.mkdirs();
            }
            file = new File(newFolder, filename);
            file.createNewFile();
            pw = new PrintWriter(file);

            //Rewrite file to contain the newly created, in order array list
           for(ScoreManagerContent.Score s : ScoreManagerContent.ITEMS) {
               pw.write(s.user + "," + s.score + LINE_SEPARATOR);
           }

            pw.close();

        } catch (Exception ex) {
            System.out.println("Error creating PW: " + ex.getMessage());
        }
    }

}