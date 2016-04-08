package com.whirzl.mmc;

/*=============================================================================
 XY Activity Class
 ==============================================================================*/
/* This activity creates an interactive XY style interface which can be used to 
 * play a pre-loaded XY patch from the application browser. The interface 
 * responds to single-touch sending XY co-ordinates to the patch. It also 
 * provides visual feedback in the form of a changing colour background.
 */

/*---------------------- Importing Java Classes -------------------------------*/

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

/*--------------------------- XY Class ----------------------------------------*/


public class XY extends Activity implements OnTouchListener {

	private static final int SAMPLE_RATE = 44100;
	MySurfaceView mySurfaceView;
	float x, y;
	private static final String TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Make the view full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// the mySurfaceview variable setting up the view
		mySurfaceView = new MySurfaceView(this, null);
		mySurfaceView.setOnTouchListener(this);

		setContentView(mySurfaceView);

	}

	/*-------------------------- onDestroy() -------------------------------*/

	
	@Override
	protected void onDestroy() {
		// cleanup() method also called with onDestroy()
		cleanup();
		super.onDestroy();
	}
	

	/*------------------------- onPause() ----------------------------------*/
	
	
	@Override
	protected void onPause() {
		// stopAudio() method automatically run when in onPause() method
		PdAudio.stopAudio();
		mySurfaceView.pause();
		super.onPause();
	}
	

	/*------------------------ cleanup() -----------------------------------*/

	
	private void cleanup() {
		// All resources released when cleanup() method is called
		PdAudio.stopAudio();
		PdBase.release();
	}

	
	/*----------------------- onResume() ----------------------------------*/

	
	@Override
	protected void onResume() {
		mySurfaceView.resume();
		super.onResume();

		/*
		 * Tests that the sample rate is at least the rate defined in thefinal
		 * SR variable
		 */
		if (AudioParameters.suggestSampleRate() < SAMPLE_RATE) {
			toast("required sample rate not available; exiting");
			finish();
			return;
		}
		// Checking for suggested output channels 2
		int nOut = Math.min(AudioParameters.suggestOutputChannels(), 2);
		if (nOut == 0) {

			/*
			 * If no channels are available toast displays string and runsthe
			 * finish() method
			 */
			toast("audio output not available; exiting");
			finish();
			return;
		}

		/*
		 * Initializing Pd audio - SR, no.input channels,output chnls,pd ticks
		 * (1 chosen for minimal latency - up to 64 in pd)
		 */
		try {
			PdAudio.initAudio(SAMPLE_RATE, 0, nOut, 1, true);
			PdAudio.startAudio(this);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	

	/*------------------------ onTouch() -----------------------------------*/

	
	
	@Override
	public boolean onTouch(View v, MotionEvent me) {

		// switch/case to handle user touch screen gestures
		switch (me.getAction()) {

		// PdBase.sendbang sending "start' message on ACTION_DOWN
		case MotionEvent.ACTION_DOWN:
			PdBase.sendBang("start");

		// ACTION_MOVE used to capture XY screen positions
		case MotionEvent.ACTION_MOVE:

			// Retreiving X and Y coordinates of touch screen gestures
			x = me.getX();
			y = me.getY();

			// Logging touch screen events in Logcat
			Log.i("MotionEvent", "X = " + x + "Y = " + y);
			break;
		}
		if (me.getAction() == MotionEvent.ACTION_MOVE) {

			/*
			 * sendFloat() method takes constant float values from the x and y
			 * variables and sends them to Pd on "channels" labelled "Xpos" and
			 * "Ypos" to be received by [rYpos] and [rXpos] objects in Pd patch.
			 */
			PdBase.sendFloat("Ypos", y);
			PdBase.sendFloat("Xpos", x);

			/*
			 * Background colour is continuously changed while the user is
			 * touching screen(x and y data re-used to vary the colours)
			 */
			mySurfaceView.setBackgroundColor((int) (0xff000000 + x / 2 * 0x10000 + y / 4 * 0x100));
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {

			// Sending a "stop" message to Pd on ACTION_UP
			PdBase.sendBang("stop");
		}
		return true;

	}

	private void toast(String string) {
		// Todo
	}
	

	/*------------------------ finish() ------------------------------------*/

	
	
	@Override
	public void finish() {
		cleanup();
		super.finish();
	}

	
	/*----------------------- MySurfaceView Class ---------------------------------*/

	
	
	public class MySurfaceView extends SurfaceView implements Runnable {

		// Initialising the SurfaceHolder and Thread for XY background
		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;

		public MySurfaceView(Context context, AttributeSet attrs) {
			super(context, attrs);
			ourHolder = getHolder();

		}

		public void pause() {
			isRunning = false;
			while (true) {
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}

		public void resume() {

			// isRunning set to true when resume method is called
			isRunning = true;

			// Thread finally starts here
			ourThread = new Thread(this);
			ourThread.start();

		}
		

	/*------------------------- run() --------------------------------------*/
		
		
		
		@Override
		public void run() {
			while (isRunning) {
				if (!ourHolder.getSurface().isValid())
					continue;

				// Creates the canvas on which to run the background behaviour
				Canvas canvas = ourHolder.lockCanvas();

				// Initialising the canvas to default XY background colour
				canvas.drawColor(Color.argb(200, 50, 70, 100));

				// Displays the canvas
				ourHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
