package com.whirzl.mmc;

/*=============================================================================
 Browse Activity Class
 ==============================================================================*/
/* This activity presents a list of the files on a chosen directory on the 
 * sdcard and shows the name of the file when clicked.*/

/*----------------------Importing Java Classes--------------------------------*/

//import java.io.File;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;




/*----------------------- Browser Activity Class -------------------------------*/

public class Browse extends ListActivity {

	// String initialised for the 'catch exceptions' in initPd() method
	private static final String TAG = null;

	// String arrays to store list item names and paths
	private List<String> item = null;
	private List<String> path = null;

	// String to store the default patch directory
	private String patchDir = "/sdcard/pd/patches/";

	/*
	 * The TextView object to display default directory path at the top of the
	 * List View
	 */
	private TextView myPath;
	
	

/*------------------------ onCreate() ---------------------------------*/

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		// Simple text view to display the actual path to the patches directory
		myPath = (TextView) findViewById(R.id.path);

		// Initializes getDir() and passes path string stores in patchDir
		getDir(patchDir);

	}
	
	

/*------------------------- getDir() ----------------------------------*/

	
	private void getDir(String dPath) {

		// Displays the path at top of list view
		myPath.setText("Location: " + dPath);

		// new File variable 'f' to hold a raw list of all files in directory
		File f = new File(dPath);
		File[] files = f.listFiles();

		// Initializing item and path arrays
		item = new ArrayList<String>();
		path = new ArrayList<String>();

		// if(!dPath.equals(patchDir)) // list everything in the patchDir but
		// not the patchDir itself??

		{
			// Adding the data in patchDir to the item and path ArrayLists
			item.add(patchDir);
			path.add(patchDir);

			// Optional for including sub-directories in the list view
			/*
			 * item.add("../"); path.add(f.getParent());
			 */

		}

		// 'for' loop to get path and names of each file in path and item arrays
		for (int i = 0; i < files.length; i++) {
			File file = files[i];

			path.add(file.getPath());
			item.add(file.getName());

		}

		// Using ArrayAdapter to prepare fileList for display in the List View
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.list_row, item);

		// Setting the ListAdapter to display the files in the List View
		setListAdapter(fileList);
	}
	
	

/*---------------------- onListItemClicked() ---------------------------*/

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		/*
		 * Initializing a new File variable to store position in array of the
		 * list item that has been clicked
		 */
		File file = new File(path.get(position));

		// Using gethpath() method to pass file location to 'filename' string
		String filename = file.getPath();
		Log.i(file.getPath(), filename);

		// Calling initPd() and passing the file stored in 'filename' string
		initPd(filename);

	}
	

/*-------------------------- initPd() ----------------------------------*/

	
	private void initPd(String filename) {

		try {

			/*
			 * Passing the patch stored in the filename variable toopenPatch()
			 * method
			 */
			PdBase.openPatch(new File("", filename));

		} catch (IOException e) {

			Log.e(TAG, e.toString() + "; exiting now");
			finish();
		}

		// Intent to launch the XY Interface Class
		Intent XY = new Intent(this, XY.class);
		startActivity(XY);

	}
}
