package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Ch13Activity1 extends AppCompatActivity {

    private EditText ip;
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ch13_1);
        ip = (EditText) findViewById(R.id.ch13_1_ip);
        port = (EditText) findViewById(R.id.ch13_1_port);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        ip.setText(sharedPreferences.getString("ip", ""));
        port.setText(sharedPreferences.getString("port", ""));
    }

    public void write(View v) {
        EditText editText = (EditText) findViewById(R.id.ch13_1_et);
        String content = editText.getText().toString();

        try {
            FileOutputStream fileOutputStream = openFileOutput("android02.txt", MODE_PRIVATE);

            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(Ch13Activity1.class.toString(), e.toString());
        }
    }

    public void read(View v) {
        try {
            FileInputStream fileInputStream = openFileInput("android02.txt");

            int size = fileInputStream.available();
            byte[] bytes = new byte[size];
            fileInputStream.read(bytes);
            String content = new String(bytes);

            fileInputStream.close();

            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(Ch13Activity1.class.toString(), e.toString());
        }
    }

    public void saveSharePref(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ip.getText().toString());
        editor.putString("port", port.getText().toString());
        editor.commit();

    }

    // 3.接收用户的授权结果；
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 4.如果用户同意，则进行下一步操作(写SD卡)
                EditText editText = (EditText) findViewById(R.id.ch13_1_et);
                String content = editText.getText().toString();
                writeExternal(content);
            }

        }

        if (requestCode == 102) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 4.如果用户同意，则进行下一步操作(读SD卡)
                readExternal();
            }

        }
    }

    public void writeSd(View view) {
        EditText editText = (EditText) findViewById(R.id.ch13_1_et);
        String content = editText.getText().toString();

        //对于6.0以后的系统，用户需要在运行时进行动态授权
        //动态授权的过程，一般分为：
        // 1.判断当前用户是否已经授权过；
        // 2.如果尚未授权，弹出动态授权对话框(同意或拒绝)；
        // 3.接收用户的授权结果；
        // 4.如果用户同意，则进行下一步操作(写SD卡)
        //writeExternal(content);

        //判断当前版本是否是6.0以后的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1.判断当前用户是否已经授权过；
            int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                writeExternal(content);
            } else {
                // 2.如果尚未授权，弹出动态授权对话框(同意或拒绝)；
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        } else {
            writeExternal(content);
        }

    }

    private void writeExternal(String content) {
        //写入外部存储
        //得到FileOutputStream的方法，与内部存储不同
        FileOutputStream fileOutputStream = null;

        //创建File对象
        File file = new File(Environment.getExternalStorageDirectory(), "abcde.txt");//构造方法中，提供文件所在的目录名和文件名


        try {
            //使用createNewFile创建文件
            file.createNewFile();
            //判断文件是否存在并且可写
            if (file.exists() && file.canWrite()) {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
            }
        } catch (IOException e) {
            Log.e(Ch13Activity1.class.toString(), e.toString());
            e.printStackTrace();
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e(Ch13Activity1.class.toString(), e.toString());
                e.printStackTrace();
            }
        }
    }

    public void readSd(View view) {
        //判断当前版本是否是6.0以后的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1.判断当前用户是否已经授权过；
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                readExternal();
            } else {
                // 2.如果尚未授权，弹出动态授权对话框(同意或拒绝)；
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
            }
        } else {
            readExternal();
        }
    }

    private void readExternal() {
        //创建File对象
        File file = new File(Environment.getExternalStorageDirectory(), "abcde.txt");//构造方法中，提供文件所在的目录名和文件名
        FileInputStream fileInputStream = null;
        try {
            if (file.exists() && file.canRead()) {
                fileInputStream = new FileInputStream(file);
                int size = fileInputStream.available();
                byte[] bytes = new byte[size];
                fileInputStream.read(bytes);
                Toast.makeText(this, new String(bytes), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Log.e(Ch13Activity1.class.toString(), e.toString());
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                Log.e(Ch13Activity1.class.toString(), e.toString());
                e.printStackTrace();
            }
        }
    }

    public void readRaw(View view) {
        Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(R.raw.readme);

        try {
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            Toast.makeText(this, new String(bytes), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(Ch13Activity1.class.toString(), e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(Ch13Activity1.class.toString(), e.toString());
                e.printStackTrace();
            }
        }
    }
}
