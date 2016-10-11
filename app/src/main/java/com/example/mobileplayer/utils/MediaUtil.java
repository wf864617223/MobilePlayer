package com.example.mobileplayer.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.example.mobileplayer.audio.ConstantValue;
import com.example.mobileplayer.audio.Music;

public class MediaUtil {
	private List<Music> songList = new ArrayList<Music>();
	// 当前正在播放
	public static int CURRENTPOS = 0;
	public static int PLAYSTATE = ConstantValue.OPTION_PAUSE;

	private static MediaUtil instance = new MediaUtil();

	private MediaUtil() {
	}

	public static MediaUtil getInstacen() {
		return instance;
	}

	public List<Music> getSongList() {
		return songList;
	}

	public void initMusics(Context context) {
		songList.clear();
		String[] projection =  { MediaStore.Audio.Media.TITLE, 
				MediaStore.Audio.Media.DURATION, 
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media._ID, 
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.SIZE };
		Cursor cur = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, null);
		try {
			if (cur != null) {
				
				
				while (cur.moveToNext()) {
					long size = cur.getLong(5);
					if(size > 1*1024*1024){
					Music m = new Music();
					m.setTitle(cur.getString(0));
					m.setDuration(cur.getInt(1));
					m.setArtist(cur.getString(2));
					m.setId(cur.getString(3));
					m.setPath(cur.getString(4));
					songList.add(m);
				}
				}
			}
		} catch (Exception e) {
		} finally {
			if (cur != null)
				cur.close();
		}

	}

	private String getString(String duration) {
		// TODO Auto-generated method stub
		return null;
	}

	public Music getCurrent() {
		return songList.get(CURRENTPOS);
	}

}
