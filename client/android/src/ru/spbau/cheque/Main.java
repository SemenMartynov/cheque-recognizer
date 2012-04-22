package ru.spbau.cheque;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import ru.spbau.cheque.recognition.OcrFailedException;

import java.io.IOException;

public class Main extends Activity
{
    static final int GET_PHOTO_FROM_CAMERA_REQUEST = 1;
    static final int CROP = 2;
    private Uri outputFileUri = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        outputFileUri = getIntent() != null && getIntent().getExtras() != null ? (Uri) getIntent().getExtras().get("outUri") : null;

        setContentView(R.layout.main);
        Button takePhotoBtn = (Button) findViewById(R.id.takePhoto);
        Button spendingsSumBtn = (Button) findViewById(R.id.spendingsSum);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "My demo image");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image Captured by Camera via an Intent");

//                File outputDir = getCacheDir(); // context being the Activity pointer
//                File outputFile = null;
//                try {
//                    outputFile = File.createTempFile("", ".jpg", outputDir);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                outputFileUri = Uri.fromFile(outputFile);

//                outputFileUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                outputFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                getIntent().putExtra("outUri", outputFileUri);

                pictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
//                pictureIntent.putExtra("furi", outputFileUri);
                //pictureIntent.setData(outputFileUri);

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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == GET_PHOTO_FROM_CAMERA_REQUEST) {
            //Bitmap bmp = (Bitmap) intent.getExtras().get("intent");

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                new Recognizer().doRecognition(bmp, 0, 0, bmp.getWidth(), bmp.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OcrFailedException e) {
                e.printStackTrace();
            }
            return;
            //Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setClassName("com.android.camera", "com.android.camera.CropImage");
//            intent.setClassName("com.android.gallery", "com.android.camera.CropImage");
            //intent.setType("image/*");
//            Bitmap bmp = (Bitmap)intent.getExtras().get("intent");


//            Uri uri = intent.getData();
//            intent.setData(outputFileUri);
//
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("scale", true);
////            intent.putExtra("return-intent", true);
//            try {
//            startActivityForResult(intent, CROP);
//            }
//            catch(Exception e){
//                System.out.println(e.getStackTrace());
//            }
//            Socket sock = null;
//            try {
//                Bitmap pic = (Bitmap) intent.getExtras().get("intent");
//                sock = new Socket("192.168.222.169", 3843);
//                OutputStream os = sock.getOutputStream();
//                pic.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                os.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                if (sock != null)
//                    try {
//                        sock.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//            }
        }
    }
}
