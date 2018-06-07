package com.grodriguez.implantclassifier.feature;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class ImagePickerActivity extends Activity implements View.OnClickListener {

    private Uri imageCaptureURI;
    private ImageView imageView;
    private Button galleryB;
    private Button cameraB;
    private Button sendB;
    Boolean permis = false;
    private Bitmap imagebitmap;
    private Classifier classifier;
    private String path;

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";


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


        //imageView.setImageResource(R.drawable.no_image);bitmap = BitmapFactory.decodeFile(path);
        //imageView.setImageResource(BitmapFactory.decodeFile(R.drawable.no_image));
        //imageView.setImageDrawable(R.drawable.no_image);
        //imageView.setImageResource(R.drawable.no_image);



        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);            }
        }

        classifier =
                TensorFlowImageClassifier.create(
                        getAssets(),
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME);

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
            path = imageCaptureURI.getPath();
            bitmap = BitmapFactory.decodeFile(path);

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
            makeToast("Gallery selected");
            startGalleryActivity();
        }
        else if (v.getId() == R.id.cameraButton) {
            makeToast("Camera selected");
            startCameraActivity();
        }
        else if (v.getId() == R.id.sendButton) {

//            if(imagebitmap!=null) {
//                makeToast("Sent");
//                //sendImage();
//
//               // esto peta, el bitmap es grande y no deja enviarlo via putExtra, mirar como hacerlo...
//                //Intent intent = new Intent(ImagePickerActivity.this, ImageViewerActivity.class);
//               // intent.putExtra("BitmapImage", imagebitmap);
//               // startActivity(intent);
//                imagebitmap=getResizedBitmap(imagebitmap,480,270);
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                imagebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//               // Intent in1 = new Intent(this, Activity2.class);
//               // in1.putExtra("image",byteArray);
//                Intent intent = new Intent(ImagePickerActivity.this, ImageViewerActivity.class);
//                intent.putExtra("image",byteArray);
//                startActivity(intent);
//
//            }
//            else
//                makeToast("Please select an image");
//        }

            if(path!=null) {
                makeToast("Sent");
                //sendImage();

                // esto peta, el bitmap es grande y no deja enviarlo via putExtra, mirar como hacerlo...
                //Intent intent = new Intent(ImagePickerActivity.this, ImageViewerActivity.class);
                // intent.putExtra("BitmapImage", imagebitmap);
                // startActivity(intent);
                imagebitmap=getResizedBitmap(imagebitmap,480,270);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // Intent in1 = new Intent(this, Activity2.class);
                // in1.putExtra("image",byteArray);
                Intent intent = new Intent(ImagePickerActivity.this, ImageViewerActivity.class);
                intent.putExtra("image",byteArray);
                intent.putExtra("imagePath",path);
                startActivity(intent);

            }
            else
                makeToast("Please select an image");
        }
    }

    public static float[] getPixels(Bitmap bitmap) {

        final int IMAGE_SIZE = 168;

        int[] intValues = new int[IMAGE_SIZE * IMAGE_SIZE];
        float[] floatValues = new float[IMAGE_SIZE * IMAGE_SIZE * 3];

        if (bitmap.getWidth() != IMAGE_SIZE || bitmap.getHeight() != IMAGE_SIZE) {
            // rescale the bitmap if needed
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, IMAGE_SIZE, IMAGE_SIZE);
        }

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            // bitwise shifting - without our image is shaped [1, 168, 168, 1] but we need [1, 168, 168, 3]
            floatValues[i * 3 + 2] = Color.red(val) - IMAGE_MEAN / IMAGE_STD;
            floatValues[i * 3 + 1] = Color.green(val) - IMAGE_MEAN / IMAGE_STD;
            floatValues[i * 3] = Color.blue(val) - IMAGE_MEAN / IMAGE_STD;
        }
        return floatValues;
    }

    private void sendImage() {

        //final List<Classifier.Recognition> results = classifier.recognizeImage(imagebitmap);


        //https://stackoverflow.com/questions/15759195/reduce-size-of-bitmap-to-some-specified-pixel-in-android
        //final List<Classifier.Recognition> results = classifier.recognizeImage2(getPixels(getResizedBitmap(imagebitmap,INPUT_SIZE,INPUT_SIZE)));
        final List<Classifier.Recognition> results = classifier.recognizeImage(getResizedBitmap(imagebitmap,INPUT_SIZE,INPUT_SIZE));



        //final List<Classifier.Recognition> results = classifier.recognizeImage2(getPixels(imagebitmap));


        makeToast(results.toString());


    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
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

        //startActivityForResult(cameraIntent, 3);
    }

    private void startGalleryActivity() {
        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select image from:"), 2);
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
