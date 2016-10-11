package com.example.mobileplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobileplayer.audio.AudioListActivity;
import com.example.mobileplayer.gamearder.GameList;
import com.example.mobileplayer.help.ChooseHelpActivity;
import com.example.mobileplayer.help.HelpUserActivity;
import com.example.mobileplayer.network.NetWorkAudioActivity;
import com.example.mobileplayer.network.NetworkVideoActivity;
import com.example.mobileplayer.network.WebView_Net;
import com.example.mobileplayer.video.VideoListActivity;
import com.example.mobileplayer.video.VitamioListActivity;

public class MainActivity extends BaseActivity {
	private GridView gridView;
	private int[] ids = {R.drawable.audio,R.drawable.video,
			R.drawable.network,R.drawable.live,
			R.drawable.ad,R.drawable.all};
	private boolean isExited = false;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			isExited = false;
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftButton(View.GONE);
		setTitle("手机影音");
		
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new MyMainAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				Intent intent;
				switch(position) {
				case 0:
					//Toast.makeText(getApplicationContext(), "Music", 0).show();
					intent = new Intent(MainActivity.this,AudioListActivity.class);
					startActivity(intent);
					break;

				case 1:
					//Toast.makeText(getApplicationContext(), "Video", 0).show();
					intent = new Intent(MainActivity.this,VideoListActivity.class);
					startActivity(intent);
					break;
				case 2:
					//Toast.makeText(getApplicationContext(), "network", 0).show();
					intent = new Intent(MainActivity.this,NetworkVideoActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(MainActivity.this,NetWorkAudioActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(MainActivity.this, GameList.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(MainActivity.this,ChooseHelpActivity.class);
					startActivity(intent);
					break;
				default:
					Toast.makeText(getApplicationContext(), "not found", 0).show();
					break;
				}
			}
			
		});
	}

	private class MyMainAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ids.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
					view = View.inflate(MainActivity.this, R.layout.main_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
					view.setTag(holder);
				}
			holder.iv_icon.setImageResource(ids[position]);
			return view;
			}
		}
	static class ViewHolder{
		ImageView iv_icon;
	}
	@Override
	public void rightButtonClick() {
		//Toast.makeText(getApplicationContext(), "右边按钮被点击", Toast.LENGTH_SHORT).show();
		String uri = "http://www.hao123.com";
		Bundle bundle = new Bundle();
		bundle.putString("uri", uri);
		
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, WebView_Net.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void leftButtonClick() {
		//Toast.makeText(getApplicationContext(), "左边按钮被点击", Toast.LENGTH_SHORT).show();
	}

	@Override
	public View setContentView() {
		return View.inflate(this, R.layout.activity_main, null);
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			QuitApp();
			return true;
			
		}
		return false;
	}
	private void QuitApp() {
		if(!isExited){
			isExited = true;
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			//利用handler发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}else{
			finish();
			System.exit(0);
		}
		/*new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage(
				"确定退出?").setIcon(R.drawable.quit).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {	
					
						finish();
					}
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();*/

	}
}
