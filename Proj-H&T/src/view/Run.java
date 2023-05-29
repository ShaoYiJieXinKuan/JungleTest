package view;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;

public class Run implements Runnable{
    int x ;
    int y;

    int height;
    Thread thread;

    @Override
    public void run() {
        File saveStepsFiles = new File("save\\Jungle");
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        int i = 1;
        File [] files = saveStepsFiles.listFiles();
        // 模拟移动到当前鼠标位置
        while (i <= files.length) {
            robot.mouseMove(x+height+100, y+height/10+130);
            // 模拟鼠标按下左键
            robot.mousePress(InputEvent.BUTTON1_MASK);
            // 模拟鼠标松开左键
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
    public Run (int x, int y,int height){
        this.x = x;
        this.y = y;
        this.height = height;
    }


    public void setThread(Thread thread){
        this.thread = thread;

    }
}