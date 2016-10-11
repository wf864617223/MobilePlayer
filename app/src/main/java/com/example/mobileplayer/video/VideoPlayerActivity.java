package com.example.mobileplayer.video;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;
import com.example.mobileplayer.domain.VideoItem;
import com.example.mobileplayer.utils.Utils;
import com.example.mobileplayer.view.VideoView;
/**
 * 使用系统api写的视频播放器，使用手机硬件解码
 * @author MI
 *
 */
public class VideoPlayerActivity extends BaseActivity {

	
	protected static final int PROGRESS = 1;
	protected static final int DELAYED_HIDECONTROLPLAYER = 2;
	private static final int FULL_SCREEN = 3;
	private static final int DEFAULT_SCREEN = 4;
	private VideoView videoview;
	//视频播放地址
	private Uri uri;
	private TextView tv_video_title;
	private ImageView iv_battery;
	private TextView tv_system_time;
	private Button btn_voice;
	private SeekBar seekbar_voice;
	private Button btn_maxvoice;
	
	private TextView tv_current_time;
	private SeekBar seekbar_video;
	private TextView tv_duration;
	private Button btn_exit;
	private Button btn_pre;
	private Button btn_play_pause;
	private Button btn_next;
	private Button btn_screen;
	private LinearLayout ll_control_player;
	private LinearLayout ll_loading;
	
	private boolean isPlaying = false;
	private MyBroadcastReceiver receiver;
	private int level;
	private ArrayList<VideoItem> videoItems;
	private int position;
	private GestureDetector detector;
	private Utils utils;
	private boolean isDestroyed =  false;
	private boolean isShowControl = false;
	private WindowManager wm;
	private int screenWidth;
	private int screenHeight;
	private AudioManager am;
	private int currentVolume;
	private int maxVolume;
	private boolean isMute = false;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PROGRESS:
				int currentPosition = videoview.getCurrentPosition();
				tv_current_time.setText(utils.stringForTime(currentPosition));
				seekbar_video.setProgress(currentPosition);
				
				setBattery();
				
				tv_system_time.setText(utils.getSystemTime());
				
				if(!isDestroyed){
					handler.removeMessages(PROGRESS);
					handler.sendEmptyMessageDelayed(PROGRESS, 1000);
				}
				
				break;
			case DELAYED_HIDECONTROLPLAYER:
				hideControlPlayer();
				break;

