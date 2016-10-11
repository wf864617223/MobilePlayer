package com.example.mobileplayer.network;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class NetworkVideoActivity extends BaseActivity {

	private GridView gridView;
	private int[] ids = {R.drawable.youku,R.drawable.tudou,R.drawable.iqiyi,
			R.drawable.tengxun,R.drawable.pptv,R.drawable.souhu};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRightButton(View.GONE);
		setTitle("网络视频");
		
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new MyMainAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				String uri;
				switch(position) {
				case 0:
					//Toast.makeText(getApplicationContext(), "Music", 0).show();
					uri = "http://www.youku.com";
					bundle.putString("uri", uri);
					break;

				case 1:
					//Toast.makeText(getApplicationContext(), "Video", 0).show();
					uri = "http://www.tudou.com";
					bundle.putString("uri", uri);
					break;
				case 2:
					//Toast.makeText(getApplicationContext(), "network", 0).show();
					uri = "http://www.iqiyi.com";
					bundle.putString("uri", uri);
					break;
				case 3:
					//intent = new Intent(MainActivity.this,VitamioListActivity.class);
					uri = "http://v.qq.com";
					bundle.putString("uri", uri);
					break;
				case 4:
					uri = "http://www.pptv.com";
					bundle.putString("uri", uri);
					break;
				case 5:
					uri = "http://tv.sohu.com";
					bundle.putString("uri", uri);
					break;
				default:
					Toast.makeText(getApplicationContext(), "not found", 0).show();
					break;
				}
				intent.setClass(NetworkVideoActivity.this, WebView_Net.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
	}
	private class MyMainAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ids.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
					view = View.inflate(NetworkVideoActivity.this, R.layout.network_item, null);
					holder = new ViewHolder();
					holder.iv_nw = (ImageView) view.findViewById(R.id.iv_nw);
					view.setTag(holder);
				}
			holder.iv_nw.setImageResource(ids[position]);
			return view;
			}
		}
	static class ViewHolder{
		ImageView iv_nw;
	}
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(this, R.layout.activity_main, null);
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
