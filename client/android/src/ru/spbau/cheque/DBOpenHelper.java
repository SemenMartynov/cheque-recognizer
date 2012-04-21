package ru.spbau.cheque;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/21/12
 * Time: 3:53 AM
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "test";

    public static final String TABLE_NAME = "blue_objects";
    public static final String NAME = "name";
    public static final String COUNT = "count";
    public static final String PRICE = "price";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
            + NAME + " TEXT, " + COUNT + " INTEGER, " + PRICE + " REAL)";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
