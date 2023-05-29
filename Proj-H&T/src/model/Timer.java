package model;

import controller.GameController;

import java.io.IOException;

public class Timer extends Thread
{
    public static int time = 11;
    public GameController gameController;
    @Override
    public void run(){
        synchronized (this){
            while (true){
                PlayerColor player = gameController.currentPlayer;
                boolean b = true;
                while(time > 0) {
                    time--;
                    try {
                        Thread.sleep(1000);
                        gameController.timeLabel.setText("Time: " + time);
                        if (gameController.currentPlayer != player){
                            gameController.swapColor();
                            b = false;
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 11;

                if (b)///////////////////time = 0
                {
                    if(gameController.easyAIBomb == 0 && gameController.hardAIBomb == 0)
                    {
                        gameController.easyAI = true;
                        gameController.easyAImove();
                        gameController.easyAI = false;
                    }
                    try {
                        gameController.checkWin();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (gameController.winNumber != 0)////////////////////someone win
                    {
                        //controller.doWin();
                        try {
                            gameController.reset();
                        } catch (IOException e) {
                            throw new RuntimeException(e);

                        }
                    }
                }
                else
                {
                    gameController.swapColor();
                }
            }
        }

    }

    public Timer(GameController gameController){
        this.gameController = gameController;
    }
}