			default:
				break;
			}
		}

		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		initData();
		initView();
		
		getData();
		setData();
		setListener();
		//videoview.setMediaController(new MediaController(this));
	}
	private void setData() {

		if(videoItems != null&& videoItems.size()>0){
			VideoItem videoItem = videoItems.get(position);
			
			videoview.setVideoPath(videoItem.getData());
			
			tv_video_title.setText(videoItem.getTitle());
		}else if(uri != null){
			videoview.setVideoURI(uri);
			tv_video_title.setText(uri.toString());
			
			btn_pre.setBackgroundResource(R.drawable.btn_pre_selector);
			btn_pre.setEnabled(false);
			
			btn_next.setBackgroundResource(R.drawable.btn_next_selector);
			btn_next.setEnabled(false);
		}
		seekbar_voice.setMax(maxVolume);
		seekbar_voice.setProgress(currentVolume);
		
	}
	@SuppressWarnings("unchecked")
	private void getData() {
		
		videoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra("videolist");
		position = getIntent().getIntExtra("position", 0);
		
		uri = getIntent().getData();
		
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initData() {
		utils = new Utils();
		isDestroyed = false;
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		receiver = new MyBroadcastReceiver();
		registerReceiver(receiver, filter);
		
		detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

			@SuppressLint("NewApi")
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				
				//Toast.makeText(getApplicationContext(), "双击屏幕", 0).show();
				if(isFullScreen){
					setVideoType(DEFAULT_SCREEN);
				}else{
					setVideoType(FULL_SCREEN);
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				
				//Toast.makeText(getApplicationContext(), "长按屏幕", 0).show();
				startOrPause(); 
				super.onLongPress(e);
			}

			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
		
				//Toast.makeText(getApplicationContext(), "单击屏幕", 0).show();
				if(isShowControl){
					removeDelayedHideoControlPlayer();
					hideControlPlayer();
				}else{
					showControlPlayer();
					sendDelayedHideoControlPlayer();
				}
				return true;
			}
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				float mOldX = e1.getX();
				float mOldY = e2.getY();
				Display disp = getWindowManager().getDefaultDisplay();
				int windowWidth = disp.getWidth();
				int windowHeight = disp.getHeight();
				int pos = videoview.getCurrentPosition();
				if(Math.abs(distanceX)>=Math.abs(distanceY)){
					if(Math.abs(distanceX)>Math.abs(distanceY)){
						onVideoSpeed(distanceX);
					}
				}else{
					onVolumeSlide(e2);
				}
				return super.onScroll(e1, e2, distanceX, distanceY);
			}

			
			
		});
		am = (AudioManager) getSystemService(AUDIO_SERVICE);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
	
	}
	private boolean sendDelayedHideoControlPlayer() {
		return handler.sendEmptyMessageDelayed(DELAYED_HIDECONTROLPLAYER, 3000);
	}
	protected void removeDelayedHideoControlPlayer() {

		handler.removeMessages(DELAYED_HIDECONTROLPLAYER);
	}
	private float startY;
	private float audioTouchRang;
	private int mVol;
	/*
	 * 手势识别(non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		 super.onTouchEvent(event);
		detector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			removeDelayedHideoControlPlayer();
			startY = event.getY();
			audioTouchRang = Math.min(screenHeight, screenWidth);
			mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			break;
		case MotionEvent.ACTION_MOVE:
			//onVolumeSlide(event);
			break;
		case MotionEvent.ACTION_UP:
			sendDelayedHideoControlPlayer();
			break;
		default:
			break;
		}
		return true;
	}
	/*
	 *快进与快退
	 */
	private void onVideoSpeed(float x){
		int currentPosition = videoview.getCurrentPosition();
		if(x < 0){
			currentPosition += 1000;
			videoview.seekTo(currentPosition);
			setProgress(currentPosition);
		}else if(x > 0){
			currentPosition -=1000;
			videoview.seekTo(currentPosition);
			setProgress(currentPosition);
		}
	}
	/*
	 * 滑动屏幕音量的变化
	 */
	private void onVolumeSlide(MotionEvent event) {
		float endY = event.getY();
		float distanceY = startY - endY;
		float date1 = distanceY/audioTouchRang;
		float volume = distanceY/audioTouchRang*maxVolume;
		
		float volemeS = Math.min(Math.max(volume+mVol, 0), maxVolume);
		if(date1!=0){
			updataVolume((int)volemeS);
		}
	}
	/*
	 *电量状态变化
	 */
	private void setBattery() {

		if(level<=0){
			iv_battery.setImageResource(R.drawable.ele_0);
		}else if(level<=20){
			iv_battery.setImageResource(R.drawable.ele_20);
		}else if(level<=40){
			iv_battery.setImageResource(R.drawable.ele_40);
		}else if(level<=60){
			iv_battery.setImageResource(R.drawable.ele_60);
		}else if(level<=80){
			iv_battery.setImageResource(R.drawable.ele_80);
		}else if(level<=100){
			iv_battery.setImageResource(R.drawable.ele_100);
		}
	};
	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
		
			level = intent.getIntExtra("level", 0);
		}
		
	}
	@Override
	protected void onDestroy() {
	
		super.onDestroy();
		isDestroyed = true;
		
		unregisterReceiver(receiver);
		receiver = null;
	}
	private void setListener() {
		
		btn_play_pause.setOnClickListener(mOnClickListener);
		btn_next.setOnClickListener(mOnClickListener);
		btn_pre.setOnClickListener(mOnClickListener);
		btn_screen.setOnClickListener(mOnClickListener);
		btn_voice.setOnClickListener(mOnClickListener);
		btn_exit.setOnClickListener(mOnClickListener);
		btn_maxvoice.setOnClickListener(mOnClickListener);
		seekbar_video.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
				sendDelayedHideoControlPlayer();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				removeDelayedHideoControlPlayer();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				if(fromUser){
					videoview.seekTo(progress);
				}
			}
		});
		
		seekbar_voice.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				sendDelayedHideoControlPlayer();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				removeDelayedHideoControlPlayer();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				if(fromUser){
					updataVolume(progress);
				}
			}
		});
		
		videoview.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
			
				videoview.start();
				isPlaying = true;
				
				setVideoType(DEFAULT_SCREEN);			
				
				int duration = videoview.getDuration();
				tv_duration.setText(utils.stringForTime(duration));
				
				seekbar_video.setMax(duration);
				
				hideControlPlayer();
				
				ll_loading.setVisibility(View.GONE);
				
				handler.sendEmptyMessage(PROGRESS);
			}
		});
		videoview.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
			
				playNextVideo();
			}
		});
		videoview.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				
				Toast.makeText(getApplicationContext(), "无法播放此视频！", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}
	protected void updataVolume(int volume) {
		
		if(isMute){
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			seekbar_voice.setProgress(0);
		}else{
			am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
			seekbar_voice.setProgress(volume);
		}
		currentVolume = volume;
		
	}
	/*
	 * 变成系统最大声音
	 */
	protected void maxVoice(int volume){
		 am.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume , 0);
	}
	OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			removeDelayedHideoControlPlayer();
			sendDelayedHideoControlPlayer();
			switch (v.getId()) {
			case R.id.btn_play_pause:
				
				startOrPause();
				break;
			case R.id.btn_next:
				playNextVideo();
				break;
			case R.id.btn_pre:
				playPreVideo();
				break;
			case R.id.btn_screen:
				if(isFullScreen){
					setVideoType(DEFAULT_SCREEN);
				}else{
					setVideoType(FULL_SCREEN);
				}
				break;
			case R.id.btn_voice:
				isMute = !isMute;
				updataVolume(currentVolume);
				break;
			case R.id.btn_exit:
				finish();
				break;
			case R.id.btn_maxvoice:
		
				//Toast.makeText(getApplicationContext(), "clicked", 0).show();
				maxVoice(currentVolume);
				seekbar_voice.setProgress(maxVolume);
				break;
			default:
				break;
			}
			
		}
	} ;
	private void initView() {
		//设置标题栏隐藏
		setTitleBar(View.GONE);
		videoview = (VideoView) findViewById(R.id.videoview);
		
		tv_video_title = (TextView) findViewById(R.id.tv_video_title);
		iv_battery = (ImageView) findViewById(R.id.iv_battery);
		tv_system_time = (TextView) findViewById(R.id.tv_system_time);
		
		btn_voice = (Button) findViewById(R.id.btn_voice);
		seekbar_voice = (SeekBar) findViewById(R.id.seekbar_voice);
		btn_maxvoice = (Button) findViewById(R.id.btn_maxvoice);
		
		tv_current_time = (TextView) findViewById(R.id.tv_current_time);
		seekbar_video = (SeekBar) findViewById(R.id.seekbar_video);
		tv_duration = (TextView) findViewById(R.id.tv_duration);
		
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_pre = (Button) findViewById(R.id.btn_pre);
		btn_play_pause = (Button) findViewById(R.id.btn_play_pause);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_screen = (Button) findViewById(R.id.btn_screen);
		ll_control_player = (LinearLayout) findViewById(R.id.ll_control_player);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
	}
	@Override
	public View setContentView() {
		
		
		return View.inflate(this, R.layout.activity_videoplayer, null);
	}

	@Override
	public void rightButtonClick() {


	}

	@Override
	public void leftButtonClick() {
	

	}
	/*
	 * 播放按钮的变色
	 */
	private void setPlayOrPauseStatus() {
		if(position == 0){
			btn_pre.setBackgroundResource(R.drawable.button_grey_rew);
			btn_pre.setEnabled(true);
		}else if(position == videoItems.size()-1){
			btn_next.setBackgroundResource(R.drawable.button_grey_ffw);
			btn_next.setEnabled(true);
		}else{
			btn_pre.setBackgroundResource(R.drawable.btn_pre_selector);
			btn_pre.setEnabled(true);
			
			btn_next.setBackgroundResource(R.drawable.btn_next_selector);
			btn_next.setEnabled(true);
		}
	}
	/*
	 * 点击 下一个 按钮时的事件
	 */
	private void playNextVideo() {
		if(videoItems != null && videoItems.size()>0){
			position++;
			
				if(position < videoItems.size()){
					VideoItem videoItem = videoItems.get(position);
					videoview.setVideoPath(videoItem.getData());
					
					tv_video_title.setText(videoItem.getTitle());
					setPlayOrPauseStatus();
				}else{
					position = videoItems.size()-1;
					Toast.makeText(getApplicationContext(), "This is last video", Toast.LENGTH_SHORT).show();
					finish();
				}
			
		}else if(uri != null){
			Toast.makeText(getApplicationContext(), "it's only one video", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	/*
	 * 点击播放上一个的事件
	 */
	private void playPreVideo() {
		if(videoItems != null && videoItems.size()>0){
			position--;
			
				if(position >= 0){
					VideoItem videoItem = videoItems.get(position);
					videoview.setVideoPath(videoItem.getData());
					
					tv_video_title.setText(videoItem.getTitle());
					setPlayOrPauseStatus();
				}else{
					position = 0;
					Toast.makeText(getApplicationContext(), "This is first video", Toast.LENGTH_SHORT).show();
					
				}
			
		}
	}
	/*
	 * 使控件栏消失
	 */
	private void hideControlPlayer(){
		ll_control_player.setVisibility(View.GONE);
		isShowControl = false;
	}
	/*
	 * 显示控件栏
	 */
	private void showControlPlayer(){
		ll_control_player.setVisibility(View.VISIBLE);
		isShowControl = true;
	}
	/*
	 * 播放 暂停 功能
	 */
	private void startOrPause() {
		if(isPlaying){
			videoview.pause();
			btn_play_pause.setBackgroundResource(R.drawable.btn_play_selector);
		}else{
			videoview.start();
			btn_play_pause.setBackgroundResource(R.drawable.btn_pause_selector);
		}
		isPlaying = !isPlaying;
	}
	//默认不全屏
	private boolean isFullScreen = false;
	/*
	 * 设置播放状态 是否全屏
	 */
	private void setVideoType(int type){
		switch (type) {
		case FULL_SCREEN:
			videoview.setVideoSize(screenWidth,screenHeight);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = true;
			
			btn_screen.setBackgroundResource(R.drawable.btn_default_full_selector);
			break;
		case DEFAULT_SCREEN:
			int mVideoWidth = videoview.getVideoWidth();
			int mVideoHeight = videoview.getVideoHeight();
			
			int width = screenWidth;
			int height = screenHeight;
			if(mVideoWidth > 0&&mVideoHeight > 0){
				if(mVideoWidth * height >width*mVideoHeight){
					height = width*mVideoHeight/mVideoWidth;
				}else if(mVideoWidth * height < width*mVideoHeight){
					width = height*mVideoWidth/mVideoHeight;
				}else{
					
				}
			}
			videoview.setVideoSize(width, height);
			
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = false;
			btn_screen.setBackgroundResource(R.drawable.btn_full_selector);
			break;
		default:
			break;
		}
	}
	

}
