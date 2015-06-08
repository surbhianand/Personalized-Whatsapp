package com.example.heya;

import org.opencv.samples.fd.FdActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
public class NewActivity extends ActionBarActivity {
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
			//Toast.makeText(this,"OnActivityResult Called",Toast.LENGTH_LONG).show();
		//	super.onActivityResult(requestCode, resultCode, data);
			    
			 super.onActivityResult(requestCode, resultCode, data);
			 //Log.i("newactivity","newactivity");
			 finish();
			 //Toast.makeText(NewActivity.this, "new acgtivtiy", Toast.LENGTH_LONG).show();
			 //Log.i("new finish","new finish");
			 Intent i=new Intent(NewActivity.this,DuplicateSplashScreen.class);
			 startActivity(i);
			// String result=data.getStringExtra("data");
			// Log.i("print",result);
			// setContentView(R.layout.activity_wall);
			/*String str=data.getExtras().getString("data");
			
			System.out.print(str);
   	if (!str.equals("") && !mSideBarOpened) {
			mEtMessageText.setText(str);
			Log.i("recording","testing");
			setSlidingDrawer(CLOSED);
			new SendMessageAsync(getApplicationContext(),
					SendMessageAsync.TYPE_TEXT).execute(str, false);
		}*/
			/* Bundle extras = data.getExtras();
			 String value1 = extras.getString(Intent.EXTRA_TEXT);
			 if (value1 != null) {	 
				 Log.i("surbhi","value1");
			 } */
			 
			/* ac=new MainActivity(); 
			 Log.i("check",ac.id);
			 onResume();
			 */
		/* new SendMessageAsync(getApplicationContext(),
						SendMessageAsync.TYPE_TEXT).execute(ac.id, false);	
		*/		    
			
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Log.i("oncreate", "new");
		Intent intent = new Intent(NewActivity.this,
				FdActivity.class);
		//Log.i("intent","new");
		 startActivityForResult(intent,109);
		// Log.i("jkdjk","fbdj");
		
	}

}
