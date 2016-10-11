package com.example.mobileplayer.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;
import com.example.mobileplayer.utils.Utils;

public class AudioPlayerActivity extends BaseActivity {

	private AnimationDrawable rocketAnimation;
	/**
	 * 代表了服务
	 * */
	private MediaService service;
	private Music music;
	private int postion;
	private TextView tv_artist;
	private TextView tv_name;
	private TextView tv_time;
	private SeekBar seekbar_audio;
	private Button btn_play_mod;
	private Button btn_pre;
	private Button btn_play_pause;
	private Button btn_next;
	private Button btn_lyric;
	
	private Utils utils;
	
	private boolean isPlaying;
	private MyBroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		utils = new Utils();
		//注册监听准备好的广播
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(MediaService.PREPAREED_MESSAGE);
		receiver = new MyBroadcastReceiver();
		registerReceiver(receiver, filter );
	}
	
	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			setViewStatus();
		}
		
	}
	private void initView() {
		setTitle("本地音乐");
		setRightButton(View.GONE);
		
		ImageView rocketImage = (ImageView) findViewById(R.id.iv_icon);
		rocketImage.setBackgroundResource(R.drawable.animation_list);
		tv_artist = (TextView) findViewById(R.id.tv_artist);
		tv_name = (TextView) findViewById(R.id.tv_name);
		seekbar_audio = (SeekBar) findViewById(R.id.seekbar_audio);
		//btn_play_mod = (Button) findViewById(R.id.btn_play_mod);
		btn_pre = (Button) findViewById(R.id.btn_pre);
		btn_play_pause = (Button) findViewById(R.id.btn_play_pause);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_lyric = (Button) findViewById(R.id.btn_lyric);
		rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		rocketAnimation.start();
	}
	/**
	 * 设置View的状态
	 */
	public void setViewStatus() {
		tv_artist.setText(music.getArtist());
		tv_name.setText(music.getTitle());
		tv_time.setText(utils.stringForTime(music.getCurrentPosition())+"/"+utils.stringForTime(music.getDuration()));
		seekbar_audio.setMax(music.getDuration());
	}
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(getApplicationContext(), R.layout.activity_audioplayer, null);
	}

	@Override
	public void rightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

}
