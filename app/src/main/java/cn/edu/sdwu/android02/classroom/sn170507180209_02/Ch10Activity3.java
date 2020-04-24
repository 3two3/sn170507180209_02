package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Ch10Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ch10_3);
    }

    public void ok(View view) {
        //2.设置子Activity的返回值
        EditText editText = (EditText) findViewById(R.id.ch10_3_tv);
        String content = editText.getText().toString();
        //将数据放在Intent里面
        Intent intent = new Intent();
        intent.putExtra("name", content);
        setResult(RESULT_OK, intent);//设置返回值

        finish();//关闭当前界面
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);//取消
        finish();//关闭当前界面
    }
}

