package com.alecgmoore.movement;

import java.util.Timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.laurel.barrelrace.Stopwatch;

public class ExampleImplementation extends Thread implements Runnable{
	//private int screenWidth;
	//private int screenHeight;
	//private TextView TV2, TV3, TV4;
	//private ImageView Icon;
	private Context appContext;
	private boolean running = false;
	private PhysicsTimer physicsTimer;
	private Thread physicsTimerThread;
	//private Handler gameEndHandler;
	private Stopwatch timer;
	
	private int currentX = Integer.MAX_VALUE;
	private int currentY = Integer.MAX_VALUE;
	private float currentdX;
	private float currentdY;
	
	public void setCurrentX(int Xval){
		currentX = Xval;
		//TODO fix physics
	}
	
	public void setCurrentY(int Yval){
		currentY = Yval;
		//TODO fix physics
	}
	
	public int getCurrentX(){
		return currentX;
	}
	
	public int getCurrentY(){
		return currentY;
	}
	
	public void setCurrentdX(float dXval){
		currentdX = dXval;
		physicsTimer.setXVelocity(currentdX);
	}
	public void setCurrentdY(float dYval){
		currentdY = dYval;
		physicsTimer.setYVelocity(currentdY);
	}
	
	public float getCurrentdX(){
		return currentdX;
	}
	public float getCurrentdY(){
		return currentdY;
	}
	
	
    Handler physicsTimerHandler = new Handler() {
    	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public void handleMessage(Message msg) {
    		//Log.d("egImpl", "received callback from physics");
    		//View IcoContainer = (View) Icon.getParent();
    		//screenWidth = IcoContainer.getMeasuredWidth();
    		//screenHeight = IcoContainer.getMeasuredHeight();
    		//Log.d("egImpl","width = "+Integer.toString(screenWidth));
    		
    		Bundle bundle = msg.getData();

    		
    		
    		//String dXVal = Float.toString(bundle.getFloat("dX"));
    		//String dYVal = Float.toString(bundle.getFloat("dY"));
    		//String dZVal = Float.toString(bundle.getFloat("dZ"));
    		

    		//LayoutParams lp = new LayoutParams(Icon.getWidth(), Icon.getHeight());
    		//LayoutParams lpOld = (LayoutParams) Icon.getLayoutParams();

    		int newX = (int) (currentX - bundle.getFloat("dX")/1E4);
    		int newY = (int) (currentY + bundle.getFloat("dY")/1E4);
    		currentdX = bundle.getFloat("dX");
    		currentdY = bundle.getFloat("dY");
    		
    		currentX = newX;
    		currentY = newY;
    		// why - for x and + for y?
    		// because we're determining which is most down with the accelerometer, our values are essentially negative
    		// but then, also, the layout is positive x is right, but negative y is UP.
    		
    		
    		//Log.d("egImpl","lMargin = " + Integer.toString(lpOld.leftMargin));
    		//Log.d("egImpl","tMargin = " + Integer.toString(lpOld.topMargin));
    		//rough test of bounds
    		/*
    		if (newX < 0){
    			newX = 0;
    			physicsTimer.setXVelocity(0);
    			//Log.d("egImpl","hit left");
    		}
    		if (newX + Icon.getWidth() > screenWidth){
    			newX = screenWidth - Icon.getWidth();
    			physicsTimer.setXVelocity(0);
    			//Log.d("egImpl","hit right");
    		}
    		if (newY < 0){
    			newY = 0;
    			physicsTimer.setYVelocity(0);
    			//Log.d("egImpl","hit top");
    		}
    		if (newY + Icon.getHeight() > screenHeight){
    			newY = screenHeight - Icon.getHeight();
    			physicsTimer.setYVelocity(0);
    			//Log.d("egImpl","hit bottom");
    			
    			//FIXME make this better ending
    			Message gameEndmsg = gameEndHandler.obtainMessage();
    				Bundle gameEndbundle = new Bundle();
    				gameEndbundle.putBoolean("finished", true);
    				gameEndmsg.setData(gameEndbundle);
    			gameEndHandler.sendMessage(gameEndmsg);
    		}
    		*/
    		
    		//BARRELS COLLISIONS!
    		//Just the colllisiosnss
    		
    		
    		//lp.leftMargin
    		/*
    		lp.leftMargin = newX;
    		lp.topMargin = newY;
    		
    		Icon.clearAnimation();
    		int dXAnim = newX - lpOld.leftMargin;
    		int dYAnim = newY - lpOld.topMargin;
    		if (dXAnim + newX > screenWidth || dXAnim + newX < 0){
    			//likely to hit x wall, so don't animate.
    			dXAnim = 0;
    		}
    		if (dYAnim + newY > screenHeight || dYAnim + newY < 0){
    			//likely to hit y wall, so don't animate.
    			dYAnim = 0;
    		}
    		Animation txAnim = new TranslateAnimation(0,dXAnim,0,dYAnim);
    		txAnim.setDuration((long) bundle.getFloat("dT"));
    		float rotation = (float) (Math.atan2(dXAnim, -dYAnim));
    		float oldRot = Icon.getRotation();
    		Animation rotAnim = new RotateAnimation(oldRot,rotation);
    		rotAnim.setDuration((long) bundle.getFloat("dT"));
    		Icon.startAnimation(rotAnim);
    		Icon.startAnimation(txAnim);
    		

    		
    		Icon.setRotation((float) (rotation * 180 / Math.PI));
    		Icon.setLayoutParams(lp);
    		*/

    		
    	}
    };
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	//public ExampleImplementation(Context pContext, TextView tv2, TextView tv3, TextView tv4, ImageView icon){
	public ExampleImplementation (Context pContext) {//, ImageView icon, Handler rxHandler){
		//gameEndHandler = rxHandler;
		//Icon = icon;
		//TV2 = tv2;
		//TV3 = tv3;
		//TV4 = tv4;
		appContext = pContext;
		physicsTimer = new PhysicsTimer(appContext, physicsTimerHandler);
		//WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
		//Display display = wm.getDefaultDisplay();
		//Point size = new Point();
		//display.getSize(size);
		
		//View IcoContainer = (View) Icon.getParent();
		//android.view.ViewGroup.LayoutParams lp = IcoContainer.getLayoutParams();
		//screenWidth = IcoContainer.getMeasuredWidth();
		//screenHeight = IcoContainer.getMeasuredHeight();

		//Log.d("egImpl","width = "+Integer.toString(screenWidth));
		
		//LayoutParams initialLayoutParams = new LayoutParams(Icon.getWidth(), Icon.getHeight());
		//initialLayoutParams.leftMargin = screenWidth/2 - Icon.getWidth()/2;
		//initialLayoutParams.topMargin = screenHeight/3 * 2 - Icon.getHeight();
		//Icon.setLayoutParams(initialLayoutParams);

	}

