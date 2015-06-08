package com.example.heya.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.heya.R;
import com.example.heya.couchdb.CouchDB;
import com.example.heya.couchdb.ResultListener;
import com.example.heya.messageshandling.MessagesUpdater;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class DeleteMessageDialog extends Dialog implements View.OnClickListener,TextToSpeech.OnInitListener {

	Activity Context;
	private ToggleButton [] deleteButtons;
	private Button closeDialog;
	private String messagebody = null;
	private String messageId = null;
	private int deleteType = 0;
	private int oldDeleteType = 0;
	private TextToSpeech tts;
    private String text;
	Context context;
	
	public DeleteMessageDialog(Activity activity, String messageId, int oldDeleteType,String messageBody) {
		super(activity, android.R.style.Theme_Translucent_NoTitleBar);
		setContentView(R.layout.dialog_delete_message);
				
		context = activity;
		tts = new TextToSpeech(activity, this);
		this.messageId = messageId;
		this.oldDeleteType = oldDeleteType;
		this.deleteType = oldDeleteType;
		
		deleteButtons = new ToggleButton[8];
		
		deleteButtons[0] = (ToggleButton) findViewById(R.id.dont_delete);
		deleteButtons[1] = (ToggleButton) findViewById(R.id.delete_now);
		deleteButtons[2] = (ToggleButton) findViewById(R.id.delete_after_5_min);
		deleteButtons[3] = (ToggleButton) findViewById(R.id.delete_after_day);
		deleteButtons[4] = (ToggleButton) findViewById(R.id.delete_after_week);
		deleteButtons[5] = (ToggleButton) findViewById(R.id.delete_after_read);
		deleteButtons[6] = (ToggleButton) findViewById(R.id.Text_to_speech);
		//deleteButtons[7] = (ToggleButton) findViewById(R.id.Audio_diff_lang);
		deleteButtons[7] = (ToggleButton) findViewById(R.id.Audio_diff_lang);

		closeDialog = (Button) findViewById(R.id.close_dialog);
		messagebody = messageBody;
		for (Button button : deleteButtons) {
			button.setOnClickListener(this);
		}
		
		deleteButtons[oldDeleteType].setChecked(true);
		
		closeDialog.setOnClickListener(this);
	}


	
	@Override
	public void onClick(View v) {
		if (v.equals(closeDialog)) {
			if(deleteButtons[6].isChecked())
			{
				
			//Log.e("Hope for the best", messagebody);
			// Api call
			 text =messagebody;

			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			DeleteMessageDialog.this.dismiss();
			return ;
			}
			
		if(deleteButtons[7].isChecked())
			{ text =messagebody;
              class bgStuff extends AsyncTask<Void, Void, Void>{
            	 
		            String translatedText = "";
		            
		            @Override
		            protected Void doInBackground(Void... params) {
		                // TODO Auto-generated method stub
		                try {
		                	
		                    //String text = ((EditText) findViewById(R.id.editText1)).getText().toString();
		                    translatedText = translate(text);
		                    
		                    Log.e("Hope for the best", translatedText);
		                } catch (Exception e) {
		                	Log.e("Hope for the best","sss");
		                    // TODO Auto-generated catch block
		                    e.printStackTrace();
		                    translatedText = e.toString();
		                }
		                 
		                return null;
		            }

		            public String translate( String body ) throws Exception {
		                // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
		                Translate.setClientId("translator_example_55");
		                Translate.setClientSecret("oeny70esl3M0zLdaduYCX3eBjRydhLlGQQTAPxE5tIY=");
		                String translatedText ="";
		                System.out.println(body.toString());
		                // From French -> English 
		                translatedText = Translate.execute(body.toString(),Language.FRENCH);
		                //System.out.println("French -> English : " + translatedText);
		                System.out.println(translatedText.toString());
		                // From English -> French - AUTO_DETECT the From Language
		               /* translatedText = Translate.execute("Hello world!",Language.FRENCH);
		                //System.out.println("English AUTO_DETECT -> French: " + translatedText);
		                
		                // English AUTO_DETECT -> Arabic
		                translatedText = Translate.execute("Hello world, how are you doing?",Language.ARABIC);
		                //System.out.println("English AUTO_DETECT -> Arabic: " + translatedText);*/
		                return translatedText;
		        	}
					@Override
		            protected void onPostExecute(Void result) {
		                // TODO Auto-generated method stub
		                //((TextView) findViewById(R.id.text1)).setText(translatedText);
		            	//text=translatedText;
		            	System.out.print(translatedText);
		            	if (!translatedText.equals("")) {
		            		tts.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null);
		        			DeleteMessageDialog.this.dismiss();
		        			return ;
					}
		                super.onPostExecute(result);
		            }
		             
		        }
		         
		        new bgStuff().execute();
		    


				
				
				
				
			}
			
			
			
			else if (oldDeleteType != deleteType) {
				callApi(deleteType);
			}
			DeleteMessageDialog.this.dismiss();
		}
		else {
			for (int i = 0; i < deleteButtons.length ; i++) {
				if (v.equals(deleteButtons[i])) {
					deleteType = i;
					deleteButtons[i].setChecked(true);
				}
				else {
					deleteButtons[i].setChecked(false);
				}
			}
		}
	}
	
	
	void callApi (int deleteType) {
		CouchDB.deleteMessageAsync(messageId, String.valueOf(deleteType), new DeleteResultListener(), context, true);
	}

	class DeleteResultListener implements ResultListener<Void> {

		@Override
		public void onResultsSucceded(Void result) {
			Log.e("*** API ***", "return success");
			MessagesUpdater.reload();
			
		}

		@Override
		public void onResultsFail() {
			Log.e("*** API ***", "return fail");
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
		
	}
}
