package ru.spbau.cheque;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import ru.spbau.cheque.camera.CropImage;
import ru.spbau.cheque.recognition.OcrFailedException;

import java.io.File;
import java.io.IOException;

public class Main extends Activity
{
    static final int GET_PHOTO_FROM_CAMERA_REQUEST = 1;
    static final int CROP_IMAGE_REQUEST = 2;
    private Uri outputFileUri = null;
    private File outputFile = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            outputFileUri = (Uri) getIntent().getExtras().get("outUri");
            outputFile = (File) getIntent().getExtras().get("outFile");
        }

        setContentView(R.layout.main);
        Button takePhotoBtn = (Button) findViewById(R.id.takePhoto);
        Button spendingsSumBtn = (Button) findViewById(R.id.spendingsSum);
        Button spendingsPeriodBtn = (Button) findViewById(R.id.spendingsPeriod);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                outputFile = null;
                try {
                    outputFile = createTemporaryFile("cheque-", ".jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                outputFile.deleteOnExit();
                getIntent().putExtra("outFile", outputFile);

                outputFileUri = Uri.fromFile(outputFile);
                getIntent().putExtra("outUri", outputFileUri);

//                pictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                pictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
                try{
                    startActivityForResult(pictureIntent, GET_PHOTO_FROM_CAMERA_REQUEST);
                }
                catch (Exception e)
                {
                    System.out.println(e.getStackTrace());
                }
            }
        });
        spendingsSumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, SpendingsSumActivity.class);
                startActivity(intent);
            }
        });


        spendingsPeriodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, SpendingsPeriodActivity.class);
                startActivity(intent);
            }
        });
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
        case GET_PHOTO_FROM_CAMERA_REQUEST :
            Intent crop = new Intent(this, CropImage.class);
            crop.setData(outputFileUri);
            crop.putExtra("return-data", true);
            try {
                startActivityForResult(crop, CROP_IMAGE_REQUEST);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            break;
        case CROP_IMAGE_REQUEST :
            if (data == null || data.getExtras() == null)
                return;

            Bitmap bmpCropped = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
                Cheque cheque = new Recognizer().doRecognition(bmp, bmpCropped);
                if (cheque.getBlues().size() > 0)
                    new DBOpenHelper(this).putChequeToDB(cheque);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OcrFailedException e) {
                e.printStackTrace();
            }
            break;
        }
    }
}
