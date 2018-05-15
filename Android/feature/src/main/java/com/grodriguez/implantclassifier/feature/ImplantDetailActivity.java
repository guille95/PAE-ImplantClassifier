package com.grodriguez.implantclassifier.feature;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ImplantDetailActivity extends Activity implements View.OnClickListener {


    TextView result1Text;
    TextView result2Text;
    TextView result3Text;
    TextView result1Value;
    TextView result2Value;
    TextView result3Value;
    String result1;
    String result2;
    String result3;
    Integer result1Val;
    Integer result2Val;
    Integer result3Val;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.implant_detail_layout);


        result1Text = (TextView) findViewById(R.id.result1Text);
        result2Text = (TextView) findViewById(R.id.result2Text);
        result3Text = (TextView) findViewById(R.id.result3Text);
        result1Value = (TextView) findViewById(R.id.result1Val);
        result2Value = (TextView) findViewById(R.id.result2Val);
        result3Value = (TextView) findViewById(R.id.result3Val);


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

        result1Text.setText(intent.getStringExtra("result1"));
        Float f = intent.getFloatExtra("result1int",0)*100;
        result1Value.setText(f.toString()+"%");

    }


        @Override
        public void onClick(View v) {

        }
}
