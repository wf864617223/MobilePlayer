package com.example.mobileplayer.help;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class HelpUserSoftware extends BaseActivity {

	private TextView software_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("软件介绍");
		setRightButton(View.GONE);
		software_text = (TextView) findViewById(R.id.software_text);
		software_text.setText("我的目标是：做移动端最好的多媒体影音播放器\n" +
				"\n" +
				"该软件是android端功能齐全的一款播放器，集成了视频播放、音乐播放、在线视频播放、在线音乐播放的功能，让您的android手机变成您的个人娱乐中心\n" +
				"【产品特点】\n" +
				"\n音乐播放：\n" +
				"    支持.mp3 .wmv .aac .ogg等多种格式让您耳朵闲不下来！\n" +
				"视频播放：\n" +
				"    支持 .3gp .mp4 .avi等您的手机硬件支持的格式，让您愉快的享受视听盛宴！\n" +
				"欢迎联系我们并将您的宝贵意见发给我们！\n" +
				"\n" +
				"微信：nibeipianle\n" +
				"微博：chawuciren");
	}
	
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(getApplicationContext(), R.layout.help_software, null);
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