	/*
	public void setToStartingLine(){
		View IcoContainer = (View) Icon.getParent();
		while (IcoContainer.getMeasuredWidth()==0){
			IcoContainer = (View) Icon.getParent();
		}
		screenWidth = IcoContainer.getMeasuredWidth();
		Log.d("screen width",Float.toString(screenWidth));

		screenHeight = IcoContainer.getMeasuredHeight();
		LayoutParams initialLayoutParams = new LayoutParams(Icon.getWidth(), Icon.getHeight());
		initialLayoutParams.leftMargin = screenWidth/2 - Icon.getWidth()/2;
		initialLayoutParams.topMargin = screenHeight/3 * 2 - Icon.getHeight();
		
		physicsTimer.resetVelocity();
	
	}
	*/

	@Override
	public void run() {

		//Icon.setLayoutParams(initialLayoutParams);
		//mOve the horse somewhere

		//Icon.set
		
		
		physicsTimerThread = new Thread( physicsTimer);
		physicsTimerThread.start();
		if (!running) running = true;
		while (running){
			//Log.d(this.getClass().getName(), Long.toString(timer.getElapsedTime()));
			//this.timeTextView.setText(Long.toString(timer.getElapsedTime()));

		}
		
	}
	public void pleaseStop() {
		running = false;
		physicsTimer.pleaseStop();
		interrupt();
	}



	public void pause(boolean pauseme) {
		physicsTimer.pause(pauseme);
		
	}

}
