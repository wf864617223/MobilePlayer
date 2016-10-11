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

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class NetWorkAudioActivity extends BaseActivity {

	private GridView gridView;
	private int[] ids = {R.drawable.migu_png,R.drawable.baidu_png,R.drawable.qq_png,
			R.drawable.wangyi_png,R.drawable.xiami_png,R.drawable.kugou_png};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setRightButton(View.GONE);
		setTitle("网络音乐");
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new MyMainAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				String uri;
				switch (position) {
				case 0:
					uri = "http://music.migu.cn";
					bundle.putString("uri", uri);
					break;
				case 1:
					uri = "http://music.baidu.com";
					bundle.putString("uri", uri);
					break;
				case 2:
					uri = "http://y.qq.com";
					bundle.putString("uri", uri);
					break;
				case 3:
					uri = "http://music.163.com";
					bundle.putString("uri", uri);
					break;
				case 4:
					uri = "http://www.xiami.com";
					bundle.putString("uri", uri);
					break;
				case 5:
					uri = "http://www.kugou.com";
					bundle.putString("uri", uri);
					break;

				default:
					break;
				}
				intent.setClass(NetWorkAudioActivity.this, WebView_Net.class);
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
		public Object getItem(int position) {
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
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(getApplicationContext(), R.layout.network_item, null);
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
		
		return View.inflate(getApplicationContext(), R.layout.activity_main, null);
	}

	@Override
	public void rightButtonClick() {
		

	}

	@Override
	public void leftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

}
