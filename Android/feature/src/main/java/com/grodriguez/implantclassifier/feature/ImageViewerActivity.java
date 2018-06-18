package com.grodriguez.implantclassifier.feature;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ImageViewerActivity extends Activity implements View.OnClickListener {

    private Button cancel;
    private Button search;
    private ImageView imageView;
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_layout);
        cancel = (Button) findViewById(R.id.returnBtn);
        cancel.setOnClickListener(this);
        search = (Button) findViewById(R.id.searchBtn);
        search.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageViewBig);
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

        path = getIntent().getStringExtra("imagePath");
        imagebitmap = BitmapFactory.decodeFile(path);
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
                makeToast("Enviado");
                List<Classifier.Recognition> result = sendImage();
                Intent intent = new Intent(ImageViewerActivity.this, ImplantDetailActivity.class);


                imagebitmap=getResizedBitmap(imagebitmap,480,270);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image",byteArray);
                intent.putExtra("imagePath",path);

                if(result.size() >= 2){
                    intent.putExtra("result2",result.get(1).getTitle());
                    intent.putExtra("result2int",result.get(1).getConfidence());
                }
                if(result.size() >= 3){
                    intent.putExtra("result3",result.get(2).getTitle());
                    intent.putExtra("result3int",result.get(2).getConfidence());
                }

                intent.putExtra("result1",result.get(0).getTitle());
                intent.putExtra("result1int",result.get(0).getConfidence());

                startActivity(intent);
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

    private List<Classifier.Recognition> sendImage() {

        final List<Classifier.Recognition> results = classifier.recognizeImage(getResizedBitmap(imagebitmap,INPUT_SIZE,INPUT_SIZE));

        return results;

    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
}
