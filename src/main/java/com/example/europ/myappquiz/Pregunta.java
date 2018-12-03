package com.example.europ.myappquiz;

import android.media.Image;

public class Pregunta {

    private String question;
    private String rightAns;

    private final String[] respuestas;
    private Image imgPregunta;


    // Constructor de Preguntas de Tipo: Texto-to-Texto
    public Pregunta(String question, String rightAns, String ans1, String ans2, String ans3){

        this.question = question;
        this.rightAns = rightAns;

        respuestas = new String[4];

        respuestas[0] = rightAns;
        respuestas[1] = ans1;
        respuestas[2] = ans2;
        respuestas[3] = ans3;

    }

    // Constructor de Preguntas de Tipo: Imagen-to-Texto
    public Pregunta(String question, Image imgPregunta, String rightAns, String ans1, String ans2, String ans3){
        this.question = question;
        this.rightAns = rightAns;
        this.imgPregunta = imgPregunta;

        respuestas = new String[4];

        respuestas[0] = rightAns;
        respuestas[1] = ans1;
        respuestas[2] = ans2;
        respuestas[3] = ans3;
    }


    public void aleatorizar() {

        int [] numeros = new int[4];

        int i = 0;
        int a;

        while(i<4){
            a = (int)(Math.random()*3);
            for(int j = i; j>=0 ; j--) {
                if (!(a == numeros[j])) // si no se ha puesto.
                    numeros[i] = a;
            }

            i++;
        }

        for(i = 0; i<4; i++){

            if(!(i==numeros[i])){
                String aux =  respuestas[i];
                respuestas[i] = respuestas[numeros[i]];
                respuestas[numeros[i]]=aux;
            }
        }
    }

    //----------------------- Comprobación -------------------------//

    public boolean esCorrecto (String respuesta){
        return this.rightAns == respuesta;
    }
    public boolean esTextToText (){
        if(imgPregunta==null)
            return true;
        else
            return false;
    }

    public boolean esImgToText (){
        if(imgPregunta!=null)
            return true;
        else
            return false;
    }

    //--------------------------- SETS ----------------------------//

    public void setQuestion(String question){
        this.question = question;
    }

    public void setRightAns(String rightAns){
        this.rightAns = rightAns;
    }

    //--------------------------- GETS ----------------------------//

    public String getAns1() {
        return respuestas[0];
    }

    public String getAns2() {
        return respuestas[1];
    }

    public String getAns3() {
        return respuestas[2];
    }

    public String getAns4() {
        return respuestas[3];
    }

    public String getQuestion() {
        return question;
    }

    public String getRightAns() {
        return rightAns;
    }

    //-------------------------- EQUALS ----------------------------//

    @Override
    public boolean equals(Object obj) {

        if(this==obj) return true;
        if(this==null) return false;
        if(this.getClass()!=obj.getClass()) return false;

        Pregunta pregunta = (Pregunta) obj;
        return (pregunta.question == this.question);
    }

    public String[] getArrayAns() {
        return respuestas;
    }
}
