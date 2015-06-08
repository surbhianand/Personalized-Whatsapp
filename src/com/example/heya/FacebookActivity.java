package com.example.heya;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.heya.extendables.SideBarActivity;


public class FacebookActivity extends Activity{

	private WebView mWebview ;
	//private RelativeLayout face;
	//Intent intent = null;
//	@SuppressWarnings("rawtypes")
	//Class nextActivity = null;
	//private HookUpDialog mExitAppDialog;
	//


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		//face = (RelativeLayout) findViewById(R.id.activity_main);
		

	//	setSideBar(getString(R.string.WALL));

    }
  /*  public boolean onKeyDown(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		intent = new Intent(FacebookActivity.this,
					RecentActivityActivity.class);
			nextActivity = RecentActivityActivity.class;
    		return true;
    	}
    	
    	return super.onKeyDown(keyCode, event);
    	}*/
	
	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mSideBarOpened) {
				closeSideBar();
				return true;
			} else {
				mExitAppDialog.show();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}*/
	@Override
	//public void onBackPressed() {
		//openSideBar();
		/*if (mWebview.getVisibility() == View.VISIBLE) {
			mWebview.setVisibility(View.GONE);
			
			
		}
		else if(face.getVisibility() == View.VISIBLE)
		{
			face.setVisibility(View.GONE);

		}
		else {
			super.onBackPressed();
		}*/
//	}
    //private class JS{
    //	public void hide(String id)
    	//{
    		//setContentView(R.layout.activity_main);
    	//}
    //}
	
	public void onBackPressed() {
		Intent intent=new Intent(FacebookActivity.this,RecentActivityActivity.class);
		startActivity(intent);
}

    
    public  void sefDestruct(View view) {
    	 mWebview  = new WebView(this);

         mWebview.getSettings().setJavaScriptEnabled(true); 
       //  mWebview.addJavascriptInterface(new JS(), "callback");
         mWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);// enable javascript
        mWebview.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
         final Activity activity = this;

         mWebview.setWebViewClient(new WebViewClient() {
        	/* public boolean shouldOverrideUrlLoading (WebView view, String url){
        		 setContentView(R.layout.activity_main);
        		 Log.e("hell","o");
        		 return false;
        	 }*/
             public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                 Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                 
             }
         });

         mWebview .loadUrl("https://www.facebook.com/dialog/send?app_id=1603415546555914&display=popup&e2e=%7B%7D&link=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2Freference%2Fdialogs%2F&locale=en_US&name=Facebook%20Dialogs&next=http%3A%2F%2Fstatic.ak.facebook.com%2Fconnect%2Fxd_arbiter%2FrFG58m7xAig.js%3Fversion%3D41%23cb%3Df17c71dee8%26domain%3Dwww.dracowane.me%26origin%3Dhttp%253A%252F%252Fwww.dracowane.me%252Ff2b134529c%26relation%3Dopener%26frame%3Df2537f26b%26result%3D%2522xxRESULTTOKENxx%2522&sdk=joey");
         setContentView(mWebview );
    }
    public void done(View view)
    {
     PackageManager pm=getPackageManager();
    	    try {

    	        Intent waIntent = new Intent(Intent.ACTION_SEND);
    	        waIntent.setType("text/plain");
    	        String text = "Hey,its me using HEYOU.... love it";

    	        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
    	        //Check if package exists or not. If not then code 
    	        //in catch block will be called
    	        waIntent.setPackage("com.whatsapp");

    	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
    	            startActivity(Intent.createChooser(waIntent, "Share with"));

    	   } catch (NameNotFoundException e) {
    	        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
    	                .show();
    	   }  
    	   
   /* 	Uri mUri = Uri.parse("smsto:+8410437022");
    	Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
    	 mIntent.setType("text/plain");
    	mIntent.setPackage("com.whatsapp");
    	mIntent.putExtra("sms_body", "The text goes here");
    	mIntent.putExtra("chat",true);
    	startActivity(mIntent);*/
    }
    /*public void done2(View view)
    {
   
    	 Intent sendIntent = new Intent();
     	sendIntent.setAction(Intent.ACTION_SEND);
     	sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey,its me using HEYOU.... love it");
     	sendIntent.setType("text/plain");
     	startActivity(sendIntent);
    }*/
    public void donehike(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("com.bsb.hike", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("com.bsb.hike");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Hike not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
    	
    }
    
    public void doneline(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("jp.naver.line.android", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("jp.naver.line.android");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Line not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
    	
    }
    public void donetelegram(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("org.telegram.messenger", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("org.telegram.messenger");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Telegram not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
	    
    	
    }
    public void donegmail(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("com.google.android.gm", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("com.google.android.gm");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Telegram not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
    }
    public void donememo(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("com.android.memo", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("com.android.memo");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Memo not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
    }

    public void donemessages(View view)
    {
    	PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Hey,its me using HEYOU.... love it";

	        PackageInfo info=pm.getPackageInfo("com.android.mms", PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage("com.android.mms");

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(waIntent);

	   } catch (NameNotFoundException e) {       Toast.makeText(this, "Messages not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  
    }

}
