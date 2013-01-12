package com.whirzl.mmc;

/*==========================================================================
 Main_Menu Activity Class
 ===========================================================================*/
/* This activity launches the main menu screen of the Whirzl application.
 * It provides 2 buttons for basic navigation; a button to launch the patch 
 * browser and a button to display a simple help widget. A further inflater
 * menu, containing 'About' and a second 'Help' option, is available via the 
 * soft menu button on the device itself. 
 */
/*---------------------- Importing Java Classes -----------------------------*/

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




/*------------------------ Main_Menu Activity -------------------------------*/



public class Main_Menu extends Activity implements OnClickListener {
	// private static final AlertDialog dialogBuilder = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Creating the initial View with the main.xml
		setContentView(R.layout.main);

		// Initializing the buttons in the view to listen for clicks
		Button a = (Button) findViewById(R.id.button1);
		a.setOnClickListener((OnClickListener) this);
		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener((OnClickListener) this);
	}
	
	

/*------------------------- onCreate() ------------------------------*/

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Creates an menu accessed from the devices soft menu button
		MenuInflater inflater = getMenuInflater();

		// Sets contents of inflate widgets to items defined in whirzl_menu.xml
		inflater.inflate(R.layout.whirzl_menu, menu);
		return true;
	}
	

/*------------------- onOptionsItemSelected() -----------------------*/

	
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);

		// swith/case to handle clicks from inflated menu items
		switch (item.getItemId()) {
		//
		case R.id.about_item:

			// Setting the title and message of the about widget
			ad.setTitle(R.string.about_title);
			ad.setMessage(R.string.about_msg);
			break;

		// Setting the title and message of the help widget
		case R.id.help_item:
			ad.setTitle(R.string.help_title);
			ad.setMessage(R.string.help_msg);
			break;

		default:
			break;
		}

		// Simple 'ok' button to close widget
		ad.setNeutralButton(android.R.string.ok, null);
		ad.setCancelable(true);
		ad.show();
		return true;
	}
	
	

/*----------------------- onClickView() ----------------------------*/

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// if/else statements to link button clicks to intents
		if (v.getId() == (R.id.button1)) {
			Intent a = new Intent(this, Browse.class);

			// Launching class passed to intent (a)
			startActivity(a);

		} else if (v.getId() == (R.id.button2)) {
			Intent b = new Intent(this, Help.class);
			// Launching class passed to intent (b)
			startActivity(b);
		}
	}
}
