/*
`ž * The MIT License (MIT)
 * 
 * Copyright � 2013 Clover Studio Ltd. All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.heya;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.opencv.samples.fd.MainActivity;
import org.opencv.samples.fd.FdActivity;

import android.speech.tts.TextToSpeech;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heya.adapters.WallMessagesAdapter;
import com.example.heya.couchdb.CouchDB;
import com.example.heya.couchdb.ResultListener;
import com.example.heya.couchdb.SpikaAsyncTask;
import com.example.heya.dialog.DeleteMessageDialog;
import com.example.heya.dialog.HookUpAlertDialog;
import com.example.heya.dialog.HookUpAlertDialog.ButtonType;
import com.example.heya.dialog.TempVideoChooseDialog;
import com.example.heya.extendables.SideBarActivity;
import com.example.heya.lazy.Emoticons;
import com.example.heya.management.SettingsManager;
import com.example.heya.management.TimeMeasurer;
import com.example.heya.management.UsersManagement;
import com.example.heya.messageshandling.MessagesUpdater;
import com.example.heya.messageshandling.SendMessageAsync;
import com.example.heya.messageshandling.UpdateMessagesInListView;
import com.example.heya.messageshandling.WallScrollListener;
import com.example.heya.model.Emoticon;
import com.example.heya.model.Group;
import com.example.heya.model.Message;
import com.example.heya.model.User;
import com.example.heya.model.WatchingGroupLog;
import com.example.heya.utils.Const;
import com.example.heya.view.EmoticonsLayout;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

//import translatorpkg.TranslateExample;
//import translator.*;
/**
 * WallActivity
 * 
 * Displays a list of messages in private wall or other user/group wall; has
 * options for sending text, photo, video, voice, location and emoticon message.
 */

public class DuplicateWallActivity extends SideBarActivity implements OnClickListener,TextToSpeech.OnInitListener{

	public static ListView gLvWallMessages;
	public static WallMessagesAdapter gMessagesAdapter;
	public static ArrayList<Message> gCurrentMessages = null;
	public static ArrayList<Message> gNewMessages = null;
	public static boolean gIsRefreshUserProfile = false;
	public static boolean gIsVisible = false;
	private Activity mActivity;
	public Message m;
	public static UpdateMessagesInListView gUpdater = null;
	public static MessagesUpdater gMessagesUpdater = null;

	private Button mBtnCamera;
	private Button mBtnGallery;
	private Button mBtnVideo;
	private Button mBtnAutoEmoticons;
	private Button mBtnEmoji;
	protected Button mBtnLocation;
	private Button mBtnRecord;
	private Button mBtnWallSend;
	private Button mBtnAudioText;
	
	
	//private Button btnSpeak;
	private ImageButton mBtnOpenSlidingDrawer;
	private EditText mEtMessageText;
	private SlidingDrawer mSlidingDrawer;
	private RelativeLayout mRlBottom;

	private RelativeLayout.LayoutParams mParamsOpened;
	private RelativeLayout.LayoutParams mParamsClosed;

	private Intent mProfileIntent;
	private LinearLayout mButtonsLayout;
	private EmoticonsLayout mEmoticonsLayout;
	private HorizontalScrollView mScrollViewEmoticons;
    public String language="english";
	private static DuplicateWallActivity sInstance = null;

	private static final int REQUEST_CODE_FROM_LOCATION = 1001;
	
	private TextToSpeech tts;
	
	private static final int OPENED = 1003;
	private static final int CLOSED = 1004;
	MainActivity ac;

	private TempVideoChooseDialog mChooseDialog;
	private TextView mTvNoMessages;

	private View mViewDeletedGroup;

	private boolean mPushHandledOnNewIntent = false;
	private HookUpAlertDialog mDeletedGroupDialog;

	private ReloadTimerTask timerTask;
	private Timer timer;
	String text;
	
