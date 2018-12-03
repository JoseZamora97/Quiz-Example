package com.example.europ.myappquiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.LinkedList;

public class JuegoActivity extends AppCompatActivity {

    //---------------------------------------------------------------------------------------------
    //      VARIABLES INTERNAS      ---------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    private LinkedList<Pregunta> _colaDPreguntas;
    private Pregunta _pregunta;
    private int _numeroDPregunta;
    private int _aciertos;
    private int _puntos;
    private Bundle bundle;

    //---------------------------------------------------------------------------------------------
    //      COMPONENTES     -----------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    private LinearLayout ll;

    private TextView textView_preguntaActual;
    private TextView textView_numPreg;
    private TextView textView_puntuacion;

    private ImageView imagePregunta_cuatro;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private RadioGroup radioGroup;

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button btnCheckRadios;

    private TableLayout tbl;
    private TableRow tableRow1;
    private TableRow tableRow2;

    private ImageButton ibtn1;
    private ImageButton ibtn2;
    private ImageButton ibtn3;
    private ImageButton ibtn4;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Button btnCheckSpinner;
    private ListView listView;
    private int pos;

    //---------------------------------------------------------------------------------------------
    //      MÉTODOS     ---------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        imagePregunta_cuatro = (ImageView)findViewById(R.id.imgview_pregunta_cuatro);

        textView_puntuacion = (TextView)findViewById(R.id.textView_numPuntos);
        textView_preguntaActual=(TextView)findViewById(R.id.textView_preguntaActual);
        textView_numPreg=(TextView)findViewById(R.id.textView_numPregunta);

        ll = (LinearLayout)findViewById(R.id.linearLayoutRespuestas);
        ll.setGravity(Gravity.CENTER);
        ll.removeAllViewsInLayout();

