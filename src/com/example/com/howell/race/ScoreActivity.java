package com.example.com.howell.race;

/*
Created by Laurel
 */
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;


import com.example.laurel.barrelrace.scoreapp.ScoreManagerContent;

import java.util.ArrayList;

public class ScoreActivity extends Activity {

    private static ArrayList<ScoreManagerContent.Score> ITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scores_display);
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
    	switch(item.getItemId()) {
    	case android.R.id.home:
    		NavUtils.navigateUpFromSameTask(this);
    		return true;
    	
    	}
    	return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_scores_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static class ScoreFragment extends ListFragment {
        ArrayAdapter adapter;

        public ScoreFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            //Display list for our ScoreActivity
            setListAdapter(new ArrayAdapter<ScoreManagerContent.Score>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    ScoreManagerContent.ITEMS));

            //ArrayAdapter will redraw the data structure connected to our UI, ie ArrayList chnaged elements will be reflected
            adapter = ((ArrayAdapter)getListAdapter());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onResume() {
            //Navigating back to a parent class will redraw the list
            super.onResume();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }
    }
}
