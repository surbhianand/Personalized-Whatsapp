package com.example.heya;

import java.util.ArrayList;

import java.util.Locale;

import com.example.heya.messageshandling.SendMessageAsync;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
public class MainActivity extends ActionBarActivity {

	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("hi","hi");
		setContentView(R.layout.activity_new_record);
         Log.e("1", "1");
		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		Log.e("2", "2");
		// hide the action bar
		//getActionBar().hide();
		Log.e("3", "3");
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		Log.e("onclicklistener", "onclicklistener");
				promptSpeechInput();
			}
		});

	}

	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Log.e("prompt","promt");
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		Log.e("pe1", "pe1");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		Log.e("pe2", "pe2");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
		Log.e("pe3", "pe3");
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		Log.e("pe4", "pe4");
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Log.i(result.get(0),result.get(0));
				//txtSpeechInput.setText(result.get(0));
				new SendMessageAsync(getApplicationContext(),
						SendMessageAsync.TYPE_TEXT).execute(result.get(0), false);
			finish();	
			}
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
