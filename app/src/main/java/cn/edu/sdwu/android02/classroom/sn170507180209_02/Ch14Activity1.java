package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Ch14Activity1 extends AppCompatActivity {
    private MyOpenHelper myOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ch14_1);

        myOpenHelper = new MyOpenHelper(this);


    }

    public void insert(View view) {
        //以可写方法打开数据库(如果数据库不存在，自动创建数据库)
        SQLiteDatabase sqliteDatabase = myOpenHelper.getWritableDatabase();
        try {
            //将插入的数据放置在contentValues中
            //开启事务
            sqliteDatabase.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("stuname", "Tom");
            contentValues.put("stutel", "15665750807");
            sqliteDatabase.insert("student", null, contentValues);
            //所有操作结束后调用此方法，才会将数据提交
            sqliteDatabase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.i(Ch14Activity1.class.toString(), e.toString());
        } finally {
            //结束事务
            sqliteDatabase.endTransaction();
            //使用完毕，将数据库关闭
            sqliteDatabase.close();
        }
    }

    public void query(View view) {
        //以只读方法打开数据库(如果数据库不存在，自动创建数据库)
        SQLiteDatabase sqliteDatabase = myOpenHelper.getReadableDatabase();
        try {
            Cursor cursor = sqliteDatabase.rawQuery("select * from student where stuname=?", new String[]{"Tom"});
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String stuname = cursor.getString(cursor.getColumnIndex("stuname"));
                String stutel = cursor.getString(cursor.getColumnIndex("stutel"));
                Toast.makeText(this, id + "-->" + stuname + "-->" + stutel, Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        } catch (Exception e) {
            Log.i(Ch14Activity1.class.toString(), e.toString());
        } finally {
            //使用完毕，将数据库关闭
            sqliteDatabase.close();
        }
    }

    public void delete(View view) {
        //以可写方法打开数据库(如果数据库不存在，自动创建数据库)
        SQLiteDatabase sqliteDatabase = myOpenHelper.getWritableDatabase();
        try {
            //开启事务
            sqliteDatabase.beginTransaction();
            sqliteDatabase.delete("student", "id=?", new String[]{"1"});
            //所有操作结束后调用此方法，才会将数据提交
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(Ch14Activity1.class.toString(), e.toString());
        } finally {
            //结束事务
            sqliteDatabase.endTransaction();
            //使用完毕，将数据库关闭
            sqliteDatabase.close();
        }
    }

    public void modify(View view) {
        //以可写方法打开数据库(如果数据库不存在，自动创建数据库)
        SQLiteDatabase sqliteDatabase = myOpenHelper.getWritableDatabase();
        try {
            //开启事务
            sqliteDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("stutel", "1342567389");

            sqliteDatabase.update("student", contentValues, "id=?", new String[]{"1"});
            //所有操作结束后调用此方法，才会将数据提交
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(Ch14Activity1.class.toString(), e.toString());
        } finally {
            //结束事务
            sqliteDatabase.endTransaction();
            //使用完毕，将数据库关闭
            sqliteDatabase.close();
        }
    }
}
