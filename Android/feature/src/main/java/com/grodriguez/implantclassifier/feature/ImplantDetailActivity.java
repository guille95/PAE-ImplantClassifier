package com.grodriguez.implantclassifier.feature;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImplantDetailActivity extends Activity implements View.OnClickListener {


    TextView result1Text;
    TextView result2Text;
    TextView result3Text;
    TextView result1Value;
    TextView result2Value;
    TextView result3Value;
    TextView implantName;
    TextView implantDetails;
    String result1;
    String result2;
    String result3;
    Integer result1Val;
    Integer result2Val;
    Integer result3Val;
    Button linkbtn;
    String url;
    ImageView image;
    private String path;
    int result = 0; //bench 1 //nob 2 // coral 3




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.implant_detail_layout);

        linkbtn = (Button) findViewById(R.id.implantWebBtn);
        linkbtn.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.imageViewMedium);

        result1Text = (TextView) findViewById(R.id.result1Text);
        result2Text = (TextView) findViewById(R.id.result2Text);
        result3Text = (TextView) findViewById(R.id.result3Text);
        result1Value = (TextView) findViewById(R.id.result1Val);
        result2Value = (TextView) findViewById(R.id.result2Val);
        result3Value = (TextView) findViewById(R.id.result3Val);
        implantName = (TextView) findViewById(R.id.implantName);
        implantDetails = (TextView) findViewById(R.id.implantDetails);


        Intent intent = getIntent();/*
        result1 = (String) intent.getParcelableExtra("result1Text");
        result2 = (String) intent.getParcelableExtra("result2Text");
        result3 = (String) intent.getParcelableExtra("result3Text");
        result1Val = (Integer) intent.getParcelableExtra("result1Val");
        result2Val = (Integer) intent.getParcelableExtra("result2Val");
        result3Val = (Integer) intent.getParcelableExtra("result3Val");
*/

        /*
        result1Text.setText(result1);
        result2Text.setText(result2);
        result3Text.setText(result3);
        result1Value.setText(result1Val);
        result2Value.setText(result2Val);
        result3Value.setText(result3Val);
*/


        result1Text.setText(getName(intent.getStringExtra("result1")));
        makeToast(getName(intent.getStringExtra("result1")));
        if(intent.getStringExtra("result1").equals("branemarkmkiii"))
            result = 1;
        else if (intent.getStringExtra("result1").equals("nobelspeedygroovy"))
            result = 2;
        else
            result = 3;
        Float f = intent.getFloatExtra("result1int",0)*100;
        result1Value.setText(String.format("%.2f", f)+"%");
        String r2 = intent.getStringExtra("result2");
        String r3 = intent.getStringExtra("result3");
        Float f2 = intent.getFloatExtra("result2int",0)*100;
        Float f3 = intent.getFloatExtra("result3int",0)*100;
        if(r2 != null){
            result2Text.setText(getName(r2));
//            result2Value.setText(f2.toString()+"%");
            result2Value.setText(String.format("%.2f", f2)+"%");
        }
        if(r3 != null){
            result3Text.setText(getName(r3));
            result3Value.setText(String.format("%.2f", f3)+"%");
        }


//        byte[] byteArray = getIntent().getByteArrayExtra("image");
//        Bitmap imagebitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        image.setImageBitmap(imagebitmap);

        path = getIntent().getStringExtra("imagePath");
        Bitmap imagebitmap = BitmapFactory.decodeFile(path);
        image.setImageBitmap(imagebitmap);

        url = "https://www.google.com";
        //URL + DETAILS
        if(result == 1){
            //branemarkmkiii
            url = "https://www.nobelbiocare.com/es/es/home/products-and-solutions/implant-systems/branemark-system.html";
            implantName.setText("Branemark MK III");
            implantDetails.setText("Details....");

        }
        else if(result == 2){
            //nobelspeedygroovy
         url = "https://www.nobelbiocare.com/es/es/home/products-and-solutions/implant-systems/nobelspeedy.html";
            implantName.setText("Nobel Speedy Groovy");
            implantDetails.setText("Details....");

        }
        else{
            //avinentcoral
            url = "https://implant-system.avinent.com/es/producto/sistema-coral";
            implantName.setText("Avinent Coral");
            implantDetails.setText("Details....");

        }


    }


        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);*/
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);

        }
        private String getName(String s){
            String name;
            if(s.equals("branemarkmkiii"))
                name = "Branemark MK III";
            else if(s.equals("nobelspeedygroovy"))
                name = "Nobel Speedy Groovy";
            else
                name = "Avinent Coral";
             return name;
        }

    private void makeToast(String s){
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        s, Toast.LENGTH_SHORT);

        toast1.show();
    }
}
