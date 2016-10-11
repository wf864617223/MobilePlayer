package com.example.mobileplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 自定义的Activity,此影音播放器的所有Activity都继承这个Activity
 */
public abstract class BaseActivity extends Activity {

	private Button btn_left;
	private TextView tv_title;
	private Button btn_right;
	private LinearLayout ll_child_content;
	private FrameLayout fl_titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);
		initView();
		setOnclickListener();
	}
	private void setOnclickListener() {
		btn_left.setOnClickListener(mOnclickListener);
		btn_right.setOnClickListener(mOnclickListener);
	}
	OnClickListener mOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_left:
				leftButtonClick();
				break;
			case R.id.btn_right:
				rightButtonClick();
				break;
			}
		}
		
	};
	private void initView() {
		btn_left = (Button)findViewById(R.id.btn_left);
		btn_right = (Button)findViewById(R.id.btn_right);
		tv_title = (TextView)findViewById(R.id.tv_title);
		ll_child_content = (LinearLayout) findViewById(R.id.ll_child_content);
		fl_titlebar = (FrameLayout) findViewById(R.id.fl_titlebar);
		
		View child = setContentView();
		if(child != null){
			LayoutParams parms = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			ll_child_content.addView(child , parms);
		}
		
		
	}
	public abstract View setContentView();
	public void setLeftButton(int visibility){
		btn_left.setVisibility(visibility);
	}
	public void setRightButton(int visibility){
		btn_right.setVisibility(visibility);
	}
	public void setTitleBar(int visibility){
		fl_titlebar.setVisibility(visibility);
	}
	public void setTitle(String title){
		tv_title.setText(title);
	}
	public abstract void rightButtonClick(); 
	public abstract void leftButtonClick(); 

}
