package com.example.mobileplayer.help;

import android.os.Bundle;
import android.view.View;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class HelpUserAudioActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("音乐播放帮助");
		setRightButton(View.GONE);
	}
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(getApplicationContext(), R.layout.help_user_audio, null);
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
