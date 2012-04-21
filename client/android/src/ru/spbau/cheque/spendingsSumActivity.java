package ru.spbau.cheque;

import android.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/20/12
 * Time: 9:25 PM
 */
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
        int count = myRand.nextInt(10) + 1;
        float price = myRand.nextFloat() * 1000;
        return new BlueObject(name, count, price);
    }

   private DBOpenHelper helper = new DBOpenHelper(SpendingsSumActivity.this);

   private void fillDBWithRandomData(){
       int chequeNum = 10;
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
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(DBOpenHelper.TABLE_NAME, new String[] { "SUM(" + DBOpenHelper.PRICE + ")" }, null, null, null, null, null);
        cursor.moveToFirst();
        return Float.toString(cursor.getFloat(0));
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fillDBWithRandomData();
        setContentView(ru.spbau.cheque.R.layout.spendingssum);
        TextView textOnScreen = (TextView) findViewById(ru.spbau.cheque.R.id.sum);
        textOnScreen.setText("Current sum of your spendings is: " + getGlobalSumm());

    }
}
