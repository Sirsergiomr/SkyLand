package com.example.SkyLand;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    GameView dibujo;
    private SensorManager sensorManager;
    private Sensor acelerometerSensor;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores;
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listaSensores.isEmpty()) {
            // Cogemos el primer sensor de tipo TYPE_ACCELEROMETER que tenga el dispositivo
            acelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            dibujo = new GameView(this,sensorManager,acelerometerSensor);
        }

        setContentView(dibujo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(mediaPlayer != null){
            mediaPlayer.release();
        }
            // Crea el media player
            mediaPlayer = MediaPlayer.create(this,R.raw.randommelodies);
            mediaPlayer.setLooping(true);
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        System.out.println("ON PAUSE");
        mediaPlayer.stop();
    }
    @Override
    protected void onResume() {

        super.onResume();
        sensorManager.registerListener(this,acelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        System.out.println("ON RESUME");
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this,R.raw.randommelodies);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    @Override public void onPointerCaptureChanged(boolean hasCapture) {}

    @Override public void onSensorChanged(SensorEvent event) {}

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}