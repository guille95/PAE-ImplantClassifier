package com.grodriguez.implantclassifier.feature;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImagePickerActivity extends Activity implements View.OnClickListener {

    private Uri imageCaptureURI;
    private ImageView imageView;
    private Button galleryB;
    private Button cameraB;
    private Button sendB;
    Boolean permis = false;
    private Bitmap imagebitmap;
    private String path;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_layout);
        galleryB = (Button) findViewById(R.id.galleryButton);
        galleryB.setOnClickListener(this);

        cameraB = (Button) findViewById(R.id.cameraButton);
        cameraB.setOnClickListener(this);

        sendB = (Button) findViewById(R.id.sendButton);
        sendB.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);




        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);            }
        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permis = true;

                } else {
                    Toast.makeText(this, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        Bitmap bitmap  = null;
        if(requestCode == 2){
            imageCaptureURI = data.getData();
            path = getRealPathFromUri(imageCaptureURI);

            if(path == null)
                path = imageCaptureURI.getPath();
            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        }
        else{
            if(imageCaptureURI!= null) {
                path = imageCaptureURI.getPath();
                bitmap = BitmapFactory.decodeFile(path);
            }

        }
        imagebitmap = bitmap;
        imageView.setImageBitmap(bitmap);

    }
    private String getRealPathFromUri(Uri uri){
        String proj[] = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return  cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.galleryButton) {
            makeToast("Galería seleccionada");
            startGalleryActivity();
        }
        else if (v.getId() == R.id.cameraButton) {
            makeToast("Camara seleccionada");
            startCameraActivity();
        }
        else if (v.getId() == R.id.sendButton) {

            if(path!=null) {
                Intent intent = new Intent(ImagePickerActivity.this, ImageViewerActivity.class);
                intent.putExtra("imagePath",path);
                startActivity(intent);
            }
            else
                makeToast("Por favor seleccione una imagen");
        }
    }




    private void startCameraActivity() {
        Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File (Environment.getExternalStorageDirectory(),"tmp_avatar" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageCaptureURI = Uri.fromFile(file);
        try{
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageCaptureURI);
            cameraIntent.putExtra("return data",true);
            startActivityForResult(cameraIntent,1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startGalleryActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from:"), 2);
    }

    private void makeToast(String s){
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        s, Toast.LENGTH_SHORT);
        toast1.show();
    }
}
