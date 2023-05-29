package view;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;

public class Run2 implements Runnable{


    boolean go ;
    Thread thread;

    @Override
    public void run() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        // 模拟移动到当前鼠标位置
        while (go){
            Point point = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(point.x, point.y);
            // 模拟鼠标按下左键
            System.out.println("开始执行机器人");
            robot.mousePress(InputEvent.BUTTON1_MASK);
            // 模拟鼠标松开左键
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(go);
    }

    public void setGo (boolean go){
        this.go =go;
    }

    public void setThread(Thread thread){
        this.thread = thread;

    }
}
