package com.example.europ.myappquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_comenzar;

    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_comenzar =(Button)findViewById(R.id.btn_botones);
    }

    //-------------------------------------------------------

    public void botonesIsClicked(View v){

        Intent toJuego = new Intent(MainActivity.this , JuegoActivity.class);
        finish();
        startActivity(toJuego);
    }
}