        bundle = new Bundle();
    }

    @Override
    protected void onStart() {
        super.onStart();

        _numeroDPregunta = 0;
        _puntos = 0;
        _aciertos = 0;
        _colaDPreguntas = construirPreguntas();

        montarPregunta();

    }

    @Override
    protected void onPause() {
        super.onPause();

        ll.removeAllViewsInLayout();
    }

    @Override
    protected void onStop() {
        super.onStop();

        ll.removeAllViewsInLayout();
    }

    // FUNCIONES DE CONTROL Y ACTUALIZACION.-------------------------------------------------------
    private void montarPregunta() {

        textView_puntuacion.setText(" "+_puntos);
        _numeroDPregunta++;

        if(_numeroDPregunta<=5) {

            textView_numPreg.setText(" " + _numeroDPregunta);
            _pregunta = _colaDPreguntas.peek();
            _colaDPreguntas.poll();

            if(_numeroDPregunta==4)
                imagePregunta_cuatro.setVisibility(View.VISIBLE);
            else
                imagePregunta_cuatro.setVisibility(View.INVISIBLE);

            if(_numeroDPregunta!=5)
                mostrarTextToText((int)(Math.random()*4));
            else
                mostrarTextToImages();
        }
        else
            mostrarPuntuaciones();
    }

    private void exito(){
        mensajeDeExito();
    }
    private void fallo(){
        mensajeDeFallo();
    }

    private void mensajeDeExito() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.textExito);
        builder.setMessage(R.string.textMsgExito);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.textContinuar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                _puntos+=3;
                _aciertos++;

                bundle.putBoolean("acertoEnPregunta_"+_numeroDPregunta, true);
                bundle.putString("question_"+_numeroDPregunta, _pregunta.getQuestion());

                ll.removeAllViewsInLayout();
                montarPregunta();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
    private void mensajeDeFallo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.textFallo);
        builder.setMessage(R.string.textMsgFallo);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.textContinuar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(_puntos-2<=0)
                   _puntos = 0;
                else
                    _puntos-=2;

                bundle.putBoolean("acertoEnPregunta_"+_numeroDPregunta, false);
                bundle.putString("question_"+_numeroDPregunta, _pregunta.getQuestion());

                ll.removeAllViewsInLayout();
                montarPregunta();
            }
        });
        builder.setNegativeButton(R.string.textReiniciar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
    private void mostrarPuntuaciones() {

        Intent toPuntuacion   = new Intent (JuegoActivity.this, PuntosActivity.class);

        bundle.putInt("puntos",_puntos);
        bundle.putInt("aciertos",_aciertos);

        toPuntuacion.putExtras(bundle);
        finish();
        startActivity(toPuntuacion);

    }

    // FUNCIONES PARA COMPROBAR SOLUCIONES.--------------------------------------------------------
    private void comprobarBoton(String s) {

        bundle.putString("respuestaDada_"+_numeroDPregunta, s);

        if(s.equals(_pregunta.getRightAns()))
            exito();
        else
            fallo();
    }
    private void comprobarRadios() {

        if( !(!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked() && !rb4.isChecked() )){
            if(rb1.isChecked()){

                bundle.putString("respuestaDada_"+_numeroDPregunta, rb1.getText().toString());

                if(rb1.getText().toString().equals(_pregunta.getRightAns()))
                    exito();
                else
                    fallo();
            }
            else if(rb2.isChecked()){

                bundle.putString("respuestaDada_"+_numeroDPregunta, rb2.getText().toString());

                if(rb2.getText().toString().equals(_pregunta.getRightAns()))
                    exito();
                else
                    fallo();
            }
            else if(rb3.isChecked()){

                bundle.putString("respuestaDada_"+_numeroDPregunta, rb3.getText().toString());

                if(rb3.getText().toString().equals(_pregunta.getRightAns()))
                    exito();
                else
                    fallo();
            }
            else{

                bundle.putString("respuestaDada_"+_numeroDPregunta, rb4.getText().toString());

                if(rb4.getText().toString().equals(_pregunta.getRightAns()))
                    exito();
                else
                    fallo();
            }
        }
    }
    private void comprobarSpinner(int pos) {

        bundle.putString("respuestaDada_"+_numeroDPregunta, _pregunta.getArrayAns()[pos]);

        if(_pregunta.getArrayAns()[pos].equals(_pregunta.getRightAns()))
            exito();
        else
            fallo();
    }
    private void comprobarListView(int pos){
        comprobarSpinner(pos);
    }

    // FUNCIONES PARA PARA GENERAR LOS CONTROLES.--------------------------------------------------
    private void mostrarTextToImages() {
        textView_preguntaActual.setText(_pregunta.getQuestion());
        _pregunta.aleatorizar();

        generateTextToImagesByButtons();
    }
    private void mostrarTextToText(int o) {

        _pregunta.aleatorizar();

        textView_preguntaActual.setText(_pregunta.getQuestion());

        if(o==0){
            // AQUI GENERO LOS CONTROLES DE TIPO BOTON.
            generateTextToTextByButtons();

            btn1.setText(_pregunta.getAns1());
            btn2.setText(_pregunta.getAns2());
            btn3.setText(_pregunta.getAns3());
            btn4.setText(_pregunta.getAns4());

        }
        else if(o==1){
            // AQUI GENERO LOS CONTROLES DE TIPO RADIO.
            generateTextToTextByRadioButtons();

            rb1.setText(_pregunta.getAns1());
            rb2.setText(_pregunta.getAns2());
            rb3.setText(_pregunta.getAns3());
            rb4.setText(_pregunta.getAns4());

            btnCheckRadios.setText(getResources().getString(R.string.btn_check));
        }
        else if(o==2){
            // AQUI GENERO LOS CONTROLES DE TIPO SPINNER.
            generateTextToTextBySpinner();
        }
        else
            // AQUI GENERO LOS CONTROLES DE TIPO LISTVIEW.
            generateTextToTextByListView();
    }

    private void generateTextToImagesByButtons() {
        // IMAGEN 1
        ibtn1 = new ImageButton(this);
        ibtn2 = new ImageButton(this);
        ibtn3 = new ImageButton(this);
        ibtn4 = new ImageButton(this);

        tbl = new TableLayout(this);
        tableRow1 = new TableRow(this);
        tableRow2 = new TableRow(this);


        if(_pregunta.getAns1().equals(getResources().getString(R.string.a5_ans1))) { // edge
            ibtn1.setContentDescription(_pregunta.getAns1());
            ibtn1.setImageDrawable(getResources().getDrawable(R.drawable.edge_logo_preg_cinco));
        }
        else if(_pregunta.getAns1().equals(getResources().getString(R.string.a5_ans2))) { // opera
            ibtn1.setContentDescription(_pregunta.getAns1());
            ibtn1.setImageDrawable(getResources().getDrawable(R.drawable.opera_logo_preg_cinco));
        }
        else if(_pregunta.getAns1().equals(getResources().getString(R.string.a5_ans3))) { // safari
            ibtn1.setContentDescription(_pregunta.getAns1());
            ibtn1.setImageDrawable(getResources().getDrawable(R.drawable.safari_logo_preg_cinco));
        }
        else if(_pregunta.getAns1().equals(getResources().getString(R.string.a5_rightAns))) { // google
            ibtn1.setContentDescription(_pregunta.getAns1());
            ibtn1.setImageDrawable(getResources().getDrawable(R.drawable.google_logo_preg_cinco));
        }

        // IMAGEN 2

        if(_pregunta.getAns2().equals(getResources().getString(R.string.a5_ans1))) { // edge
            ibtn2.setContentDescription(_pregunta.getAns2());
            ibtn2.setImageDrawable(getResources().getDrawable(R.drawable.edge_logo_preg_cinco));
        }
        else if(_pregunta.getAns2().equals(getResources().getString(R.string.a5_ans2))) { // opera
            ibtn2.setContentDescription(_pregunta.getAns1());
            ibtn2.setImageDrawable(getResources().getDrawable(R.drawable.opera_logo_preg_cinco));
        }
        else if(_pregunta.getAns2().equals(getResources().getString(R.string.a5_ans3))) { // safari
            ibtn2.setContentDescription(_pregunta.getAns2());
            ibtn2.setImageDrawable(getResources().getDrawable(R.drawable.safari_logo_preg_cinco));
        }
        else if(_pregunta.getAns2().equals(getResources().getString(R.string.a5_rightAns))) { // google
            ibtn2.setContentDescription(_pregunta.getAns2());
            ibtn2.setImageDrawable(getResources().getDrawable(R.drawable.google_logo_preg_cinco));
        }

        // IMAGEN 3

        if(_pregunta.getAns3().equals(getResources().getString(R.string.a5_ans1))) { // edge
            ibtn3.setContentDescription(_pregunta.getAns3());
            ibtn3.setImageDrawable(getResources().getDrawable(R.drawable.edge_logo_preg_cinco));
        }
        else if(_pregunta.getAns3().equals(getResources().getString(R.string.a5_ans2))) { // opera
            ibtn3.setContentDescription(_pregunta.getAns1());
            ibtn3.setImageDrawable(getResources().getDrawable(R.drawable.opera_logo_preg_cinco));
        }
        else if(_pregunta.getAns3().equals(getResources().getString(R.string.a5_ans3))) { // safari
            ibtn3.setContentDescription(_pregunta.getAns3());
            ibtn3.setImageDrawable(getResources().getDrawable(R.drawable.safari_logo_preg_cinco));
        }
        else if(_pregunta.getAns3().equals(getResources().getString(R.string.a5_rightAns))) { // google
            ibtn3.setContentDescription(_pregunta.getAns3());
            ibtn3.setImageDrawable(getResources().getDrawable(R.drawable.google_logo_preg_cinco));
        }


        // IMAGEN 4

        if(_pregunta.getAns4().equals(getResources().getString(R.string.a5_ans1))) { // edge
            ibtn4.setContentDescription(_pregunta.getAns4());
            ibtn4.setImageDrawable(getResources().getDrawable(R.drawable.edge_logo_preg_cinco));
        }
        else if(_pregunta.getAns4().equals(getResources().getString(R.string.a5_ans2))) { // opera
            ibtn4.setContentDescription(_pregunta.getAns4());
            ibtn4.setImageDrawable(getResources().getDrawable(R.drawable.opera_logo_preg_cinco));
        }
        else if(_pregunta.getAns4().equals(getResources().getString(R.string.a5_ans3))) { // safari
            ibtn4.setContentDescription(_pregunta.getAns4());
            ibtn4.setImageDrawable(getResources().getDrawable(R.drawable.safari_logo_preg_cinco));
        }
        else if(_pregunta.getAns4().equals(getResources().getString(R.string.a5_rightAns))) { // google
            ibtn4.setContentDescription(_pregunta.getAns4());
            ibtn4.setImageDrawable(getResources().getDrawable(R.drawable.google_logo_preg_cinco));
        }

        ibtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(ibtn1.getContentDescription().toString());
            }
        });

        ibtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(ibtn2.getContentDescription().toString());
            }
        });

        ibtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(ibtn3.getContentDescription().toString());
            }
        });

        ibtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(ibtn4.getContentDescription().toString());
            }
        });

        ll.addView(tbl);

        tbl.addView(tableRow1);
        tbl.addView(tableRow2);

        tableRow1.addView(ibtn1);
        tableRow1.addView(ibtn2);
        tableRow2.addView(ibtn3);
        tableRow2.addView(ibtn4);

        ibtn1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ibtn2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ibtn3.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ibtn4.setScaleType(ImageView.ScaleType.FIT_CENTER);

        TableRow.LayoutParams params = new TableRow.LayoutParams(350, 350);
        params.setMargins(25,25, 25, 25);

        ibtn1.setLayoutParams(params);
        ibtn2.setLayoutParams(params);
        ibtn3.setLayoutParams(params);
        ibtn4.setLayoutParams(params);

        ibtn1.setAdjustViewBounds(true);
        ibtn2.setAdjustViewBounds(true);
        ibtn3.setAdjustViewBounds(true);
        ibtn4.setAdjustViewBounds(true);

        ibtn1.setBackgroundColor(Color.WHITE);
        ibtn2.setBackgroundColor(Color.WHITE);
        ibtn3.setBackgroundColor(Color.WHITE);
        ibtn4.setBackgroundColor(Color.WHITE);

        ibtn1.setBackground(getResources().getDrawable(R.drawable.boton_imagenes));
        ibtn2.setBackground(getResources().getDrawable(R.drawable.boton_imagenes));
        ibtn3.setBackground(getResources().getDrawable(R.drawable.boton_imagenes));
        ibtn4.setBackground(getResources().getDrawable(R.drawable.boton_imagenes));

        tableRow2.setGravity(Gravity.CENTER);
        tableRow1.setGravity(Gravity.CENTER);

        tbl.setGravity(Gravity.CENTER);
    }
    private void generateTextToTextByButtons() {

        btn1=new Button(this);
        btn2=new Button(this);
        btn3=new Button(this);
        btn4=new Button(this);

        ll.addView(btn1);
        ll.addView(btn2);
        ll.addView(btn3);
        ll.addView(btn4);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 140);
        params.setMargins(25,25,25,25);

        btn1.setLayoutParams(params);
        btn2.setLayoutParams(params);
        btn3.setLayoutParams(params);
        btn4.setLayoutParams(params);

        btn1.setTextSize(18);
        btn2.setTextSize(18);
        btn3.setTextSize(18);
        btn4.setTextSize(18);

        btn1.setBackground(this.getResources().getDrawable(R.drawable.boton_fondo));
        btn2.setBackground(this.getResources().getDrawable(R.drawable.boton_fondo));
        btn3.setBackground(this.getResources().getDrawable(R.drawable.boton_fondo));
        btn4.setBackground(this.getResources().getDrawable(R.drawable.boton_fondo));

        // Cambiar Font-family a los botones.

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(btn1.getText().toString());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(btn2.getText().toString());
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(btn3.getText().toString());
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarBoton(btn4.getText().toString());
            }
        });
    }
    private void generateTextToTextByRadioButtons() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800,140);
        params.setMargins(25,25,25,25);
        RadioGroup.LayoutParams rgParams = new RadioGroup.LayoutParams(700,120);
        rgParams.setMargins(15,15,15,15);
        radioGroup = new RadioGroup(this);

        btnCheckRadios = new Button(this);
        rb1= new RadioButton(this);
        rb2= new RadioButton(this);
        rb3= new RadioButton(this);
        rb4= new RadioButton(this);

        rb1.setLayoutParams(rgParams);
        rb2.setLayoutParams(rgParams);
        rb3.setLayoutParams(rgParams);
        rb4.setLayoutParams(rgParams);

        btnCheckRadios.setTextSize(18);
        rb1.setTextSize(18);
        rb2.setTextSize(18);
        rb3.setTextSize(18);
        rb4.setTextSize(18);

        radioGroup.addView(rb1);
        radioGroup.addView(rb2);
        radioGroup.addView(rb3);
        radioGroup.addView(rb4);

        btnCheckRadios.setLayoutParams(params);

        btnCheckRadios.setBackground(getResources().getDrawable(R.drawable.boton_fondo));
        btnCheckRadios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarRadios();
            }
        });

        ll.addView(radioGroup);
        ll.addView(btnCheckRadios);
    }
    private void generateTextToTextBySpinner(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800,120);
        params.setMargins(25,25,25,25);

        spinner = new Spinner(this, Spinner.MODE_DIALOG);
        adapter =  new  ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, _pregunta.getArrayAns());


        spinner.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        spinner.setAdapter(adapter);
        spinner.setLayoutParams(params);
        spinner.setPrompt(getResources().getString(R.string.spinnerPrompt));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada
            }
        });

        btnCheckSpinner = new Button(this);

        btnCheckSpinner.setLayoutParams(params);
        btnCheckSpinner.setTextSize(18);
        btnCheckSpinner.setText(getResources().getString(R.string.btn_check));
        btnCheckSpinner.setBackground(getResources().getDrawable(R.drawable.boton_fondo));

        ll.addView(spinner);
        ll.addView(btnCheckSpinner);

        btnCheckSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    comprobarSpinner(pos);
            }
        });
    }
    private void generateTextToTextByListView(){

        listView = new ListView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800,500);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(800, 120);
        params1.setMargins(25,25,25,25);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, _pregunta.getArrayAns());

        TextView tv = new TextView(this);

        tv.setText(getResources().getString(R.string.spinnerPrompt));
        tv.setTextSize(20);
        tv.setTextColor(getResources().getColor(R.color.colorAccent));
        tv.setLayoutParams(params1);

        listView.setAdapter(adapter);
        listView.setLayoutParams(params);
        listView.setBackground(getResources().getDrawable(R.drawable.boton_fondo));

        ll.addView(tv);
        ll.addView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                comprobarListView(position);
            }
        });
    }

    // FUNCION QUE TRAE DE @STRING LAS PREGUNTAS.--------------------------------------------------
    private LinkedList<Pregunta> construirPreguntas() {

        LinkedList<Pregunta> p = new LinkedList<>();


        // PREGUNTA 1
        String p1_question = getResources().getString(R.string.a1_question);
        String p1_rightAns = getResources().getString(R.string.a1_rightAns);
        String p1_ans1 = getResources().getString(R.string.a1_ans1);
        String p1_ans2 = getResources().getString(R.string.a1_ans2);
        String p1_ans3 = getResources().getString(R.string.a1_ans3);

        p.add(new Pregunta(p1_question,p1_rightAns,p1_ans1,p1_ans2,p1_ans3));


        // PREGUNTA 2
        String p2_question = getResources().getString(R.string.a2_question);
        String p2_rightAns = getResources().getString(R.string.a2_rightAns);
        String p2_ans1 = getResources().getString(R.string.a2_ans1);
        String p2_ans2 = getResources().getString(R.string.a2_ans2);
        String p2_ans3 = getResources().getString(R.string.a2_ans3);

        p.add(new Pregunta(p2_question,p2_rightAns,p2_ans1,p2_ans2,p2_ans3));


        // PREGUNTA 3
        String p3_question = getResources().getString(R.string.a3_question);
        String p3_rightAns = getResources().getString(R.string.a3_rightAns);
        String p3_ans1 = getResources().getString(R.string.a3_ans1);
        String p3_ans2 = getResources().getString(R.string.a3_ans2);
        String p3_ans3 = getResources().getString(R.string.a3_ans3);

        p.add(new Pregunta(p3_question,p3_rightAns,p3_ans1,p3_ans2,p3_ans3));


        // PREGUNTA 4
        String p4_question = getResources().getString(R.string.a4_question);
        String p4_rightAns = getResources().getString(R.string.a4_rightAns);
        String p4_ans1 = getResources().getString(R.string.a4_ans1);
        String p4_ans2 = getResources().getString(R.string.a4_ans2);
        String p4_ans3 = getResources().getString(R.string.a4_ans3);

        p.add(new Pregunta(p4_question,p4_rightAns,p4_ans1,p4_ans2,p4_ans3));

        // PREGUNTA 5
        String p5_question = getResources().getString(R.string.a5_question);
        String p5_rightAns = getResources().getString(R.string.a5_rightAns);
        String p5_ans1 = getResources().getString(R.string.a5_ans1);
        String p5_ans2 = getResources().getString(R.string.a5_ans2);
        String p5_ans3 = getResources().getString(R.string.a5_ans3);

        p.add(new Pregunta(p5_question,p5_rightAns,p5_ans1,p5_ans2,p5_ans3));

        return p;
    }
}
