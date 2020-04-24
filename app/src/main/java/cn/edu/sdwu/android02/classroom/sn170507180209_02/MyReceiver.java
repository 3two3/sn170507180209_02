package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        //获取广播的内容
        String content = intent.getStringExtra("key1");
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

    }
}
