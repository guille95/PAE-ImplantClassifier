package com.grodriguez.implantclassifier.feature;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

    @Override
    public void onClick(View v) {
       // startActivity(new Intent(MainActivity.this,ImagePickerActivity));

    }


}
