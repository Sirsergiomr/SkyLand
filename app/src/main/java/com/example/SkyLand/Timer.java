package com.example.SkyLand;

public class Timer extends Thread{
    GameView view;

    public Timer(GameView view){
        this.view = view;
    }
    @Override
    public void run(){
            while(view.getTime()>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                view.setTime(view.getTime()-1);

                if(view.getTime() == 20 && view.isWin() == false|| view.getTime() == 15&& view.isWin() == false || view.getTime() == 0 && view.isWin() == false){
                    view.setVidas(view.getVidas()-1);
                }

            }
    }
}
