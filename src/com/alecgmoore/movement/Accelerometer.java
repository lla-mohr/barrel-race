package com.alecgmoore.movement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener{
	private Context appContext;	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	public float X;
	public float Y;
	public float Z;
	
	public Accelerometer(Context pContext){
		//if (pContext != null){
		appContext = pContext.getApplicationContext();
		//}
		//parentContext = pContext;
		mSensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	public boolean getAccelPresent(){
		return (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null);
	}
	
	/*public float getAccelValueX(){
		return mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).get
	}*/

	public void Reg(){
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		Log.d("Accel","registered for sensing accel");
	}
	
	public void unReg(){
		mSensorManager.unregisterListener(this);
		Log.d("Accel","unregistered for sensing accel");
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER )
			return;
		//we want to determine which way gravity is pointing, so we apply low pass filter.
		final float alpha = 0.8f;
		X = alpha * X + (1-alpha) * event.values[0];
		Y = alpha * Y + (1-alpha) * event.values[1];
		Z = alpha * Z + (1-alpha) * event.values[2];
		
		//Log.d("Accel","sensor changed");
		
		

		//X = event.values[0];
		//Y = event.values[1];
		//Z = event.values[2];
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	

}
