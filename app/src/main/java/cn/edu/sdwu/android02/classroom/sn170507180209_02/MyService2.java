package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService2 extends Service {

    private Random random;
    private MyBinder myBinder;
    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        myBinder = new MyBinder();
    }

    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回值，会返回给调用者，以后调用者与服务来交互，都会使用此返回值
        // TODO: Return the communication channel to the service.
        Log.i(MyService2.class.toString(),"onBind");
        //throw new UnsupportedOperationException("Not yet implemented");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(MyService2.class.toString(),"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        //当所有调用者都解绑后，销毁
        Log.i(MyService2.class.toString(),"onDestroy");
        super.onDestroy();
    }

    public class MyBinder extends Binder{
        public MyService2 getRandomService(){
            return MyService2.this;
        }
    }

    //实际与调用者进行交互的方法
    public int getRandom(){
        return random.nextInt();
    }
}
