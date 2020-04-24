package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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
        ip.setText(sharedPreferences.getString("ip",""));
        port.setText(sharedPreferences.getString("port",""));
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
}
