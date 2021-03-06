package com.example.mobileplayer.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.mobileplayer.audio.ConstantValue;
import com.example.mobileplayer.utils.HandlerManager;

/**
 * 系统刷新媒体列表的广播接收者
 * 
 * @author Administrator
 * 
 */
public class ScanSdFilesReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
			HandlerManager.getHandler().sendEmptyMessage(ConstantValue.STARTED);
		}
		if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			HandlerManager.getHandler().sendEmptyMessage(ConstantValue.FINISHED);
		}

	}

}
