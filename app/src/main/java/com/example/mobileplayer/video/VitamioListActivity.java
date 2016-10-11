package com.example.mobileplayer.video;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class VitamioListActivity extends BaseActivity {

	private ListView lv_videolist;
	private TextView tv_novideo;
	private final String MUSIC_PATH = "/sdcard/"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("本地视频");
		setRightButton(View.GONE);
		
		lv_videolist = (ListView) findViewById(R.id.lv_videolist);
		getAllVideo();
	}
	class videofilter implements FilenameFilter {  
	    /* 
	     * accept方法的两个参数的意义： dir：文件夹对像，也就是你原来调用list方法的File文件夹对像 name：当前判断的文件名， 
	     * 这个文件名就是文件夹下面的文件 
	     * 返回：这个文件名是否符合条件，当为true时，list和listFiles方法会把这个文件加入到返回的数组里，false时则不会加入 
	     */  
	    public boolean accept(File dir, String filename) {  
	        // TODO Auto-generated method stub  
	        return (filename.endsWith(".mp3"));  
	    }  
	}
	private void getAllVideo() {
		// TODO Auto-generated method stub
	/*	File file = new File(MUSIC_PATH);
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();  
	    //将所有的文件加入到一个list文件中  
	    if(file.list(new videofilter()).length>0){  
	          
	        for (File file1 : file.listFiles(new videofilter())){  
	            Map<String,Object> map=new HashMap<String, Object>();  
	           map.put("filename",new EFfile(file1).getFile().getName());  
	           list.add(map);  
	        }  
	    }  
	    SimpleAdapter sa= new SimpleAdapter(SetAlarm.this, list,   
	            R.layout.musiclist, new String[]{"filename"}, new int[]{R.id.lv_videolist} );  
	    listview.setAdapter(sa); */
	}
	@Override
	public View setContentView() {
		// TODO Auto-generated method stub
		return View.inflate(this, R.layout.activity_video_list, null);
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
