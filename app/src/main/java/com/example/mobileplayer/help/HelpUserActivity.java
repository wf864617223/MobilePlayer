package com.example.mobileplayer.help;

import android.os.Bundle;
import android.view.View;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class HelpUserActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("使用小贴士");
		setRightButton(View.GONE);
	}
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(this, R.layout.help_user, null);
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
