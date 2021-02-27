package com.example.SkyLand;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ObstaculoCubo {
    Pinchos Pinchos = new Pinchos();
    Paint pincelCubo = new Paint();
    public void Cubo(Canvas lienzo,float posX,float top, float ejeX,float ejeY,  int tamanio, int borde,GameView view){


        pincelCubo.setColor(Color.CYAN);
        pincelCubo.setStyle(Paint.Style.FILL_AND_STROKE);
        pincelCubo.setStrokeWidth(12);
        lienzo.drawRect(posX,top,posX+50,top+50,pincelCubo);

        if (ejeX <= posX + 62 && ejeX >= posX-62 && ejeY >= top - tamanio - borde && ejeY <= top ) {
            view.setSumador(62);
            view.setAnimacion(true);
            view.setTouch(true);
        }
    }
}
