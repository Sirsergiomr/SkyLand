package com.example.SkyLand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.sql.Time;


@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements SensorEventListener {
    private final GameLoopThread gameLoopThread;
    private final Timer timer;
    int time = 40;
    int timeset= -1;
    Paint pincel = new Paint();
    Paint pincelBorde = new Paint();
    Paint pincelBorde2 = new Paint();
    Paint pincelFondo = new Paint();
    Paint pincelTexto = new Paint();
    boolean win= false;
    private int vidas = 3;
    static  float alto, ancho;
    static boolean animacion = false;
    static boolean  touch = false;
    int tamanio= 50;
    int borde = 12;
    private float ejeX, ejeY;
    static int lv=0;
    private final com.example.SkyLand.Pinchos Pinchos = new Pinchos();
    private final ObstaculoCubo Cubo = new ObstaculoCubo();
    private float sumador = 0;
    static float j= -1;
    static float k= -1;
    MediaPlayer mediaPlayer;
    public GameView(Context context, SensorManager sensorManager, Sensor acelerometerSensor) {
        super(context);
        sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        Display pantalla = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        ancho = pantalla.getWidth();
        alto = pantalla.getHeight();
        ejeY = alto-tamanio-borde;
        mediaPlayer = MediaPlayer.create(context,R.raw.bandasonora);
        mediaPlayer.setLooping(true);
        gameLoopThread = new GameLoopThread(this);
        timer = new Timer(this);
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
                timer.start();

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        PlayerMove(event);
    }
    //Control de Player y Colisiones con el marco----------------------------
    public void PlayerMove(SensorEvent event){
        if( vidas >= 1){
            ejeX+= Math.floor(event.values[1]);
        }else{
            ejeX = 62;
        }

        if (ejeX < (tamanio + borde) &&vidas >= 1){
            ejeX = (tamanio+borde);

        }else if(ejeX > (ancho-(tamanio+borde)) && vidas >= 1){
            ejeX = ancho-(tamanio+borde);
        }
        if(ejeX == ancho-tamanio-borde && ejeY>= (alto / 2)+(alto/4) && ejeY <= alto && vidas >= 1){
            ejeX =62;
            if(lv==0 ||lv==1||lv==2){
                System.out.println("Pasas de nivel");
                    lv++;
                j=-1;
                k=-1;
            }
        }
        Salto();
        invalidate();
    }
    public void Salto(){
        //salto   720 -50 -12 - 50 =608
        if(ejeY < alto-tamanio-borde-sumador && animacion && vidas >= 1){
            touch = false;
            ejeY = ejeY+4;
            sumador =0;
        }else if(ejeY == alto-tamanio-borde-sumador&& vidas >= 1){
            animacion = false;

        }
        if(touch == true && ejeY >Math.floor((alto/2)+(alto/4))-tamanio-borde-sumador && vidas >= 1){
            ejeY = ejeY-4;
            if(ejeY == Math.floor((alto/2)+(alto/4))-tamanio-borde-sumador&& vidas >= 1){
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

    public void setSumador(float sumador) { this.sumador = sumador; }

    public  void setAnimacion(boolean animacion) {GameView.animacion = animacion; }

    public  void setTouch(boolean touch) { GameView.touch = touch;}

    public  void setEjeX(float eje) { ejeX = eje;}

    public void setVidas(int vidas) {this.vidas = vidas;}

    public int getVidas() {return vidas;}

    public int getTime() {return time;}

    public void setTime(int time) { this.time = time;}

    public int getLv() { return lv; }

    public  void setLv(int lv) {GameView.lv = lv; }

    public boolean isWin() {return win;}

    public void setWin(boolean win) { this.win = win;}

    public void setTimeset(int timeset) {this.timeset = timeset;}

    @Override
    public void onDraw(Canvas lienzo){

        if(lienzo != null) {

            pincelFondo.setColor(Color.CYAN);
            pincelBorde.setColor(Color.BLACK);
            pincelBorde.setStyle(Paint.Style.STROKE);
            pincelBorde.setStrokeWidth(10);

            lienzo.drawRect(0, 0, ancho, alto, pincelFondo);
            lienzo.drawRect(0, 0, ancho, alto, pincelBorde);

            pincelBorde2.setColor(Color.GREEN);
            pincelBorde2.setStyle(Paint.Style.STROKE);
            pincelBorde2.setStrokeWidth(20);

            pincelTexto.setColor(Color.BLACK);
            pincelTexto.setTextSize(40);
            pincelTexto.setTextAlign(Paint.Align.CENTER);
            Typeface fuente = Typeface.create(String.valueOf(Typeface.BOLD), Typeface.NORMAL);
            pincelTexto.setTypeface(fuente);
            if(win == false && vidas > 0){
                lienzo.drawText("Time :  " + time + " s ", (ancho / 2)+(ancho/3), (alto / 6), pincelTexto);
            }

            lienzo.drawText("Ejex :  " + ejeX, (ancho / 2), (alto / 6), pincelTexto);
            if(vidas == 3 ){
                Cubo.Cubo(lienzo,(ancho/3), (alto / 6),ejeX,ejeY,tamanio,borde,this);
                Cubo.Cubo(lienzo,(ancho/4), (alto / 6),ejeX,ejeY,tamanio,borde,this);
                Cubo.Cubo(lienzo,(ancho/6), (alto / 6),ejeX,ejeY,tamanio,borde,this);

            }else if(vidas ==2 ){
                Cubo.Cubo(lienzo,(ancho/4), (alto / 6),ejeX,ejeY,tamanio,borde,this);
                Cubo.Cubo(lienzo,(ancho/6), (alto / 6),ejeX,ejeY,tamanio,borde,this);

            }else if(vidas ==1){
                Cubo.Cubo(lienzo,(ancho/4), (alto / 6),ejeX,ejeY,tamanio,borde,this);
            }

            if(lv<3 && vidas >= 1 && time != 0){
                lienzo.drawText("NIVEL "+lv, (ancho / 2), 40, pincelTexto);
                Player(lienzo);
                lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
                lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
            }else if (lv<3 && vidas < 1 ){
                lienzo.drawText("You lost in level"+lv, (ancho / 2), 40, pincelTexto);

                if(timeset == -1){
                    timeset = time;
                }
                lienzo.drawText("Time :  " + timeset + " s ", (ancho / 2)+(ancho/3), (alto / 6), pincelTexto);
            }else if (lv == 3 && vidas>=1){
                Player(lienzo);
            }

            switch (lv){
                case 0:

                    lienzo.drawLine(ancho,alto,ancho,(alto / 2)+(alto/4),pincelBorde2);
                    MoverPincho(296,lienzo);
                    MoverPincho(658,lienzo);
                    Pinchos.Pincho(lienzo,ancho-(ancho/4),alto,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,ancho/2,alto-100,ejeX,ejeY,tamanio,borde,this);
                    break;
                case 1:

                    MoverPincho(296,lienzo);
                    MoverPincho(658,lienzo);
                    MoverPincho2(lienzo,(float) Math.floor((ancho/2)),alto,200);
                    Pinchos.Pincho(lienzo,ancho-(ancho/4), (alto/2)+(alto/4),ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,ancho-(ancho/6), (alto/2)+(alto/3),ejeX,ejeY,tamanio,borde,this);

                    break;
                case 2:
                    Pinchos.Pincho(lienzo,(ancho/3)-tamanio-borde, (alto/2)+(alto/4),ejeX,ejeY,tamanio,borde,this);

                    Cubo.Cubo(lienzo,(ancho/3)-tamanio-borde,alto-tamanio-borde,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,(ancho/3)-tamanio-borde, alto-20,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,(ancho/5), alto,ejeX,ejeY,tamanio,borde,this);

                    Pinchos.Pincho(lienzo,ancho-(ancho/2)-tamanio-borde, (alto/2)+(alto/4),ejeX,ejeY,tamanio,borde,this);// ^

                    Cubo.Cubo(lienzo,ancho-(ancho/2)-tamanio-borde,alto-tamanio-borde,ejeX,ejeY,tamanio,borde,this);      // []
                    Pinchos.Pincho(lienzo,ancho-(ancho/2)-tamanio-borde, alto-20,ejeX,ejeY,tamanio,borde,this);
                    Pinchos.Pincho(lienzo,(float) Math.floor((ancho/2)-(alto/7)-(tamanio/2)), alto,ejeX,ejeY,tamanio,borde,this);// ^^


                    MoverPincho2(lienzo,(float) Math.floor(ancho-(ancho/6)),alto,(float) Math.floor((ancho/2)+(ancho/5)));
                    MoverPincho(ancho/2+200,lienzo);
                    break;
                case 3:
                    lienzo.drawText("YOU WIN!!", ancho / 2, 40, pincelTexto);
                    win = true;
                    if(timeset == -1){
                        timeset = time;
                    }
                    lienzo.drawText("Time :  " + timeset + " s ", (ancho / 2)+(ancho/3), (alto / 6), pincelTexto);
                    break;
            }

        }
    }

    public void Player(Canvas lienzo) {
        pincel.setStrokeWidth(15);
        pincel.setStyle(Paint.Style.FILL_AND_STROKE);
        pincel.setColor(Color.BLACK);
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
        //lienzo.drawText("EjeX= " + j + " EjeY= " + k, ancho / 2, (alto / 2)/2, pincelTexto);
    }


}
