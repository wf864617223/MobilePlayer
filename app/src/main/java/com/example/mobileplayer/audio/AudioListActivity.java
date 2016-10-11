package com.example.mobileplayer.audio;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;
import com.example.mobileplayer.utils.HandlerManager;
import com.example.mobileplayer.utils.MediaUtil;
import com.example.mobileplayer.utils.PromptManager;

public class AudioListActivity extends BaseActivity {

	/************ 资源加载 ****************/
	private ListView songListView;
	private MySongListAdapter songAdapter;
	private ScanSdFilesReceiver scanReceiver;
	private Button btn_audio_mode;
	private Button btn_audio_pre;
	private Button btn_audio_play_pause;
	private Button btn_audio_next;
	private Button btn_audio_menu;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantValue.STARTED:
				// 开始刷新播放列表界面
				PromptManager.showProgressDialog(AudioListActivity.this);
				break;
			case ConstantValue.FINISHED:
				// 结束刷新播放列表界面
				MediaUtil.getInstacen().initMusics(AudioListActivity.this);
				PromptManager.closeProgressDialog();
				songAdapter.notifyDataSetChanged();
				unregisterReceiver(scanReceiver);
				break;
			case ConstantValue.PLAY_END:
				// 播放完成
				// 播放模式：单曲循环、顺序播放、循环播放、随机播放
				// 单曲循环:记录当前播放位置
				// 顺序播放:当前播放位置上＋1
				// 循环播放:判断如果，增加的结果大于songList的大小，修改播放位置为零
				// 随机播放:Random.nextInt() songList.size();

				MediaUtil.CURRENTPOS++;

				if (MediaUtil.CURRENTPOS < MediaUtil.getInstacen()
						.getSongList().size()) {
					Music music = MediaUtil.getInstacen().getSongList()
							.get(MediaUtil.CURRENTPOS);
					startPlayService(music, ConstantValue.OPTION_PLAY);
					changeNotice(Color.GREEN);

				}

				break;
			}
		};
	};
	private void changeNotice(int color) {
		TextView tx = (TextView) songListView
				.findViewWithTag(MediaUtil.CURRENTPOS);
		if (tx != null) {
			tx.setTextColor(color);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("本地音乐");
		
		HandlerManager.putHandler(handler);
		init();
		setLintener();
	}
	private void setLintener() {
		btn_audio_play_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (MediaUtil.PLAYSTATE) {
				case ConstantValue.OPTION_PLAY:
				case ConstantValue.OPTION_CONTINUE:
					startPlayService(null, ConstantValue.OPTION_PAUSE);
					btn_audio_play_pause.setBackgroundResource(R.drawable.btn_audio_play_selector);
					break;
				case ConstantValue.OPTION_PAUSE:
					if (MediaUtil.CURRENTPOS >= 0
							&& MediaUtil.CURRENTPOS < MediaUtil.getInstacen()
									.getSongList().size()) {
						startPlayService(MediaUtil.getInstacen().getSongList()
								.get(MediaUtil.CURRENTPOS),
								ConstantValue.OPTION_CONTINUE);
						btn_audio_play_pause
								.setBackgroundResource(R.drawable.btn_audio_pause_selector);

					}
					break;
				}
			}
		});
		btn_audio_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MediaUtil.getInstacen().getSongList().size() > MediaUtil.CURRENTPOS + 1) {
					changeNotice(Color.WHITE);
					MediaUtil.CURRENTPOS++;
					startPlayService(
							MediaUtil.getInstacen().getSongList()
									.get(MediaUtil.CURRENTPOS),
							ConstantValue.OPTION_PLAY);
					btn_audio_play_pause.setBackgroundResource(R.drawable.btn_now_playing_pause);
					MediaUtil.PLAYSTATE = ConstantValue.OPTION_PLAY;
					changeNotice(Color.GREEN);
				}

			}
		});
		btn_audio_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MediaUtil.CURRENTPOS > 0) {
					changeNotice(Color.WHITE);
					MediaUtil.CURRENTPOS--;
					startPlayService(
							MediaUtil.getInstacen().getSongList()
									.get(MediaUtil.CURRENTPOS),
							ConstantValue.OPTION_PLAY);
					btn_audio_play_pause.setBackgroundResource(R.drawable.btn_now_playing_pause);
					MediaUtil.PLAYSTATE = ConstantValue.OPTION_PLAY;

					changeNotice(Color.GREEN);
				}

			}
		});
		songListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changeNotice(Color.WHITE);

				MediaUtil.CURRENTPOS = position;
				Music music = MediaUtil.getInstacen().getSongList()
						.get(MediaUtil.CURRENTPOS);
				startPlayService(music, ConstantValue.OPTION_PLAY);
				btn_audio_play_pause.setBackgroundResource(R.drawable.btn_now_playing_pause);
				// songAdapter.notifyDataSetChanged();
				changeNotice(Color.GREEN);

			}
		});
		btn_audio_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.xiami.com");
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
		});
	}
	private void startPlayService(Music music, int option) {
		Intent intent = new Intent(getApplicationContext(), MediaService.class);
		if (music != null) {
			intent.putExtra("file", music.getPath());
		}
		intent.putExtra("option", option);
		startService(intent);
	}
	private void init() {
		loadSongList();
		
		mediaController();
	}
	private void mediaController() {
		btn_audio_mode = (Button) findViewById(R.id.btn_audio_mode);
		btn_audio_pre = (Button) findViewById(R.id.btn_audio_pre);
		btn_audio_play_pause = (Button) findViewById(R.id.btn_audio_play_pause);
		btn_audio_next = (Button) findViewById(R.id.btn_audio_next);
		btn_audio_menu = (Button) findViewById(R.id.btn_audio_menu);
		
		if(MediaUtil.PLAYSTATE == ConstantValue.OPTION_PAUSE){
			btn_audio_play_pause.setBackgroundResource(R.drawable.btn_audio_play_selector);
		}else if(MediaUtil.PLAYSTATE == ConstantValue.OPTION_PLAY){
			btn_audio_play_pause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
		}
	}
	private void loadSongList() {
		//MediaUtil.getInstacen().initMusics(getApplicationContext());
		songAdapter = new MySongListAdapter(getApplicationContext());
		songListView = (ListView) findViewById(R.id.audio_play_list);
		songListView.setAdapter(songAdapter);
		
		//new InitDataTask().execute();//线程池，如操作资源过多 会出现等待情况
		new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	@Override
	public View setContentView() {
		return View.inflate(this, R.layout.activity_audio_list, null);
	}

	@Override
	public void rightButtonClick() {
		/**
		 * 模仿SD卡插拔
		 */
		//Intent intent = new Intent();
		//intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		//intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		//sendBroadcast(intent);
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		scanReceiver = new ScanSdFilesReceiver();
		registerReceiver(scanReceiver, intentFilter);
		sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://" + Environment.getExternalStorageDirectory())));
	}

	@Override
	public void leftButtonClick() {
		finish();
	}


	/**
	 * 音乐资源过多
	 */
	class InitDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			PromptManager.showProgressDialog(AudioListActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// 加载多媒体信息
			MediaUtil.getInstacen().initMusics(AudioListActivity.this);
			// SystemClock.sleep(100);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PromptManager.closeProgressDialog();
			songAdapter.notifyDataSetChanged();
		}
	}
}
