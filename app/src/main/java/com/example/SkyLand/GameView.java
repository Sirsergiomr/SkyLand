package com.example.SkyLand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;



@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements SensorEventListener {
    private final GameLoopThread gameLoopThread;
    Paint pincel = new Paint();

    Paint pincelBorde = new Paint();
    Paint pincelBorde2 = new Paint();
    Paint pincelFondo = new Paint();
    Paint pincelTexto = new Paint();
    float alto, ancho;
    static boolean animacion = false;
    static boolean  touch = false;
    int tamanio= 50;
    int borde = 12;
    private float ejeX, ejeY=658,ejeZ=0;
    static int lv=2;
    private final com.example.SkyLand.Pinchos Pinchos = new Pinchos();
    private final ObstaculoCubo Cubo = new ObstaculoCubo();
    private float sumador = 0;

    public GameView(Context context, SensorManager sensorManager, Sensor acelerometerSensor) {
        super(context);
        sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        Display pantalla = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        ancho = pantalla.getWidth();
        alto = pantalla.getHeight();

        gameLoopThread = new GameLoopThread(this);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("Alto y Ancho "+ alto+", "+ancho);
                gameLoopThread.setRunning(true);
                gameLoopThread.start();

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    public  void setEjeX(float eje) {
        ejeX = eje;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        PlayerMove(event);
    }
    //Control de Player y Colisiones con el marco----------------------------
    public void PlayerMove(SensorEvent event){
        ejeX+= Math.floor(event.values[1]); 
        if (ejeX < (tamanio + borde)){
            ejeX = (tamanio+borde);

        }else if(ejeX > (ancho-(tamanio+borde))){
            ejeX = ancho-(tamanio+borde);
        }
        if(ejeX == ancho-tamanio-borde && ejeY>= (alto / 2)+(alto/4) && ejeY <= alto){
            if(lv==0||lv==1){
                ejeX = 62;
                System.out.println("Pasas de nivel");
                lv++;
            }

        }
        Salto();
        ejeZ = event.values[2];
        invalidate();
    }
    public void Salto(){
        //salto   720 -50 -12 - 50 =608
        if(ejeY < alto-tamanio-borde-sumador && animacion){
            touch = false;
            ejeY = ejeY+4;
            sumador =0;
        }else if(ejeY == alto-tamanio-borde-sumador){
            animacion = false;

        }
        if(touch == true && ejeY >550-sumador ){
            ejeY = ejeY-4;
            if(ejeY == 550-sumador){
                animacion = true;
            }
        }
    }
//---------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("OntouchEvent");
        touch = true;
        animacion = true;
        return super.onTouchEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void setSumador(float sumador) {
        this.sumador = sumador;
    }

    public  void setAnimacion(boolean animacion) {
        GameView.animacion = animacion;
    }

    public  void setTouch(boolean touch) {
        GameView.touch = touch;
    }

    @Override
    public void onDraw(Canvas lienzo){

        if(lienzo != null) {

            pincelFondo.setColor(Color.BLACK);
            pincelBorde.setColor(Color.rgb(255, 000, 000));
            pincelBorde.setStyle(Paint.Style.STROKE);
            pincelBorde.setStrokeWidth(10);

            lienzo.drawRect(0, 0, ancho, alto, pincelFondo);
            lienzo.drawRect(0, 0, ancho, alto, pincelBorde);

            pincelBorde2.setColor(Color.GREEN);
            pincelBorde2.setStyle(Paint.Style.STROKE);
            pincelBorde2.setStrokeWidth(20);

            pincelTexto.setColor(Color.GREEN);
            pincelTexto.setTextSize(40);
            pincelTexto.setTextAlign(Paint.Align.CENTER);
            Typeface fuente = Typeface.create(String.valueOf(Typeface.BOLD), Typeface.NORMAL);
            pincelTexto.setTypeface(fuente);
            lienzo.drawText("EjeX= " + ejeX + " EjeY= " + ejeY, ancho / 2, (alto / 2), pincelTexto);
            Player(lienzo);
            lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
            lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
            lienzo.drawText("NIVEL "+lv, ancho / 2, 40, pincelTexto);

            switch (lv){
                case 0:
                    lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
                    MoverPincho(296,lienzo);
                    MoverPincho(658,lienzo);
                    Pinchos.Pincho(lienzo,1000,alto,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,ancho/2,alto-100,ejeX,ejeY,tamanio,borde,this);
                    break;
                case 1:

                    MoverPincho(296,lienzo);
                    MoverPincho(658,lienzo);
                    MoverPincho2(lienzo,ancho/2,alto,200);

                    Pinchos.Pincho(lienzo,1135, 555,ejeX,ejeY,tamanio,borde,this);
                    break;
                case 2:

                    Pinchos.Pincho(lienzo,758, 555,ejeX,ejeY,tamanio,borde,this);
                    Cubo.Cubo(lienzo,758,658,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,758, alto-20,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,700, alto-20,ejeX,ejeY,tamanio,borde,this);

                    Pinchos.Pincho(lienzo,458, 555,ejeX,ejeY,tamanio,borde,this);
                    Cubo.Cubo(lienzo,458,658,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,458, alto-20,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,400, alto-20,ejeX,ejeY,tamanio,borde,this);

                    MoverPincho(ancho/2+200,lienzo);
                    MoverPincho2(lienzo,1095,alto,900);

                    MoverPincho2(lienzo,1095,alto-100,900);
                    break;
                default: lv = 1;
                break;
            }

        }
    }

    public void Player(Canvas lienzo) {
        pincel.setStrokeWidth(15);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setColor(Color.rgb(255, 000, 000));
        lienzo.drawRect(ejeX, ejeY,ejeX + 50, ejeY + 50, pincel);
    }

    static float i = 600;
    public void MoverPincho(float posX,Canvas lienzo){
            if(i >= 500){
                Pinchos.Pincho(lienzo,posX,i,ejeX,ejeY,tamanio,borde,this);
                i--;
            }
            if(i== 500){
                i= alto;
                Pinchos.Pincho(lienzo,posX,i,ejeX,ejeY,tamanio,borde,this);
            }
    }

    static float j= -1;
    static float k= -1;
    public void MoverPincho2(Canvas lienzo,float startX ,float startY,float endX){
        if(j==-1 ){
            j = (float) Math.floor(startX);
            k = (float) Math.floor(startY);
        }
        if(j>endX){
            Pinchos.Pincho(lienzo,j,k,ejeX,ejeY,tamanio,borde,this);
            j--;
        }else if(j==endX){
            j=startX;
            Pinchos.Pincho(lienzo,j,k,ejeX,ejeY,tamanio,borde,this);
        }
        lienzo.drawText("EjeX= " + j + " EjeY= " + k, ancho / 2, (alto / 2)/2, pincelTexto);
    }
}