	/* @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
			Toast.makeText(this,"OnActivityResult Called",Toast.LENGTH_LONG).show();
		//	super.onActivityResult(requestCode, resultCode, data);
			
			 super.onActivityResult(requestCode, resultCode, data);
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
				
				    
			
		}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("content","view0");
		super.onCreate(savedInstanceState);
		Log.e("content","view1");
		sInstance = this;
		Log.e("content","view2");
		tts = new TextToSpeech(this, this);
		Log.e("content","view3");
		setContentView(R.layout.activity_wall);
		Log.e("content","view4");
		setSideBar(getString(R.string.WALL));
		Log.e("duplicate","here");
		ac=new MainActivity();
		Log.e("ac.id2",ac.id2);
		mEtMessageText.setText(ac.id2);
		initialization();
		
		onClickListeners();
		mBtnWallSend.setOnClickListener(this);
	
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mPushHandledOnNewIntent = false;
		if (getIntent().getBooleanExtra(Const.PUSH_INTENT, false)) {
			mPushHandledOnNewIntent = true;
			intent.removeExtra(Const.PUSH_INTENT);
			openWallFromNotification(intent);
		}
		setWatchingGroupLog();
		super.onNewIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.e("RESUME", "TIMERS!");
		timerTask = new ReloadTimerTask();
		timer = new Timer();
		timer.schedule(timerTask, 5 * 1000, 5 * 1000);
		
		if (checkTimersIfReloadNeeded()) {
			return;
		}
		Log.i("on resume","here");
		if (!mPushHandledOnNewIntent) {
			if (getIntent().getBooleanExtra(Const.PUSH_INTENT, false)) {
				mPushHandledOnNewIntent = false;
				openWallFromNotification(getIntent());
				getIntent().removeExtra(Const.PUSH_INTENT);
			}
		}

		if (mSlidingDrawer.isOpened()) {
			setSlidingDrawer(CLOSED);
		}

		if (SpikaApp.hasNetworkConnection()) {

			refreshWallMessages();
			setWatchingGroupLog();
			checkIfGroupIsDeleted();
			setTitle();

		}
	}

	private boolean checkTimersIfReloadNeeded () {
		int myUTC = (int) (System.currentTimeMillis() / 1000);
		
		if (gCurrentMessages != null) {
			for (Message message : gCurrentMessages) {
				if (message.getDelete() != 0) {
					if (message.getDelete() < myUTC) {
						Log.e("*** RELOAD ***", " " + myUTC + " " + message.getDelete());
						MessagesUpdater.reload();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void setTitle() {

		if (UsersManagement.getToUser() != null) {
			mTvTitle.setText(UsersManagement.getToUser().getName());
		} else if (UsersManagement.getToGroup() != null) {
			mTvTitle.setText(UsersManagement.getToGroup().getName());
		} else {
			mTvTitle.setText(getString(R.string.WALL));
		}
	}

	private void setWatchingGroupLog() {
		String watchingGroupId = SpikaApp.getPreferences()
				.getWatchingGroupId();

		User loginUser = UsersManagement.getLoginUser();
		Group group = UsersManagement.getToGroup();

		boolean alreadyHasWatchingGroup = !watchingGroupId.equals("");

		if (alreadyHasWatchingGroup) {
			CouchDB.deleteWatchingGroupLogAsync(SpikaApp.getPreferences()
					.getWatchingGroupId(), SpikaApp.getPreferences()
					.getWatchingGroupRev(), new DeleteWatchingLogFinish(), DuplicateWallActivity.this, false);
			SpikaApp.getPreferences().setWatchingGroupId("");
			SpikaApp.getPreferences().setWatchingGroupRev("");
		}

		if (group != null) {
			boolean isInFavorites = loginUser.isInFavoriteGroups(group);
			if (!isInFavorites) {
				WatchingGroupLog watchingGroupLog = new WatchingGroupLog();
				watchingGroupLog.setGroupId(UsersManagement.getToGroup()
						.getId());
				watchingGroupLog.setUserId(UsersManagement.getLoginUser()
						.getId());
				CouchDB.createWatchingGroupLogAsync(watchingGroupLog, new CreateWatchingLogFinish(), DuplicateWallActivity.this, false);
			}
		}
	}

	private boolean isGroupDeleted() {
		if (UsersManagement.getToGroup() != null) {
			return (UsersManagement.getToGroup().isDeleted());
		}
		return false;
	}

	private void checkIfGroupIsDeleted() {
		if (isGroupDeleted()) {
			if (mDeletedGroupDialog == null) {
				mDeletedGroupDialog = new HookUpAlertDialog(this);
				mDeletedGroupDialog
						.show(getString(R.string.this_group_is_deleted_please_unsubscribe),
								ButtonType.CLOSE);
			}
			mViewDeletedGroup.setVisibility(View.VISIBLE);
		} else {
			mViewDeletedGroup.setVisibility(View.GONE);
		}
	}

	private void openWallFromNotification(Intent intent) {

		String fromUserId = intent.getStringExtra(Const.PUSH_FROM_USER_ID);
		String fromType = intent.getStringExtra(Const.PUSH_FROM_TYPE);

		User fromUser = null;
		Group fromGroup = null;

		try {
//			fromUser = new GetUserByIdAsync(this).execute(fromUserId).get();
			 
			fromUser = new SpikaAsyncTask<Void, Void, User>(new CouchDB.FindUserById(fromUserId), null, DuplicateWallActivity.this, true).execute().get();
			if (fromType.equals(Const.PUSH_TYPE_GROUP)) {
				String fromGroupId = intent
						.getStringExtra(Const.PUSH_FROM_GROUP_ID);
				
//				fromGroup = new GetGroupByIdAsync(this).execute(fromGroupId)
//						.get();
				
				fromGroup = new SpikaAsyncTask<Void, Void, Group>(new CouchDB.FindGroupById(fromGroupId), null, DuplicateWallActivity.this, true).execute().get();
				
				UsersManagement.setToGroup(fromGroup);
				UsersManagement.setToUser(null);
				SettingsManager.ResetSettings();
				if (DuplicateWallActivity.gCurrentMessages != null) {
					DuplicateWallActivity.gCurrentMessages.clear();
				}

			}
			if (fromType.equals(Const.PUSH_TYPE_USER)) {

				UsersManagement.setToUser(fromUser);
				UsersManagement.setToGroup(null);
				SettingsManager.ResetSettings();
				if (DuplicateWallActivity.gCurrentMessages != null) {
					DuplicateWallActivity.gCurrentMessages.clear();
				}
				DuplicateWallActivity.gIsRefreshUserProfile = true;

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
//	private class OpenWallFromNotification implements Command<>

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			gIsVisible = true;
		} else {
			gIsVisible = false;
		}
	}

	@Override
	protected void enableViews() {
		super.enableViews();
		gLvWallMessages.setEnabled(true);
		mBtnOpenSlidingDrawer.setEnabled(true);
	}

	@Override
	protected void disableViews() {
		super.disableViews();
		if (gLvWallMessages != null)
			gLvWallMessages.setEnabled(false);
		if (mSlidingDrawer.isOpened()) {
			setSlidingDrawer(CLOSED);
		}
		mBtnOpenSlidingDrawer.setEnabled(false);
		hideKeyboard();
	}

	public static DuplicateWallActivity getInstance() {
		return sInstance;
	}

	@Override
	protected void refreshWallMessages() {
		super.refreshWallMessages();

		TimeMeasurer.start();

		if (gUpdater == null || gMessagesUpdater == null) {
			gUpdater = new UpdateMessagesInListView(this);
		}
		MessagesUpdater.update(true);
		if (gMessagesAdapter != null) {
			gMessagesAdapter.notifyDataSetChanged();
		}
		if (gIsRefreshUserProfile) {
			setWallMessages();
			gIsRefreshUserProfile = false;
		}
	}

	public void checkMessagesCount() {
		if (gCurrentMessages.size() > 0) {
			mTvNoMessages.setVisibility(View.GONE);
		} else {
			mTvNoMessages.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void setObjectsNull() {
		gCurrentMessages = null;
		gUpdater = null;
		gMessagesUpdater = null;
		unbindDrawables(findViewById(R.id.galleryEmoticons));
		mEmoticonsLayout.removeAllViews();
		mEmoticonsLayout = null;
		sInstance = null;
		gLvWallMessages = null;
		gMessagesAdapter = null;
		mScrollViewEmoticons = null;
		mButtonsLayout.removeAllViews();
		mButtonsLayout = null;
		super.setObjectsNull();
	}

	private void onClickListeners(){

		/*Translate.setClientId("translator_example_55");
        Translate.setClientSecret("oeny70esl3M0zLdaduYCX3eBjRydhLlGQQTAPxE5tIY=");
        
        // From French -> English 
        String translatedText = Translate.execute("Bonjour le monde", Language.FRENCH, Language.ENGLISH);
        Systeme.out.println("French -> English : " + translatedText);
        
        Toast.makeText(getApplicationContext(),translatedText ,
				   Toast.LENGTH_LONG).show();*/
		


		mBtnOpenSlidingDrawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mSlidingDrawer.isOpened()) {
					setSlidingDrawer(CLOSED);
				} else {
					setSlidingDrawer(OPENED);
				}

			}
		});
		
		

		mEtMessageText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (mSlidingDrawer.isOpened()) {
					setSlidingDrawer(CLOSED);
				}
				smoothScrollToBottom();
			}
		});

		mEtMessageText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				smoothScrollToBottom();
			}
		});
		
		/*btnSpeak.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
						   Toast.LENGTH_LONG).show();
				
				speakOut();
				
				
			}
			
		   
			public void onInit(int status) {
				// TODO Auto-generated method stub

				if (status == TextToSpeech.SUCCESS) {

					int result = tts.setLanguage(Locale.US);

					// tts.setPitch(5); // set pitch level

					// tts.setSpeechRate(2); // set speech speed rate

					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.e("TTS", "Language is not supported");
					} else {
						btnSpeak.setEnabled(true);
						speakOut();
					}

				} else {
					Log.e("TTS", "Initilization Failed");
				}

			}

			
			private void speakOut() {

				String text = mEtMessageText.getText().toString();

				tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				
				setSlidingDrawer(CLOSED);
				new SendMessageAsync(getApplicationContext(),
						SendMessageAsync.TYPE_TEXT).execute(text, false);
			}

		});*/
		
		
		
		mBtnCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(DuplicateWallActivity.this,
						CameraCropActivity.class);
				intent.putExtra("type", "camera");
				DuplicateWallActivity.this.startActivity(intent);

			}
		});

		mBtnEmoji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setEmoticonsLayout(OPENED);

			}
		});
		
		

		mBtnGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(DuplicateWallActivity.this,
						CameraCropActivity.class);
				intent.putExtra("type", "gallery");
				DuplicateWallActivity.this.startActivity(intent);

			}
		});

		
		mBtnRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("hi", "hi");
				startActivity(new Intent(DuplicateWallActivity.this,
						MainActivity.class));
			}
		});

		mBtnVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mChooseDialog.show();
			}
		});
		
		mBtnAutoEmoticons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DuplicateWallActivity.this, NewActivity.class);
		        startActivity(intent);
			}
		});
		
		
		
		mBtnLocation.setOnClickListener(new OnClickListener(){
            
			@Override
			   public void onClick(View v) {
			    LayoutInflater layoutInflater 
			     = (LayoutInflater)getBaseContext()
			      .getSystemService(LAYOUT_INFLATER_SERVICE);  
			    View popupView = layoutInflater.inflate(R.layout.popup, null);  
			             final PopupWindow popupWindow = new PopupWindow(
			               popupView, 
			               LayoutParams.WRAP_CONTENT,  
			                     LayoutParams.WRAP_CONTENT);  
			             
			             Button ok = (Button)popupView.findViewById(R.id.ok);
			         	ok.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             Button german = (Button)popupView.findViewById(R.id.german);
			         	german.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             Button spanish = (Button)popupView.findViewById(R.id.spanish);
			         	spanish.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             Button french = (Button)popupView.findViewById(R.id.french);
			         	french.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             Button arabic = (Button)popupView.findViewById(R.id.arabic);
			         	arabic.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             Button hindi = (Button)popupView.findViewById(R.id.hindi);;
			         	hindi.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
			             ok.setOnClickListener(new Button.OnClickListener(){

			     @Override
			     public void onClick(View v) {
			      // TODO Auto-generated method stub
			      popupWindow.dismiss();
			     }});
			             german.setOnClickListener(new Button.OnClickListener(){

						     @Override
						     public void onClick(View v) {
						      // TODO Auto-generated method stub
						     language="german";
						     }});
			             french.setOnClickListener(new Button.OnClickListener(){

						     @Override
						     public void onClick(View v) {
						      // TODO Auto-generated method stub
						    	 language="french";
						     }});
			             hindi.setOnClickListener(new Button.OnClickListener(){

						     @Override
						     public void onClick(View v) {
						      // TODO Auto-generated method stub
						    	 language="hindi";
						     }});
			             arabic.setOnClickListener(new Button.OnClickListener(){

						     @Override
						     public void onClick(View v) {
						      // TODO Auto-generated method stub
						    	 language="arabic";
						     }});
			             spanish.setOnClickListener(new Button.OnClickListener(){

						     @Override
						     public void onClick(View v) {
						      // TODO Auto-generated method stub
						    	 language="spanish";
						     }});
			               
			             popupWindow.showAsDropDown(mBtnLocation,50,100);
			         
			   }
			
			
	});
}


	private void smoothScrollToBottom() {
		if (gLvWallMessages != null && gMessagesAdapter != null
				&& gMessagesAdapter.getCount() > 1) {
			gLvWallMessages
					.smoothScrollToPosition(gMessagesAdapter.getCount() - 1);
		}
	}
	
	
	private void initialization() {

		mTvNoMessages = (TextView) findViewById(R.id.tvNoMessages);
		mBtnOpenSlidingDrawer = (ImageButton) findViewById(R.id.btnSlideButton);
		mBtnWallSend = (Button) findViewById(R.id.btnWallSend);
		mBtnWallSend.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
		mEtMessageText = (EditText) findViewById(R.id.etWallMessage);
		mEtMessageText.setTypeface(SpikaApp.getTfMyriadPro());
		mSlidingDrawer = (SlidingDrawer) findViewById(R.id.slDrawer);
		mButtonsLayout = (LinearLayout) findViewById(R.id.llButtonsLayout);
		mEmoticonsLayout = (EmoticonsLayout) findViewById(R.id.galleryEmoticons);
		mScrollViewEmoticons = (HorizontalScrollView) findViewById(R.id.svEmoticons);
		mRlBottom = (RelativeLayout) findViewById(R.id.rlBottom);
		mBtnCamera = (Button) findViewById(R.id.btnCamera);
		mBtnGallery = (Button) findViewById(R.id.btnGallery);
		mBtnVideo = (Button) findViewById(R.id.btnVideo);
		mBtnAutoEmoticons = (Button) findViewById(R.id.btnautoemoticons);
		mBtnEmoji = (Button) findViewById(R.id.btnEmoji);
		mBtnAudioText = (Button) findViewById(R.id.btnaudiotext);
		mBtnLocation = (Button) findViewById(R.id.btnLocation);
		mBtnRecord = (Button) findViewById(R.id.btnRecord);
		mBtnAudioText = (Button) findViewById(R.id.btnaudiotext);
		
		mBtnWallSend.setTypeface(SpikaApp.getTfMyriadProBold(), Typeface.BOLD);
		mViewDeletedGroup = (View) findViewById(R.id.viewDeletedGroup);

		mParamsClosed = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mParamsClosed.leftMargin = 2;
		mParamsOpened = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mParamsOpened.leftMargin = 2;
		mParamsClosed.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mParamsOpened.addRule(RelativeLayout.ABOVE, mSlidingDrawer.getId());

		setWallMessages();

		mProfileIntent = new Intent(DuplicateWallActivity.this, MyProfileActivity.class);
		mProfileIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

		CouchDB.findAllEmoticonsAsync(new FindAllEmoticonsFinish(), DuplicateWallActivity.this, true);

		mChooseDialog = new TempVideoChooseDialog(this);
		mChooseDialog.setOnButtonClickListener(
				TempVideoChooseDialog.BUTTON_CAMERA, new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent recordVideoIntent = new Intent(
								DuplicateWallActivity.this, RecordingVideoActivity.class);
						recordVideoIntent.putExtra("camera", true);
						startActivity(recordVideoIntent);
						mChooseDialog.dismiss();

					}
				});

		mChooseDialog.setOnButtonClickListener(
				TempVideoChooseDialog.BUTTON_GALLERY, new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent recordVideoIntent = new Intent(
								DuplicateWallActivity.this, RecordingVideoActivity.class);
						recordVideoIntent.putExtra("gallery", true);
						startActivity(recordVideoIntent);
						mChooseDialog.dismiss();

					}
				});

		mChooseDialog.setCancelable(true);

	}

	public void setWallMessages() {
		if (gCurrentMessages == null) {
			gCurrentMessages = new ArrayList<Message>();
		}
		gLvWallMessages = (ListView) findViewById(R.id.lvWallMessages);
		gLvWallMessages.setOnScrollListener(new WallScrollListener());
		gMessagesAdapter = new WallMessagesAdapter(this, gCurrentMessages);
		gLvWallMessages.setAdapter(gMessagesAdapter);
	}

	public void resetBoard() {
		DuplicateWallActivity.gUpdater = null;
		DuplicateWallActivity.gMessagesUpdater = null;
		gIsRefreshUserProfile = true;
		gCurrentMessages = new ArrayList<Message>();
	}

	public static void redirect(Context context) {
		Intent intent = new Intent(context, DuplicateWallActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (emoticonsLayoutIsOpened()) {
			setEmoticonsLayout(CLOSED);
		} else if (mSlidingDrawer.isOpened()) {
			setSlidingDrawer(CLOSED);
		} else {
			super.onBackPressed();
		}
	}

	private class FindAllEmoticonsFinish implements ResultListener<List<Emoticon>> {

		@Override
		public void onResultsSucceded(List<Emoticon> emoticons) {
			if (emoticons != null) {
				Emoticons.getInstance().setEmoticons(emoticons);
			}
			fillEmoticonsGallery(emoticons);
		}
		@Override
		public void onResultsFail() {			
		}
	}

	private class CreateWatchingLogFinish implements ResultListener<String> {

		@Override
		public void onResultsSucceded(String result) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onResultsFail() {
			// TODO Auto-generated method stub
		}
	}
	
	private class DeleteWatchingLogFinish implements ResultListener<String> {

		@Override
		public void onResultsSucceded(String result) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onResultsFail() {
			// TODO Auto-generated method stub
		}
	}

	private void fillEmoticonsGallery(final List<Emoticon> emoticons) {

		if (mEmoticonsLayout != null) {
			for (int i = 0; i < emoticons.size(); i = i + 2) {
				if (i + 1 < emoticons.size()) {
					mEmoticonsLayout
							.add(emoticons.get(i), emoticons.get(i + 1));
				} else {
					mEmoticonsLayout.add(emoticons.get(i), null);
				}
			}
		}
	}

	/**
	 * Sends message type "emoticon" to CouchDB and closes the sliding drawer
	 * 
	 * @param Emoticon
	 */
	public void sendEmoticon(Emoticon emoticon) {
		if (mSlidingDrawer.isOpened()) {
			setSlidingDrawer(CLOSED);
		}
		new SendMessageAsync(getApplicationContext(),
				SendMessageAsync.TYPE_EMOTICON).execute(emoticon, false);
	}

	private void setSlidingDrawer(int state) {
		switch (state) {
		case OPENED:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEtMessageText.getWindowToken(), 0);
			mEtMessageText.clearFocus();
			mBtnOpenSlidingDrawer
					.setImageResource(R.drawable.hide_more_button_selector);
			mSlidingDrawer.open();
			mRlBottom.setLayoutParams(mParamsOpened);
			mButtonsLayout.setVisibility(View.VISIBLE);
			mScrollViewEmoticons.setVisibility(View.GONE);
			break;
		case CLOSED:
			mBtnOpenSlidingDrawer
					.setImageResource(R.drawable.more_button_selector);
			mSlidingDrawer.close();
			mRlBottom.setLayoutParams(mParamsClosed);
			break;
		default:
			break;
		}
	}

	private void setEmoticonsLayout(int state) {
		switch (state) {
		case OPENED:
			mButtonsLayout.setVisibility(View.GONE);
			mScrollViewEmoticons.setVisibility(View.VISIBLE);
			break;
		case CLOSED:
			mScrollViewEmoticons.setVisibility(View.GONE);
			mButtonsLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private boolean emoticonsLayoutIsOpened() {
		if (mScrollViewEmoticons.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	private class ReloadTimerTask extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	checkTimersIfReloadNeeded();
	            }
			});
		}
	}
	public String translate( String body ) throws Exception {
        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("translator_example_55");
        Translate.setClientSecret("oeny70esl3M0zLdaduYCX3eBjRydhLlGQQTAPxE5tIY=");
        String translatedText ="";
        System.out.println(body.toString());
        // From French -> English 
        if(language=="french")
        translatedText = Translate.execute(body.toString(),Language.FRENCH);
        else if(language=="spanish")
            translatedText = Translate.execute(body.toString(),Language.SPANISH);
        else if(language=="hindi")
            translatedText = Translate.execute(body.toString(),Language.HINDI);
        else if(language=="arabic")
            translatedText = Translate.execute(body.toString(),Language.ARABIC);
        else if(language=="german")
            translatedText = Translate.execute(body.toString(),Language.GERMAN);
        else if(language=="english")
            translatedText = Translate.execute(body.toString(),Language.ENGLISH);
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
	protected void onPause() {
		super.onPause();
		
		Log.e("PAUSE", "TIMERS!");
		
		if (timerTask != null) timerTask.cancel();
		if (timer != null) timer.cancel();
	}

	@Override
	public void onClick(View v) {
		class bgStuff extends AsyncTask<Void, Void, Void>{
		            
		            String translatedText = "";
		            @Override
		            protected Void doInBackground(Void... params) {
		                // TODO Auto-generated method stub
		                try {
		                	String body = mEtMessageText.getText().toString();
		                    //String text = ((EditText) findViewById(R.id.editText1)).getText().toString();
		                    translatedText = translate(body);
		                	
		                } catch (Exception e) {
		                    // TODO Auto-generated catch block
		                    e.printStackTrace();
		                    translatedText = e.toString();
		                }
		                 
		                return null;
		            }

		            @Override
		            protected void onPostExecute(Void result) {
		                // TODO Auto-generated method stub
		                //((TextView) findViewById(R.id.text1)).setText(translatedText);
		            	//text=translatedText;
		            	System.out.print(translatedText);
		            	if (!translatedText.equals("") && !mSideBarOpened) {
						mEtMessageText.setText("");
						Log.i("recording","testing");
						setSlidingDrawer(CLOSED);
						new SendMessageAsync(getApplicationContext(),
								SendMessageAsync.TYPE_TEXT).execute(translatedText, false);
					}
		                super.onPostExecute(result);
		            }
		             
		        }
		         
		        new bgStuff().execute();
		    


		        
				/*if (!text.equals("") && !mSideBarOpened) {
					mEtMessageText.setText("");
					setSlidingDrawer(CLOSED);
					new SendMessageAsync(getApplicationContext(),
							SendMessageAsync.TYPE_TEXT).execute(text, false);
				}*/
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}

	
}
