package com.example.mobileplayer.help;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class ChooseHelpActivity extends BaseActivity {

	private ListView help_list;
	private String[] ids = {"视频播放帮助","音乐播放帮助","软件介绍","关于开发者"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("使用小贴士");
		setRightButton(View.GONE);
		
		help_list = (ListView) findViewById(R.id.help_list);
		help_list.setAdapter(new MyAdapter());
		help_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent;
				switch (position) {
				case 0:
					intent = new Intent(ChooseHelpActivity.this,HelpUserActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(ChooseHelpActivity.this,HelpUserAudioActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(ChooseHelpActivity.this,HelpUserSoftware.class);
					startActivity(intent);
					break;
				case 3:
					new AlertDialog.Builder(ChooseHelpActivity.this).setTitle("关于开发者")
							.setMessage("开发人员：王帆\n" +
									"E-mail:864617223@qq.com" )
							.show();
					break;
				default:
					break;
				}
				
			}
		});
	}
	private class MyAdapter extends BaseAdapter{

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
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(ChooseHelpActivity.this, R.layout.help_text, null);
				holder = new ViewHolder();
				holder.textView_help = (TextView) view.findViewById(R.id.textView_help);
				view.setTag(holder);
			}
			holder.textView_help.setText(ids[position]);
			return view;
		}
		
	}
	static class ViewHolder{
		TextView textView_help;
	}
	@Override
	public View setContentView() {
		
		return View.inflate(this, R.layout.choose_help, null);
	}

	@Override
	public void rightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftButtonClick() {
		finish();

	}

}
