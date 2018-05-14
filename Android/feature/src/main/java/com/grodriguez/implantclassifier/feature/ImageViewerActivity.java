package com.grodriguez.implantclassifier.feature;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class ImageViewerActivity extends Activity implements View.OnClickListener {

    private  Button cancel;
    private Button search;
    private Uri imageCaptureURI;
    private ImageView imageView;
    Boolean permis = false;
    private Bitmap imagebitmap;
    private Classifier classifier;

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_layout);
        cancel = (Button) findViewById(R.id.returnBtn);
        cancel.setOnClickListener(this);
        search = (Button) findViewById(R.id.searchBtn);
        search.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
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


        Intent intent = getIntent();
        imagebitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        imageView.setImageBitmap(imagebitmap);

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }







    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchBtn) {
            if(imagebitmap!=null) {
                makeToast("Sent");
                sendImage();
            }
            else
                makeToast("Please select an image");
        }
        else if (v.getId() == R.id.returnBtn)
            finish();
    }

    private void makeToast(String s){
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        s, Toast.LENGTH_SHORT);

        toast1.show();
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
}
