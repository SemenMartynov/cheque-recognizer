package ru.spbau.cheque;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.spbau.cheque.recognition.*;
import ru.spbau.cheque.recognition.BlueObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/21/12
 * Time: 3:53 AM
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "test";

    public static final String CHEQUE_TABLE_NAME = "cheques";
    public static final String CHEQUE_DATE = "date";
    public static final String CHEQUE_COMPANY = "company";
    private static final String CHEQUE_CREATE_TABLE = "create table " + CHEQUE_TABLE_NAME + " ( _cheque_id integer primary key autoincrement, "
            + CHEQUE_DATE + " INTEGER, " + CHEQUE_COMPANY + " TEXT);";


    public static final String BLUE_TABLE_NAME = "blue_objects";
    public static final String BLUE_NAME = "name";
    public static final String BLUE_COUNT = "count";
    public static final String BLUE_PRICE = "price";
    public static final String BLUE_CHEQUE_REF = "cheque_ref";
    private static final String BLUE_CREATE_TABLE = "create table " + BLUE_TABLE_NAME + " ( _id integer primary key autoincrement, "
            + BLUE_NAME + " TEXT, " + BLUE_COUNT + " REAL, "  + BLUE_CHEQUE_REF + " INTEGER, " + BLUE_PRICE + " REAL, FOREIGN KEY(" + BLUE_CHEQUE_REF + ") REFERENCES " + CHEQUE_TABLE_NAME + "(_id) ON DELETE CASCADE);";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CHEQUE_CREATE_TABLE);
        sqLiteDatabase.execSQL(BLUE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }

    public void putChequeToDB(Cheque inpCheque){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.CHEQUE_COMPANY, inpCheque.getCompany());
        final long millis = 1000;
        final long seconds = 60;
        final long minutes = 60;
        final long hours = 24;
        final long days = 356;
        final long millisInYear = millis*seconds*minutes*hours*days;
        cv.put(DBOpenHelper.CHEQUE_DATE, System.currentTimeMillis() - nextLong(myRand, millisInYear));
        long chequeID = db.insert(DBOpenHelper.CHEQUE_TABLE_NAME, null, cv);
        for (ru.spbau.cheque.recognition.BlueObject bl : inpCheque.getBlues()){
            ContentValues bcv = new ContentValues();
            bcv.put(DBOpenHelper.BLUE_NAME, bl.getName());
            bcv.put(DBOpenHelper.BLUE_COUNT, bl.getCount());
            bcv.put(DBOpenHelper.BLUE_PRICE, bl.getPrice());
            bcv.put(DBOpenHelper.BLUE_CHEQUE_REF, chequeID);
            db.insert(DBOpenHelper.BLUE_TABLE_NAME, null, bcv);
        }
        db.close();
    }

    public void fillDBWithRandomData(int chequesNum){
        for (int i = 0; i < chequesNum; ++i){
            putChequeToDB(getRandomCheque());
        }
    }

    private static Integer blueCounter = 0;
    private static final Random myRand = new Random(12345);

    private Cheque getRandomCheque(){
        int blueNum = myRand.nextInt(10);
        List<ru.spbau.cheque.recognition.BlueObject> table = new ArrayList<ru.spbau.cheque.recognition.BlueObject>();
        for (int i = 0; i < blueNum; ++i){
            table.add(getRandomBlue());
        }
        return new Cheque("ROGA & KOPITA", table);
    }

    private ru.spbau.cheque.recognition.BlueObject getRandomBlue(){
        String name = "Name" + blueCounter.toString();
        float count = myRand.nextFloat() * 3;
        float price = myRand.nextFloat() * 100;
        return new BlueObject(name, count, price);
    }
}
