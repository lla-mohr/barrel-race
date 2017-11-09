package com.alecgmoore.movement;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PhysicsTimer extends Thread implements Runnable{

	
	private float Vx;
	private float Vy;
	private float Vz;
	private long tick;
	private long dT = 50;
	private boolean running = false;
	private Accelerometer accel;
	Handler postHandler;
	private boolean paused = false;
	private float topSpeed = (float) (2E3);
	
	public PhysicsTimer(Context appContext, Handler rxHandler) {
		accel = new Accelerometer(appContext);
		postHandler = rxHandler;
	}
	
	
	/*public void init(Context appContext, Handler rxHandler) {
		accel = new Accelerometer(appContext);
		postHandler = rxHandler;
	}*/
	
	public void resetVelocity(){
		Vx = 0;
		Vy = 0;
		Vz = 0;
	}
	
	public void setXVelocity(float newVx){
		Vx = newVx;
	}
	public void setYVelocity(float newVy){
		Vy = newVy;
	}
	public void setZVelocity(float newVz){
		Vz = newVz;
	}
	public void pause(boolean pauseme){
		paused = pauseme;
	}
	
	
	public void pleaseStop() {
		running = false;
		accel.unReg();
		interrupt();
	}
	@Override
	public void run() {
		if (running == false){
			accel.Reg();
			running = true;
		}
		tick = System.currentTimeMillis();
		while (running){
			if (!paused){
				while (System.currentTimeMillis() < tick + dT){
					synchronized (this) {
						try {
							wait (tick + dT - System.currentTimeMillis());
						} catch (Exception e) {}
					}
				}
				//physics update here:
				// true dT = System.currentTimeMillis() - tick;
				long true_dT = System.currentTimeMillis() - tick;
				//Log.d("pT","update from physics timer");
				tick = System.currentTimeMillis();
				//update our current velocities.
				Vx = dT * accel.X + Vx;
				Vy = dT * accel.Y + Vy;
				Vz = dT * accel.Z + Vz;
				

				if (Vx*Vx + Vy*Vy > topSpeed * topSpeed){
					float angle = (float) Math.atan2(Vy,Vx);
					//float magnitude = (float) Math.sqrt((double) (2 * topSpeed * topSpeed));
					Vx = (float) (Math.cos(angle)*topSpeed);
					Vy = (float) (Math.sin(angle)*topSpeed);
				}
				//turn these new velocities into changes in pos.
				float dX = dT * Vx;
				float dY = dT * Vy;
				float dZ = dT * Vz;
				Message msg = postHandler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putFloat("dX", dX);
					bundle.putFloat("dY", dY);
					bundle.putFloat("dZ", dZ);
					bundle.putFloat("dT", (float) dT);
				msg.setData(bundle);
				//Log.d("pT", "posting message");
				postHandler.sendMessage(msg);
			}
			
		}
	}


}
