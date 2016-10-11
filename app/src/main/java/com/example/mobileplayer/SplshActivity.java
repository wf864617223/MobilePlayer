package com.example.mobileplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
/**
 * 闪屏界面
 * @author MI
 *
 */
public class SplshActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splsh);
		isEnterMained = false;
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				enterMain();
			}
		}, 2000);
	}

	protected void enterMain() {
		if(!isEnterMained){
			isEnterMained = true;
			Intent intent = new Intent(this , MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	private boolean isEnterMained = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		enterMain();
		return super.onTouchEvent(event);
	}

}
