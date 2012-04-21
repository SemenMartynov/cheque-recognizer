package ru.spbau.cheque;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Main extends Activity
{
    static final int GET_PHOTO_FROM_CAMERA_REQUEST = 1;
    static final int CROP = 2;
    private Uri outputFileUri;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button takePhotoBtn = (Button) findViewById(R.id.takePhoto);
        Button spendingsSumBtn = (Button) findViewById(R.id.spendingsSum);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "tmp.jpg");
                outputFileUri = Uri.fromFile(file);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GET_PHOTO_FROM_CAMERA_REQUEST) {
            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setClassName("com.android.camera", "com.android.camera.CropImage");
//            intent.setClassName("com.android.gallery", "com.android.camera.CropImage");
            intent.setType("image/*");
//            Bitmap bmp = (Bitmap)data.getExtras().get("data");


//            Uri uri = data.getData();
            intent.setData(outputFileUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
//            intent.putExtra("return-data", true);
            try {
            startActivityForResult(intent, CROP);
            }
            catch(Exception e){
                System.out.println(e.getStackTrace());
            }
//            Socket sock = null;
//            try {
//                Bitmap pic = (Bitmap) data.getExtras().get("data");
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
