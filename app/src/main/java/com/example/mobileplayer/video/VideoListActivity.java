 package com.example.mobileplayer.video;

import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;
import com.example.mobileplayer.audio.ScanSdFilesReceiver;
import com.example.mobileplayer.domain.VideoItem;
import com.example.mobileplayer.utils.Utils;

public class VideoListActivity extends BaseActivity {

	private ListView lv_videolist;
	private TextView tv_novideo;
	private Utils utils;
	private ScanSdFilesReceiver scanReceiver;
	
	private ArrayList<VideoItem> videoItems;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(videoItems != null&&videoItems.size()>0){
				lv_videolist.setAdapter(new VideoListAdapter());
			}else{
				tv_novideo.setVisibility(View.VISIBLE);  
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
		setTitle("本地视频");
		//setRightButton(View.GONE);
		
		lv_videolist = (ListView) findViewById(R.id.lv_videolist);
		tv_novideo = (TextView) findViewById(R.id.tv_novideo);
		utils = new Utils();
		getAllVieo();
		lv_videolist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				/*VideoItem videoItem = videoItems.get(position);
				Intent intent = new Intent(VideoListActivity.this,VideoPlayerActivity.class);
				intent.setData(Uri.parse(videoItem.getData()));
				startActivity(intent);*/
				//Toast.makeText(getApplicationContext(), videoItem.getTitle(), 0).show();
				Intent intent = new Intent(VideoListActivity.this,VideoPlayerActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("videolist", videoItems);
				intent.putExtras(extras);
				
				intent.putExtra("position", position);
				
				startActivity(intent);
			}
		});
	}
	private class VideoListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return videoItems.size();
		}


		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(converView != null){
				view = converView;
				holder = (ViewHolder)view.getTag();
			}else{
				view = View.inflate(VideoListActivity.this, R.layout.video_list_item, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
				
				holder.tv_size = (TextView) view.findViewById(R.id.tv_size);
				
				view.setTag(holder);
			}
			VideoItem videoItem = videoItems.get(position);
			holder.tv_name.setText(videoItem.getTitle());
			holder.tv_duration.setText(utils.stringForTime(Integer.valueOf(videoItem.getDuration())));
			holder.tv_size.setText(Formatter.formatFileSize(VideoListActivity.this, videoItem.getSize()));
			
			return view;
		}


		@Override
		public Object getItem(int position) {
			return null;
		}


		@Override
		public long getItemId(int position) {
			return 0;
		}
		
	}
	static class ViewHolder{
		TextView tv_name;
		TextView tv_duration;
		TextView tv_size;
	}
	/*
	 * 得到视频的列表和视频的信息
	 */
	private void getAllVieo() {
		new Thread(){
			public void run(){
				videoItems = new ArrayList<VideoItem>();
				ContentResolver resolver =  getContentResolver();
				Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				String[] projection = {
						MediaStore.Video.Media.TITLE,
						MediaStore.Video.Media.DURATION,
						MediaStore.Video.Media.SIZE,
						MediaStore.Video.Media.DATA
				};
				String selection = null;
				String[] selectionArgs = null;
				String sortOrder = null;
				//Cursor cursor = resolver.query(uri, projection, null, null, null);
				Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);
				while(cursor.moveToNext()){
					long size = cursor.getLong(2);
					if(size > 3*1024*1024){
						VideoItem item = new VideoItem();
						String title = cursor.getString(0);
						item.setTitle(title);
						
						String duration = cursor.getString(1);
						item.setDuration(duration);
						
						
						item.setSize(size);
						
						String data = cursor.getString(3);
						item.setData(data);
						
						videoItems.add(item);
					}
					
					
					
					
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
	@Override
	public View setContentView() {
		
		return View.inflate(this, R.layout.activity_video_list, null);
	}

	/*
	 * 重新加载存储卡(non-Javadoc)
	 * @see com.example.mobileplayer.BaseActivity#rightButtonClick()
	 */
	@Override
	public void rightButtonClick() {
		
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

}
