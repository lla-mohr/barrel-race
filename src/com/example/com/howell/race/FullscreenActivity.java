package com.example.com.howell.race;

import static com.example.laurel.barrelrace.scoreapp.ScoreManagerContent.addItem;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.alecgmoore.movement.ExampleImplementation;

import com.example.com.howell.race.util.SystemUiHider;
import com.example.laurel.barrelrace.FileIO;

import com.example.laurel.barrelrace.Stopwatch;
import com.example.laurel.barrelrace.scoreapp.ScoreManagerContent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.view.SurfaceHolder.Callback;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements Callback {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	static FileIO file;
	EditText eText, name;
	String scoretime, username;
	Stopwatch timer = new Stopwatch();
	private ExampleImplementation egImpl;
	private Thread egImplT;
	private TextView timeTextView;
	private long elapsedTime = 0;
	private boolean isPaused = false;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Bitmap horseBitmap;
	private Bitmap dirtBitmap;
	private Bitmap barrelBitmap;
	private Bitmap greenCheckBitmap;
	private Bitmap redXBitmap;
	private GameLoop gameLoop;

	// This if for the game rules
	// private float currentBarrelArc = 0;
	private double barrelArcLeft = 0;
	private double barrelArcRight = 0;
	private double barrelArcTop = 0;
	private int currentBarrel = 0;
	private boolean barrelTopCompleted = false;
	private boolean barrelLeftCompleted = false;
	private boolean barrelRightCompleted = false;
	private int lastX;
	private int lastY;
	private Vector leftCurrent;
	private Vector leftLast;
	private Vector rightCurrent;
	private Vector rightLast;
	private Vector topCurrent;
	private Vector topLast;
	
	private TimerTask timerTask;

	private View homeView;
	private boolean useUnpauseTimer = false;
	
	
	final Handler clockHandler = new Handler();
	Timer clockTimer = new Timer();

	/*
	 * Handler gameEndHandler = new Handler() {
	 * 
	 * @TargetApi(Build.VERSION_CODES.HONEYCOMB) public void
	 * handleMessage(Message msg) {
	 * 
	 * Bundle bundle = msg.getData();
	 * 
	 * if (bundle.getBoolean("finished")){ //Log.d("gameEndHandler","finished");
	 * }
	 * 
	 * //TODO redo stop button functionality here
	 * 
	 * } };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		timeTextView = (TextView) findViewById(R.id.textView01);
		surfaceView = (SurfaceView) findViewById(R.id.racing_arena);
		surfaceView.setZOrderOnTop(true);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.Button01).setOnTouchListener(mDelayHideTouchListener);

		// Create a new static file from FileIO class
		file = new FileIO();

		// Get the View in order to pause the game to view scores
		homeView = findViewById(R.id.fullscreen_content);

		// set up for movement
		// ImageView icon = (ImageView) findViewById(R.id.horseView);
		Context appContext = getApplicationContext();
		egImpl = new ExampleImplementation(appContext); // , icon,
														// gameEndHandler);

		Resources resources = getResources();
		horseBitmap = BitmapFactory.decodeResource(resources, R.drawable.horse);
		barrelBitmap = BitmapFactory.decodeResource(resources,
				R.drawable.barrel);
		dirtBitmap = BitmapFactory.decodeResource(resources,
				R.drawable.background);
		greenCheckBitmap = BitmapFactory.decodeResource(resources,
				R.drawable.greencheck);
		redXBitmap = BitmapFactory.decodeResource(resources, R.drawable.redx);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	@Override
	public void onResume() {
		// egImpl.setToStartingLine();

		super.onResume();
		

	}

	@Override
	public void onPause() {
		super.onPause();
		// egImpl.stop();
		this.isPaused = false;
		this.pause(homeView);

	}
	
	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_actions, menu);

		return true;
	}

	public static String convertMillisToMMSSmm(long millis) {
		long mi = millis % 10;
		long s = (millis / 10) % 60;
		long m = (millis / (10 * 60)) % 60;
		return String.format("%02d:%02d.%01d", m, s, mi);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_scores) {
			// Launch new intent to view scores
			// Pause game in order to view scores
			if (!gameLoop.running){
			Intent launchNewIntent = new Intent(this, ScoreActivity.class);
			startActivity(launchNewIntent);
			}

		}
		return super.onOptionsItemSelected(item);
	}

	private void draw() {
		try {
			Canvas canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas();

				if (canvas != null) {
					canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
					drawTheHorsieArenaAndTheHorseAndTheBarrelsAndOtherThings(canvas);
				}
			} catch(Exception e) {
				if(canvas != null) {
					canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				}
			}
			finally {
				if (canvas != null) {
					//canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		} catch (Exception e) {
			// FU
		}
	}

	private void drawTheHorsieArenaAndTheHorseAndTheBarrelsAndOtherThings(
			Canvas canvas) {
		// TODO Auto-generated method stub
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		Paint tempPaint = new Paint();
		tempPaint.setColor(Color.BLACK);
		tempPaint.setAntiAlias(true);
		tempPaint.setStrokeWidth(300);

		Paint debugPaint = new Paint();
		debugPaint.setColor(Color.RED);
		debugPaint.setAntiAlias(true);
		debugPaint.setStyle(Style.FILL);
		debugPaint.setStrokeWidth(5);
		debugPaint.setTextSize(60);

		Paint debugPaint2 = new Paint();
		debugPaint2.setColor(Color.YELLOW);
		debugPaint2.setAntiAlias(true);
		debugPaint2.setStyle(Style.FILL);
		debugPaint2.setStrokeWidth(5);
		debugPaint2.setTextSize(40);

		Paint debugPaint3 = new Paint();
		debugPaint3.setColor(Color.GREEN);
		debugPaint3.setAntiAlias(true);
		debugPaint3.setStyle(Style.FILL);
		debugPaint3.setStrokeWidth(5);
		debugPaint3.setTextSize(42);

		Rect src;
		Rect dst;
		// src = new Rect(0, 0, width, height);
		// dst = new Rect(x, y, x + width, y + height);
		// canvas.drawBitmap(barrelBitmap, null, new Rect(0, 0, width, height),
		// null);
		int radius = width / 12;
		// Draw the gate
		canvas.drawLine(0, height, width / 5 * 2, height, tempPaint);
		canvas.drawLine(width / 5 * 3, height, width, height, tempPaint);
		canvas.drawLine((float)width / 5 * 2, (float)(height - (radius * 1.5)), width / 5 * 3,(float) (height - (radius * 1.5)), debugPaint3);
		
		
		
		// Barrel Positions
		
		// Top Barrel
		src = new Rect(0, 0, 32, 32);
		int topBarrelX = width / 2;
		int topBarrelY = height / 3;
		dst = new Rect(width / 2 - radius, height / 3 - radius, width / 2
				+ radius, height / 3 + radius);
		if (!barrelTopCompleted) {
			canvas.drawBitmap(barrelBitmap, null, dst, null);
		} else {
			canvas.drawBitmap(greenCheckBitmap, null, dst, null);
		}
		// Left Barrel
		int leftBarrelX = width / 10 * 3;
		int leftBarrelY = height / 5 * 3;
		dst = new Rect(leftBarrelX - radius, leftBarrelY - radius, leftBarrelX
				+ radius, leftBarrelY + radius);
		if (!barrelLeftCompleted) {
			canvas.drawBitmap(barrelBitmap, null, dst, null);
		} else {
			canvas.drawBitmap(greenCheckBitmap, null, dst, null);
		}
		// Right Barrel
		int rightBarrelX = width / 10 * 7;
		dst = new Rect(rightBarrelX - radius, leftBarrelY - radius,
				rightBarrelX + radius, leftBarrelY + radius);
		if (!barrelRightCompleted) {
			canvas.drawBitmap(barrelBitmap, null, dst, null);
		} else {
			canvas.drawBitmap(greenCheckBitmap, null, dst, null);
		}
		// canvas.drawLine(0, 0, width, height, tempPaint);

		if (egImpl.getCurrentX() == Integer.MAX_VALUE) {
			egImpl.setCurrentX(width / 2);
			egImpl.setCurrentY(height / 10 * 9);
		}

		// HOERS
		// Draw the horse here
		int currentX = egImpl.getCurrentX();
		int currentY = egImpl.getCurrentY();
		dst = new Rect(currentX - radius, currentY - radius, currentX + radius,
				currentY + radius);
		canvas.drawBitmap(horseBitmap, null, dst, null);

		// Check if the player is touching a wall
		if (currentX <= radius || currentX >= width - radius
				|| currentY <= radius || currentY >= height - radius) {
			elapsedTime += 50;
			// reverse the velocity so that we bounce off walls.
			if (currentX <= radius) {
				egImpl.setCurrentdX(-1 * egImpl.getCurrentdX());
				egImpl.setCurrentX(radius + 1); // maybe +1
				egImpl.setCurrentY(currentY);
			} else if (currentX >= width - radius) {
				egImpl.setCurrentdX(-1 * egImpl.getCurrentdX());
				egImpl.setCurrentX(width - radius - 1); // maybe -1
			}

			if (currentY <= radius) {
				egImpl.setCurrentdY(-1 * egImpl.getCurrentdY());
				egImpl.setCurrentY(radius + 1); // maybe +1
			} else if (currentY >= height - radius) {
				egImpl.setCurrentdY(-1 * egImpl.getCurrentdY());
				egImpl.setCurrentY(height - radius - 1); // maybe -1
			}
		}
		// Check if the player is touching a barrel
		// Left Barrel
		double distance = Math.sqrt((currentX - leftBarrelX)
				* (currentX - leftBarrelX) + (currentY - leftBarrelY)
				* (currentY - leftBarrelY));
		if (distance < 2 * radius) {
			// Game over
			// int leftBarrelX = width/10*3;
			// int leftBarrelY = height/5*3;
			currentBarrel = 0;
			barrelTopCompleted = false;
			barrelLeftCompleted = false;
			barrelRightCompleted = false;
			
			dst = new Rect(leftBarrelX - radius, leftBarrelY - radius,
					leftBarrelX + radius, leftBarrelY + radius);
			canvas.drawBitmap(redXBitmap, null, dst, null);

			String writeme = "GAME OVER! \r Press Start to play again";
			canvas.drawText(writeme, 0, writeme.length(), 0,
					height/2, debugPaint);
			pause(homeView);
			gameLoop.pleaseStop();
			egImpl.pleaseStop();
		}

		// Right Barrel
		distance = Math.sqrt((currentX - rightBarrelX)
				* (currentX - rightBarrelX) + (currentY - leftBarrelY)
				* (currentY - leftBarrelY));
		if (distance < 2 * radius) {
			// Game over
			currentBarrel = 0;
			barrelTopCompleted = false;
			barrelLeftCompleted = false;
			barrelRightCompleted = false;
			
			dst = new Rect(rightBarrelX - radius, leftBarrelY - radius,
					rightBarrelX + radius, leftBarrelY + radius);
			canvas.drawBitmap(redXBitmap, null, dst, null);
			
			String writeme = "GAME OVER! \r Press Start to play again";
			canvas.drawText(writeme, 0, writeme.length(), 0,
					height/2, debugPaint);
			pause(homeView);
			gameLoop.pleaseStop();
			egImpl.pleaseStop();
		}

		// Top Barrel
		distance = Math.sqrt((currentX - topBarrelX) * (currentX - topBarrelX)
				+ (currentY - topBarrelY) * (currentY - topBarrelY));
		if (distance < 2 * radius) {
			// Game over
			currentBarrel = 0;
			barrelTopCompleted = false;
			barrelLeftCompleted = false;
			barrelRightCompleted = false;
			
			dst = new Rect(width / 2 - radius, height / 3 - radius, width / 2
					+ radius, height / 3 + radius);
			canvas.drawBitmap(redXBitmap, null, dst, null);
			
			String writeme = "GAME OVER! \r Press Start to play again";
			canvas.drawText(writeme, 0, writeme.length(), 0,
					height/2, debugPaint);
			pause(homeView);
			gameLoop.pleaseStop();
			egImpl.pleaseStop();
		}

		/*
		 * Barrels 0: no barrel -> checking for being between the barrels 1:
		 * checking for going around the first barrel 2: checking for going
		 * around the second barrel 3: checking for going around the top barrel
		 * 4: checking for touching the gate...
		 */
		// Check
		double rightAngle;
		double leftAngle;
		double topAngle;
		switch (currentBarrel) {
		case 0:
			//currentBarrel = 4;
			// We haven't really started
			// First goal is to get between the barrels

			
			// if the player is above the line of the barrels
			if (currentY <= leftBarrelY) {
				// if the player is between the two barrels
				if (currentX > leftBarrelX && currentX < rightBarrelX) {
					// escalate the currentBarrel++;
					currentBarrel++;
					// Lets set up for the next step
					barrelArcLeft = Double.valueOf(0);
					barrelArcRight = Double.valueOf(0);
					leftLast = new Vector(currentX - leftBarrelX, currentY
							- leftBarrelY);
					rightLast = new Vector(currentX - rightBarrelX, currentY
							- leftBarrelY);
				}
			}
			break;
		case 1:
			// get the angle between the left barrel and the player
			// take into account the last position and current position to get
			// the "arc"

			leftCurrent = new Vector(currentX - leftBarrelX, currentY
					- leftBarrelY);
			rightCurrent = new Vector(currentX - rightBarrelX, currentY
					- leftBarrelY);

			//canvas.drawLine(currentX, currentY, leftBarrelX, leftBarrelY,
			//		debugPaint);
			//canvas.drawLine(lastX, lastY, leftBarrelX, leftBarrelY, debugPaint);

			// get the angle between the right barrel
			leftAngle = Vector.signedAngleBetween(leftCurrent, leftLast);

			rightAngle = Vector.signedAngleBetween(rightCurrent, rightLast);

			//String writeme = Double.toString(barrelArcLeft);
			//		leftBarrelY, debugPaint);

			// add them to their current arcs... barrelArcLeft and barrelArc
			// right
			if (!Double.isNaN(leftAngle)) {
				barrelArcLeft += leftAngle;
				barrelArcRight += rightAngle;
			}
			// check the size of the arcs
			// if one of the arcs is really big, like we're talking 270 degrees
			// big
			// then set the barrel to being complete
			if (barrelArcLeft > 270) {
				// WE JUST WENT AROUND THE BARREL!!!
				barrelLeftCompleted = true;
				currentBarrel++;
				// lets set up for the next step
				barrelArcRight = 0;
				barrelArcLeft = 0;
			} else if (barrelArcRight < -270) {
				// WE JUST WENT AROUND THE OTHER WAY
				barrelRightCompleted = true;
				currentBarrel++;
				// lets set up for the next step
				barrelArcLeft = 0;
				barrelArcRight = 0;
			}
			// change the currentBarrel++;
			leftLast = leftCurrent;
			rightLast = rightCurrent;
			break;
		case 2:
			if (barrelLeftCompleted) {

				// Check only the right barrel
				rightCurrent = new Vector(currentX - rightBarrelX, currentY
						- leftBarrelY);
				rightAngle = Vector.signedAngleBetween(rightCurrent, rightLast);

				// DEBUG
				/*canvas.drawLine(currentX, currentY, rightBarrelX, leftBarrelY,
						debugPaint);
				canvas.drawLine(lastX, lastY, rightBarrelX, leftBarrelY,
						debugPaint2);
				writeme = Double.toString(barrelArcRight);
				canvas.drawText(writeme, 0, writeme.length(), rightBarrelX,
						leftBarrelY, debugPaint);*/
				// DEBUG

				if (!Double.isNaN(rightAngle)) {
					barrelArcRight += rightAngle;
				}
				if (barrelArcRight < -270) {
					// WE JUST WENT AROUND THE OTHER WAY
					barrelRightCompleted = true;
					currentBarrel++;
					// lets set up for the next step
					barrelArcTop = 0;
					barrelArcLeft = 0;
					barrelArcRight = 0;
					topLast = new Vector(currentX - topBarrelX, currentY
							- topBarrelY);
				}

			} else {
				// Check only the left barrel
				leftCurrent = new Vector(currentX - leftBarrelX, currentY
						- leftBarrelY);
				leftAngle = Vector.signedAngleBetween(leftCurrent, leftLast);

				// DEBUG
				// canvas.drawLine(currentX, currentY, leftBarrelX, leftBarrelY,
				// debugPaint);
				// canvas.drawLine(lastX, lastY, leftBarrelX, leftBarrelY,
				// debugPaint2);
				//writeme = Double.toString(barrelArcLeft);
				// canvas.drawText(writeme, 0, writeme.length(), leftBarrelX,
				// leftBarrelY, debugPaint);
				// DEBUG

				if (!Double.isNaN(leftAngle)) {
					barrelArcLeft += leftAngle;
				}
				if (barrelArcLeft > 270) {
					// WE JUST WENT AROUND THE OTHER WAY
					barrelLeftCompleted = true;
					currentBarrel++;
					// lets set up for the next step
					barrelArcTop = 0;
					barrelArcLeft = 0;
					barrelArcRight = 0;
					topLast = new Vector(currentX - topBarrelX, currentY
							- topBarrelY);
				}
			}
			leftLast = leftCurrent;
			rightLast = rightCurrent;
			break;
		case 3:
			// check only the top barrel

			if (barrelLeftCompleted && barrelRightCompleted) {
				topCurrent = new Vector(currentX - topBarrelX, currentY
						- topBarrelY);
				topAngle = Vector.signedAngleBetween(topCurrent, topLast);
/*
				// DEBUG
				canvas.drawLine(currentX, currentY, topBarrelX, topBarrelY,
						debugPaint);
				canvas.drawLine(lastX, lastY, topBarrelX, topBarrelY,
						debugPaint2);
				writeme = Double.toString(barrelArcTop);
				canvas.drawText(writeme, 0, writeme.length(), topBarrelX,
						topBarrelY, debugPaint);
				// DEBUG
				 * 
				 */
				if (!Double.isNaN(topAngle)) {
					barrelArcTop += topAngle;
				}
				if (Math.abs(barrelArcTop) > 270) {
					// OMG WE JUST NEED TO GO TO THE GATE..
					barrelTopCompleted = true;
					currentBarrel++;
					// set up for the next step
					// TODO THIS
				}
			}
			topLast = topCurrent;
			break;
		case 4:

			// check if they go between the two barrels when they are above the
			// line of barrels
			// canvas.drawLine(0, height, width / 5 * 2, height, tempPaint);
			// canvas.drawLine(width / 5 * 3, height, width, height, tempPaint);
			// check if they go between the gate
			/*
			canvas.drawLine(width / 5 * 2, height - 150, width / 5 * 3,
					height - 150, debugPaint3);
					*/
			// if the player is above the line of the barrels
			if (currentY >= height - (radius * 1.5)) {
				// if the player is between the two barrels
				if (currentX > width / 5 * 2 && currentX < width / 5 * 3) {
					// NO WAY THEY FREAKING DID IT!
					// END THE GAME PLEAES
					currentBarrel = 0;
					barrelTopCompleted = false;
					barrelLeftCompleted = false;
					barrelRightCompleted = false;
					finish();
				}
			}
		}
		lastX = currentX;
		lastY = currentY;
	}




	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public void start(View view) {
		if (gameLoop != null) {
			if (gameLoop.isRunning()) {
				return;
			}
		}
		
		currentBarrel = 0;
		barrelTopCompleted = false;
		barrelLeftCompleted = false;
		barrelRightCompleted = false;
		elapsedTime = 0;
		
		timerTask = new TimerTask() {
			@Override
			public void run() {
				clockHandler.post(new Runnable() {
					@Override
					public void run() {
						if (!isPaused) {
							setTheTime(convertMillisToMMSSmm(
									elapsedTime + timer.getElapsedTime())
									.toString());
							Log.d("Paused Time", Long.toString(elapsedTime));
						}
					}
				});
			}
		};
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(findViewById(R.id.username).getWindowToken(), 0);
		gameLoop = new GameLoop();
		gameLoop.start();
		// egImpl.setCurrentX(Xval);
		// egImpl.setCurrentY(Yval);
		egImpl = new ExampleImplementation(getApplicationContext());
		egImplT = new Thread(egImpl);
		egImplT.start();
		timer = new Stopwatch();
		timer.start();
		clockTimer.scheduleAtFixedRate(timerTask, 0, 100);
		isPaused = true;
		pause(homeView);
		// egImpl.setToStartingLine();

	}

	public void pause(View view) {
		if (isPaused) {
			
			//wait 5 seconds
			
			timer = new Stopwatch();
			timer.start();
		} else {
			elapsedTime += timer.getElapsedTime();
			timer = new Stopwatch();
			timer.start();
		}
		egImpl.pause(!isPaused);
		isPaused = !isPaused;

	}

	public void finish() {

		gameLoop.pleaseStop();
		pause(homeView);
		// timerTask.cancel();
		egImpl.pleaseStop();
		EditText user = (EditText) findViewById(R.id.username);
		username = user.getText().toString();
		scoretime = Long.toString(elapsedTime + timer.getElapsedTime());
		addItem(new ScoreManagerContent.Score(username, scoretime));
		FullscreenActivity.file.save();
		
		Intent launchNewIntent = new Intent(this, ScoreActivity.class);
		startActivity(launchNewIntent);

		/*
		 * AlertDialog.Builder alert = new
		 * AlertDialog.Builder(FullscreenActivity.this);
		 * 
		 * alert.setTitle("Title"); alert.setMessage("Message");
		 * 
		 * // Set an EditText view to get user input final EditText input = new
		 * EditText(this); alert.setView(input);
		 * 
		 * alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int whichButton) { String
		 * value = input.getText().toString(); // Do something with value! } });
		 * 
		 * alert.setNegativeButton("Cancel", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int whichButton) { // Canceled. } });
		 * 
		 * alert.show(); Log.d("show alert","running");
		 * 
		 * 
		 * 
		 * final EditText input = new EditText(this); new
		 * AlertDialog.Builder(FullscreenActivity.this) .setTitle("Great Job!")
		 * .setMessage("Enter a username") .setView(input)
		 * .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int whichButton) {
		 * Editable value = input.getText(); String test = value.toString(); if
		 * (test.isEmpty()) { username = "User"; // add User as name if user
		 * does // not care to enter a name } else { username =
		 * value.toString(); } addItem(new ScoreManagerContent.Score(username,
		 * scoretime)); FullscreenActivity.file.save(); } }).show();
		 * .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int whichButton) { // Do
		 * nothing. } }).show();
		 */
	}

	public void setTheTime(String string) {
		this.timeTextView.setText(string);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// draw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gameLoop = null;
	}

	private class GameLoop extends Thread {
		private volatile boolean running = true;

		public void run() {
			while (running) {
				draw();
			}
		}

		public void pleaseStop() {

			running = false;
			interrupt();
		}

		public boolean isRunning() {
			return running;
		}
	}

	private static class Vector {
		public float x;
		public float y;

		public Vector(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public Vector add(Vector v) {
			return new Vector(v.x + this.x, v.y + this.y);
		}

		public Vector mult(Vector v) {
			return new Vector(v.x * this.x, v.y * this.y);
		}

		public double magnitude() {
			return Math.sqrt(this.x * this.x + this.y * this.y);
		}

		public static double dot(Vector u, Vector v) {
			return u.x * v.x + u.y * v.y;
		}

		public static double signedAngleBetween(Vector u, Vector v) {
			double tempAngle = ((180 / Math.PI) * (Math.atan2(v.y, v.x) - Math
					.atan2(u.y, u.x)));
			// 300 degree change shouldn't be reachable unless we're rolling
			// over, because
			// at that point, you would have to be on the barrel, and have
			// already lost.
			if (tempAngle <= -300) {
				tempAngle += 360;
			} else if (tempAngle >= 300) {
				tempAngle -= 360;
			}
			return tempAngle;

		}
	}
}
