package com.example.europ.myappquiz;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PuntosActivity extends AppCompatActivity {

    private Bundle bundle;
    private boolean recibido;
    private Button btn_salir;
    private Button btn_reinciar;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5;

    private TextView tvAciertos;
    private TextView tvPuntos;

    ImageView imageView;

    String[] questions;
    String[] respuestasDadas;
    Boolean[] aciertos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos);

        tvAciertos=(TextView)findViewById(R.id.textView_aciertosNum);
        tvPuntos=(TextView)findViewById(R.id.textView_puntuacionNum);
        imageView = (ImageView)findViewById(R.id.iv_estrellas);
        bundle = this.getIntent().getExtras();

        btn_reinciar=(Button)findViewById(R.id.btn_reiniciar);
        btn_salir=(Button)findViewById(R.id.btn_salir);

        btn_1=(Button)findViewById(R.id.button1);
        btn_2=(Button)findViewById(R.id.button2);
        btn_3=(Button)findViewById(R.id.button3);
        btn_4=(Button)findViewById(R.id.button4);
        btn_5=(Button)findViewById(R.id.button5);

        if(bundle!=null)
            recibido = true;
        else
            recibido = false;

    }

    @Override
    protected void onStart(){
        super.onStart();

        questions = new String[5];
        respuestasDadas = new String[5];
        aciertos = new Boolean[5];

        if(recibido){

            tvAciertos.setText(""+bundle.getInt("aciertos"));
            tvPuntos.setText(""+bundle.getInt("puntos"));
        }
        else{
            tvAciertos.setText("N/A");
            tvPuntos.setText("N/A");
        }

        if(bundle.getInt("aciertos")==5){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.tres_estrellas));
        }
        else if(bundle.getInt("aciertos")<5 && bundle.getInt("aciertos")>2){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.dos_estrellas));
        }
        else
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.una_estrella));

        inicializarBotones();

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRespuesta(1);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRespuesta(2);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRespuesta(3);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRespuesta(4);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRespuesta(5);
            }
        });
    }

    private void mostrarRespuesta(int i) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.textFallo);

        if(aciertos[i-1])
            builder.setTitle(R.string.textExito);
        builder.setMessage(questions[i-1]+"\nHas respondido: "+respuestasDadas[i-1]);

        Dialog dialog = builder.create();
        dialog.show();

    }

    private void inicializarBotones() {

        for(int i = 1; i<=5; i++){
            questions[i-1]=bundle.getString("question_"+i);
            respuestasDadas[i-1] = bundle.getString("respuestaDada_"+i);
            aciertos[i-1] = bundle.getBoolean("acertoEnPregunta_"+i);
        }

        if(aciertos[0])
            btn_1.setBackgroundColor(Color.GREEN);
        else
            btn_1.setBackgroundColor(Color.RED);
        if(aciertos[1])
            btn_2.setBackgroundColor(Color.GREEN);
        else
            btn_2.setBackgroundColor(Color.RED);
        if(aciertos[2])
            btn_3.setBackgroundColor(Color.GREEN);
        else
            btn_3.setBackgroundColor(Color.RED);
        if(aciertos[3])
            btn_4.setBackgroundColor(Color.GREEN);
        else
            btn_4.setBackgroundColor(Color.RED);
        if(aciertos[4])
            btn_5.setBackgroundColor(Color.GREEN);
        else
            btn_5.setBackgroundColor(Color.RED);
    }

    public void salir(View view){
        finish();
    }

    public void reiniciar(View view){
        Intent intent = new Intent(PuntosActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
