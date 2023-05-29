package view;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    public GameBeginFrame gameBeginFrame;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;
    JLabel background;
    public final JLabel dayBG;
    public final JLabel nightBG;
    JLabel timeLabel;
    JLabel turnLabel;

    public ChessboardComponent chessboardComponent;
    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        //chessboardComponent.isDay = true;

        addTimeLabel();
        addChessboard();
        addCurrentPlayerLabel();
        addCurrentStepsrLabel2();
        addblueWinLabel2();
        adddrawLabel2();
        addredWinLabel2();
        addResetButton();
        addSaveButton();
        addLoadButton();
        addRegretButton();
        addEasyAIButton();
        addHardAIButton();
        addChangeThemeButton();
        addBackButton();
        addPlayBackButton();
        addAutoPlayBackButton();
        addDrawButton();
        addSurrenderButton();
        addAutoClickButton();

        Image picture = new ImageIcon("Resource/day1.jpg").getImage();
        picture = picture.getScaledInstance(1100, 810, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(picture);
        dayBG = new JLabel(icon);
        dayBG.setSize(1100, 810);
        dayBG.setLocation(0, 0);

        picture = new ImageIcon("Resource/night.jpg").getImage();
        picture = picture.getScaledInstance(1100, 810, Image.SCALE_DEFAULT);
        icon = new ImageIcon(picture);
        nightBG = new JLabel(icon);
        nightBG.setSize(1100, 810);
        nightBG.setLocation(0, 0);

        background = dayBG;
        add(background);
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE, timeLabel,this);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */

    JLabel currentPlayerLabel;
    private void addCurrentPlayerLabel() {
        currentPlayerLabel = new JLabel("Current player: BLUE" );
        currentPlayerLabel.setLocation(HEIGTH, HEIGTH / 10 - 50);
        currentPlayerLabel.setSize(600, 60);
        currentPlayerLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(currentPlayerLabel);
    }

    public void setCurrentPlayerLabel(int i){
        if(i == 1){
            currentPlayerLabel.setText("Current Player: RED");
        }else{
            currentPlayerLabel.setText("Current Player: BLUE");
        }
        currentPlayerLabel.repaint();
    }

    JLabel stepsLabel;
    private void addCurrentStepsrLabel2() {
        stepsLabel = new JLabel("CurrentSteps: 1" );
        stepsLabel.setLocation(HEIGTH, HEIGTH / 10 - 90);
        stepsLabel.setSize(600, 60);
        stepsLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(stepsLabel);
    }

    public void setCurrentStepsLabel2(int i){

        stepsLabel.setText("CurrentSteps:"+i);
        stepsLabel.repaint();
    }

    JLabel blueWinLabel;
    private void addblueWinLabel2() {
        blueWinLabel = new JLabel("BlueWin: 0" );
        blueWinLabel.setLocation(200, HEIGTH / 10 - 70);
        blueWinLabel.setSize(600, 60);
        blueWinLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(blueWinLabel);
    }

    public void setBlueWinLabel2(int i){

        blueWinLabel.setText("BlueWin: "+i);
        blueWinLabel.repaint();
    }
    JLabel redWinLabel;
    private void addredWinLabel2() {
        redWinLabel = new JLabel("RedWin: 0" );
        redWinLabel.setLocation(600, HEIGTH / 10 - 70);
        redWinLabel.setSize(600, 60);
        redWinLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(redWinLabel);
    }

    public void setredWinLabel2(int i){

        redWinLabel.setText("RedWin: "+i);
        redWinLabel.repaint();
    }

    JLabel drawLabel;
    private void adddrawLabel2() {
        drawLabel = new JLabel("DrawChess: 0" );
        drawLabel.setLocation(380, HEIGTH / 10 - 70);
        drawLabel.setSize(600, 60);
        drawLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(drawLabel);
    }

    public void setdrawLabel2(int i){

        drawLabel.setText("DrawChess: "+i);
        drawLabel.repaint();
    }
    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addTimeLabel() {
        timeLabel = new JLabel("Time: 10");
        timeLabel.setLocation(20, HEIGHT / 10 + 30);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 32));
        add(timeLabel);
    }
    private void addResetButton() {
        JButton button = new JButton("Reset");
        //button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGTH, HEIGTH / 10 + 140);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            try {
                chessboardComponent.gameController.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //chessboardComponent.gameController.model.outputCell();
            //chessboardComponent.gameController.timer.run();
        });

        add(button);
    }

    private void addSurrenderButton() {

        JButton button = new JButton("Surrender");
        //button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(10, HEIGTH / 10 );
        button.setSize(150, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener( (e) -> {
            try {
                chessboardComponent.gameController.surrender();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //chessboardComponent.gameController.model.outputCell();
        });

        add(button);
    }

    private void addDrawButton() {

        JButton button = new JButton("Draw");
        //button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(10, HEIGTH / 10 + 90);
        button.setSize(150, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener( (e) -> {
            try {
                chessboardComponent.gameController.draw();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //chessboardComponent.gameController.model.outputCell();
        });

        add(button);
    }
    private void addSaveButton()
    {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 205);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener( (e) -> {
            System.out.println("Click save");
            String path = JOptionPane.showInputDialog("存档名");

            while (path.equals("")) {
                JOptionPane.showMessageDialog(null, "存档名不能为空");
                path = JOptionPane.showInputDialog("存档名");
            }

            try {
                chessboardComponent.gameController.saveGame(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 270);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(null,"Input Path here");


            try {
                chessboardComponent.gameController.loadGame(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void addRegretButton()
    {
        JButton button = new JButton("Regret");
        button.setLocation(HEIGTH, HEIGTH / 10 + 335);
        button.setSize(200,55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            try {
                chessboardComponent.gameController.regret();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(button);
    }
    private void addEasyAIButton()
    {
        //timeLabel.setVisible(false);
        JButton button = new JButton("EasyAI");
        button.setLocation(HEIGTH, HEIGTH / 10 + 400);
        button.setSize(200,55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            if(chessboardComponent.gameController.easyAIBomb == 0)
            {
                chessboardComponent.gameController.easyAI = true;
                //chessboardComponent.gameController.timer.stop();
                chessboardComponent.gameController.easyAIBomb = 1;
                timeLabel.setVisible(false);

            }
            else
            {
                chessboardComponent.gameController.easyAI = false;
                //chessboardComponent.gameController.timer.run();/////////the program will collapse
                chessboardComponent.gameController.easyAIBomb = 0;
                timeLabel.setVisible(true);
            }
        });
        add(button);
    }
    private void addHardAIButton()
    {
        //timeLabel.setVisible(false);
        JButton button = new JButton("HardAI");
        button.setLocation(HEIGTH, HEIGTH / 10 + 465);
        button.setSize(200,55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            if(chessboardComponent.gameController.hardAIBomb == 0)
            {
                chessboardComponent.gameController.hardAI = true;
                //chessboardComponent.gameController.timer.stop();
                chessboardComponent.gameController.hardAIBomb = 1;
                timeLabel.setVisible(false);
            }
            else
            {
                chessboardComponent.gameController.hardAI = false;
                //chessboardComponent.gameController.timer.run();/////////the program will collapse
                chessboardComponent.gameController.hardAIBomb = 0;
                timeLabel.setVisible(true);
            }

        });
        add(button);
    }
    private void addChangeThemeButton()
    {
        JButton button = new JButton("Change Theme");
        button.setLocation(HEIGTH, HEIGTH / 10 + 530);
        button.setSize(200,55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click change theme");
            //repaint();
            //addChessboard();///////////////////////////////////////////
            if (chessboardComponent.isDay){
                remove(background);
                chessboardComponent.turnNight();
                background = nightBG;
                add(background);
                //chessboardComponent.initiateGridComponents();
                System.out.println("day");
            } else {
                remove(background);
                chessboardComponent.turnDay();
                background = dayBG;
                add(background);
                //chessboardComponent.initiateGridComponents();
                System.out.println("night");
            }
            repaint();
            revalidate();
        });
    }
    private void addBackButton() {
        JButton button = new JButton("Back");
        button.setLocation(HEIGTH, HEIGTH / 10 + 595);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click back");
            this.setVisible(false);
            gameBeginFrame.setVisible(true);
            try {
                chessboardComponent.gameController.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void addPlayBackButton()
    {
        JButton button = new JButton("PlayBack");
        button.setLocation(HEIGTH, HEIGTH / 10 + 75);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            try {
                chessboardComponent.gameController.playBack();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(button);

    }
    private void addAutoPlayBackButton()
    {
        JButton button = new JButton("AutoPlayBack");
        button.setLocation(HEIGTH, HEIGTH / 10 + 10);
        button.setSize(200, 55);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        button.addActionListener( (e) -> {
            robotStart();
        });
        add(button);

    }

    private void addAutoClickButton()
    {
        JButton button = new JButton("LazyGuyNeedIt");
        button.setLocation(10, HEIGTH / 10 + 180);
        button.setSize(150, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener( (e) -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    chessboardComponent.gameController.autoClick();
                }
            },1000);
        });
    }

    public void robotStart() {
        // 显示主界面
        int x = this.getX();
        int y = this.getY();

        Run run = new Run(x,y,HEIGTH);
        Thread thread = new Thread(run);
        run.setThread(thread);
        thread.start();
    }
}
