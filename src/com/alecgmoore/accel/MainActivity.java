package com.alecgmoore.accel;
/*
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import com.alecgmoore.movement.*;


public class MainActivity extends ActionBarActivity {

	private Context appContext;
	//private Accelerometer accel;
	//private PhysicsTimer physicsTimerThread;
	private ExampleImplementation egImpl;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private ImageView icon;
	private Thread egImplT;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        icon = (ImageView) findViewById(R.id.imageView1);
        
        appContext = getApplicationContext();
        egImpl = new ExampleImplementation(appContext, tv2,tv3,tv4, icon);


    }
    



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	egImpl.stop();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
        egImplT = new Thread(egImpl);
    	egImplT.start();
    }
}
*/
