package com.example.SkyLand;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pinchos  {

    Paint pincelPinchos = new Paint();
    //Canvas Principal
    public  void Pincho(Canvas lienzo, float posX, float posY, float ejeX, float ejeY, int tamanio, int borde ,GameView view){
            pincelPinchos.setStyle(Paint.Style.FILL);
            pincelPinchos.setStrokeWidth(8);
            pincelPinchos.setColor(Color.BLACK);

            lienzo.drawLine(posX, posY, posX + 25, posY - 25, pincelPinchos);//A - B
            lienzo.drawLine(posX + 25, posY - 25, posX + 50, posY, pincelPinchos);//B-C
            lienzo.drawLine(posX + 50, posY, posX, posY, pincelPinchos);//C-A

            if (ejeX <= posX + 50 && ejeX >= posX && ejeY >= posY - tamanio - borde && ejeY <= posY) {

                view.setEjeX(62);
                view.setSumador(0);
                view.setAnimacion(true);
                System.out.println("Colision pincho");

                int vidas = view.getVidas();
                view.setVidas(vidas-1);
            }
    }

}