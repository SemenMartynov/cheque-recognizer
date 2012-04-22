package ru.spbau.cheque;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import ru.spbau.cheque.recognition.BlueObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpendingsSumActivity extends Activity {

    private static Integer blueCounter = 0;
    private static final Random myRand = new Random(12345);

    private Cheque getRandomCheque(){
        int blueNum = myRand.nextInt(10);
        List<BlueObject> table = new ArrayList<BlueObject>();
        for (int i = 0; i < blueNum; ++i){
            table.add(getRandomBlue());
        }
        return new Cheque("ROGA & KOPITA", table);
    }

    private BlueObject getRandomBlue(){
        String name = "Name" + blueCounter.toString();
        float count = myRand.nextFloat() * 3;
        float price = myRand.nextFloat() * 100;
        return new BlueObject(name, count, price);
    }

   private DBOpenHelper helper = new DBOpenHelper(SpendingsSumActivity.this);

   private void fillDBWithRandomData(){
       int chequeNum = 3;
       SQLiteDatabase db = helper.getWritableDatabase();
       for (int i = 0; i < chequeNum; ++i){
           Cheque rndCheque = getRandomCheque();
           for (BlueObject bl : rndCheque.getBlues()){
               ContentValues cv = new ContentValues();
               cv.put(DBOpenHelper.NAME, bl.getName());
               cv.put(DBOpenHelper.COUNT, bl.getCount());
               cv.put(DBOpenHelper.PRICE, bl.getPrice());
               db.insert(DBOpenHelper.TABLE_NAME, null, cv);
           }
       }
       db.close();
   }

    private String getGlobalSumm(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBOpenHelper.TABLE_NAME, new String[] { "SUM(" + DBOpenHelper.PRICE + ")" }, null, null, null, null, null);
        cursor.moveToFirst();
        return Float.toString(cursor.getFloat(0));
    }


    private String getSumOfLast(int inpNumber){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBOpenHelper.TABLE_NAME, new String[] { "SUM(" + DBOpenHelper.PRICE + ")" }, null, null, null, null, null);
        cursor.moveToFirst();
        return Float.toString(cursor.getFloat(0));
    }

    private void drobDB(){
        //SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + DBOpenHelper.TABLE_NAME);
        SpendingsSumActivity.this.deleteDatabase(DBOpenHelper.DB_NAME);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fillDBWithRandomData();
        //drobDB();
        setContentView(ru.spbau.cheque.R.layout.spendingssum);
        TextView textOnScreen = (TextView) findViewById(ru.spbau.cheque.R.id.sum);
        textOnScreen.setText("Current sum of all spendings is: " + getGlobalSumm());

    }

    public void putChequeToDB(Cheque inpCheque){
        SQLiteDatabase db = helper.getWritableDatabase();
        for (BlueObject bl : inpCheque.getBlues()){
            ContentValues cv = new ContentValues();
            cv.put(DBOpenHelper.NAME, bl.getName());
            cv.put(DBOpenHelper.COUNT, bl.getCount());
            cv.put(DBOpenHelper.PRICE, bl.getPrice());
            db.insert(DBOpenHelper.TABLE_NAME, null, cv);
        }
        db.close();
    }
}
