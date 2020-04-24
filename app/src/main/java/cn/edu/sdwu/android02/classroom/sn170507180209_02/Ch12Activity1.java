package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Ch12Activity1 extends AppCompatActivity {

    private ServiceConnection serviceConnection;
    private MyService2        myService2;
    private boolean           bindSucc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ch12_1);

        bindSucc = false;
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //当调用者与服务建立起链接后会自动调用该方法
                //第二个参数是service中onBind方法的返回值
                MyService2.MyBinder myBinder = (MyService2.MyBinder) iBinder;
                myService2 = myBinder.getRandomService();
                bindSucc = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                //断开链接后，会自动调用这个方法
                bindSucc = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Ch12Activity1.class.toString(), "onStart");
        Intent intent = new Intent(this, MyService2.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        //绑定
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Ch12Activity1.class.toString(), "onStop");
        //解绑
        unbindService(serviceConnection);


    }

    public void start_service(View view) {
        Log.i(Ch12Activity1.class.toString(), "start_service");
        //使用intent启动服务
        Intent intent = new Intent(this, MyService.class);
        //使用startService以启动方式使用服务
        startService(intent);
    }

    public void stop_service(View view) {
        Log.i(Ch12Activity1.class.toString(), "stop_service");
        //停止服务
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void getRandom(View view) {
        if (bindSucc) {
            int ran = myService2.getRandom();
            Toast.makeText(this,""+ran, Toast.LENGTH_SHORT).show();
        }
    }
}
