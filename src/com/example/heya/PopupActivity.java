package com.example.heya;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopupActivity extends WallActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
      
    LayoutInflater layoutInflater 
     = (LayoutInflater)getBaseContext()
      .getSystemService(LAYOUT_INFLATER_SERVICE);  
    
    View popupView = layoutInflater.inflate(R.layout.popup,
            null);


    final PopupWindow popupWindow = new PopupWindow(popupView,
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, true);



    popupWindow.setTouchable(true);
    popupWindow.setFocusable(true);

    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
}