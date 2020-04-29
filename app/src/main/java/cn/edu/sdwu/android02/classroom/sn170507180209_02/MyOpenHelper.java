package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @创建者 coffee
 * @创建时间 2020/4/29 9:13
 * @描述
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String STUDENT_TB_SQL = "create table student(id integer primary key autoincrement,stuname text,stutel text)";

    public MyOpenHelper(Context context) {
        //Context context上下文，String name数据库的名称，CursorFactory factory传入NULL，int version数据库的版本
        super(context, "stud.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //当打开数据库时，发现数据库并不存在，此时回自动创建数据库，然后调用onCreate方法
        //在本方法中，完成数据库对象（表、试图、索引等）的创建
        //通过sqLiteDatabase对象完成sql语句的执行
        sqLiteDatabase.execSQL(STUDENT_TB_SQL);
        Log.i(MyOpenHelper.class.toString(), "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
