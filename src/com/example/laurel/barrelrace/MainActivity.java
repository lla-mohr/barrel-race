package com.example.laurel.barrelrace;
/*
Created by Laurel
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.laurel.barrelrace.scoreapp.ScoreManagerContent;

import java.util.concurrent.TimeUnit;

import static com.example.laurel.barrelrace.scoreapp.ScoreManagerContent.addItem;

/*
public class MainActivity extends Activity {
    static FileIO file;
    EditText eText, name;
    String scoretime, username;
    Stopwatch timer = new Stopwatch();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_main_game);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        //Create a new static file from FileIO class
        file = new FileIO();
    }

    @Override
    public void onResume() {
        //Navigating back to a parent class will redraw the list
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_actions, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_scores) {
            //Launch new inent to view scores
            Intent launchNewIntent = new Intent(this, ScoreActivity.class);
            startActivity(launchNewIntent);
            if(eText!=null) {
                eText.setText("");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void start(View view) {
        eText = (EditText) findViewById(R.id.TextView01); //This text will hold the time for the race
        timer.start(); //start timer

    }

    public void end(View view) {
        timer.stop();//stop timer

        //add penalty seconds to the elapsed time here
        long millis = timer.getElapsedTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(seconds);

        eText.setText(minutes + ":" + seconds + ":" + millis);
        scoretime = String.valueOf(millis);

        final EditText input = new EditText(this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Great Job!")
                .setMessage("Enter a username")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        String test = value.toString();
                        if (test.isEmpty()) {
                            username = "User"; //add User as name if user does not care to enter a name
                        } else {
                            username = value.toString();
                        }
                        addItem(new ScoreManagerContent.Score(username, scoretime));
                        MainActivity.file.save();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }


    /**
     * A placeholder fragment containing a simple view.
     * <<---THIS HERE
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //Inflate the Button and Text fields for the game fragment
            View rootView = inflater.inflate(R.layout.fragment_scores_main_game, container, false);
            return rootView;
        }


    }
}*/
