package controller;


import listener.GameListener;
import model.*;
import model.Timer;
import view.CellComponent;
import view.ChessboardComponent;
import view.Run2;
import view.TotalChessComponent;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {


    public Chessboard model;
    public ChessboardComponent view;
    public PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    //写在move()方法被调用之后，自增一次
    public int steps = 1;
    File saveStepsFiles = new File("save\\Jungle");
    public boolean easyAI = false;
    public boolean hardAI = false;
    public JLabel timeLabel;
    public static Timer timer;
    public int winNumber = 0;
    public int playBackNumber = 1;
    public int easyAIBomb = 0;
    public int hardAIBomb = 0;

    //新建棋局的时候同时新建了一个文件夹
    public GameController(ChessboardComponent view, Chessboard model) throws IOException {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        timeLabel = view.timeLabel;
        deleteLastSaveFile();
        saveStepsMethod();

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // after a valid move swap the player
    public void swapColor()
    {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        view.chessGameFrame.setCurrentPlayerLabel(currentPlayer == PlayerColor.RED?1:0);

    }
   //收到赢的信号后发生的事情
    int blueWin = 0;
    int redWin = 0;
   public void checkWin() throws IOException {
       int countRed = 0;
       int countBlue = 0;
       for (int i = 0; i < 9; i++) {
           for (int j = 0; j < 7; j++) {
               if (model.getChessPieceAt(new ChessboardPoint(i,j)) != null){
                   if (model.getChessPieceAt(new ChessboardPoint(i,j)).getOwner().equals(PlayerColor.RED)){
                       countRed++;
                   }
                   else {
                       countBlue++;
                   }
               }
           }
       }

       ArrayList <ChessboardPoint> reaminedMovable = searchChess();
       if (reaminedMovable.size() == 0){
           if (currentPlayer.equals(PlayerColor.RED)){
               JOptionPane.showMessageDialog(null, "        蓝方获胜！！！\n      点击确定，再开一把",
                       "Win!!!", JOptionPane.INFORMATION_MESSAGE);
               blueWin++;
               view.chessGameFrame.setredWinLabel2(blueWin);
               reset();
           }
           else {
               JOptionPane.showMessageDialog(null, "        红方获胜！！！\n      点击确定，再开一把",
                       "Win!!!", JOptionPane.INFORMATION_MESSAGE);
               redWin++;
               view.chessGameFrame.setBlueWinLabel2(redWin);
               reset();
           }
       }


       //我的部分
       if (model.getChessPieceAt(new ChessboardPoint(0,3))!= null || countRed == 0) {
           new SoundEffect().playEffect("Resource\\win.wav");
           JOptionPane.showMessageDialog(null, "        蓝方获胜！！！\n      点击确定，再开一把",
                   "Win!!!", JOptionPane.INFORMATION_MESSAGE);
           blueWin++;
           view.chessGameFrame.setBlueWinLabel2(blueWin);
           reset();
       }
       //蓝方获胜场景
       if (model.getChessPieceAt(new ChessboardPoint(8,3))!= null || countBlue == 0){
           new SoundEffect().playEffect("Resource\\win.wav");
           JOptionPane.showMessageDialog(null, "        红方获胜！！！\n      点击确定，再开一把",
                   "Win!!!", JOptionPane.INFORMATION_MESSAGE);
           redWin++;
           view.chessGameFrame.setredWinLabel2(redWin);
           reset();
           //红方获胜场景
       }

   }
    public void surrender () throws IOException {
        if (currentPlayer.equals(PlayerColor.RED)){
            JOptionPane.showMessageDialog(null, "      红方投子认负  蓝方获胜！！！\n      点击确定，再开一把",
                    "Win!!!", JOptionPane.INFORMATION_MESSAGE);
            reset();
            blueWin++;
            view.chessGameFrame.setBlueWinLabel2(blueWin);
        }
        else {
            JOptionPane.showMessageDialog(null, "      蓝方投子认负  红方获胜！！！\n      点击确定，再开一把",
                    "Win!!!", JOptionPane.INFORMATION_MESSAGE);
            reset();
            redWin++;
            view.chessGameFrame.setredWinLabel2(redWin);
        }

    }
    //public int getSteps() {
    //    return steps;
    //}

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) throws IOException {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            steps++;
            view.chessGameFrame.setCurrentStepsLabel2(steps);
            saveStepsMethod();
            checkWin();
            easyAImove();
            hardAImove();
            checkWin();

            // TODO: if the chess enter Dens or Traps and so on
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, TotalChessComponent component) throws IOException {
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        }
        else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        }
        // TODO: Implement capture function
        else{
            System.out.println();
            if (model.isValidCapture(selectedPoint,point)){
                model.captureChessPiece(selectedPoint,point);
                model.moveChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                view.setChessComponentAtGrid(point,view.removeChessComponentAtGrid(selectedPoint));
                //System.out.println(view.getGridComponents()[4][1].getComponentCount());
                view.repaint();
                selectedPoint = null;
                swapColor();
                steps++;
                view.chessGameFrame.setCurrentStepsLabel2(steps);
                saveStepsMethod();
                checkWin();
                easyAImove();
                hardAImove();
                checkWin();
                //model.outputCell();
            }
        }
    }

    //重开：绑定在重开按钮上

    public void reset () throws IOException {
        //先把棋子全部清除
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <7; j++) {
                this.model.getGrid()[i][j].removePiece();
                this.view.removeChessComponentAtGrid(new ChessboardPoint(i,j));
            }
        }
        timeLabel.setVisible(true);
        //turnLabel.setVisible(true);
        easyAIBomb = 0;
        hardAIBomb = 0;
        view.repaint();
        //再重新摆好棋子
        model.initPieces();
        //清空存档文件夹
        deleteLastSaveFile();
        steps = 1;
        view.chessGameFrame.setCurrentStepsLabel2(steps);
        easyAI = false;
        hardAI = false;
        saveStepsMethod();
        //确保蓝方先手
        this.currentPlayer = PlayerColor.BLUE;
        //接下来是你的部分
        this.view.initiateChessComponent(model);
    }
    //对于每一步棋的存储
    //写在move后面
    public void deleteLastSaveFile(){
        File [] files = saveStepsFiles.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }
    public void saveStepsMethod() throws IOException {
        byte [] data = new byte[65];
        int countNumber = 1;

        //向数组中导入信息
        if (this.currentPlayer.equals(PlayerColor.BLUE)){
            data[0] = 99;
        }
        else {
            data[0] = 100;
        }
        data[1] = (byte) steps;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                countNumber++;
                if (this.model.getGrid()[i][j].getPiece() != null){
                    if (this.model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("鼠")){
                            data[countNumber] = 1;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("猫")){
                            data[countNumber] = 2;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("狗")){
                            data[countNumber] = 3;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("狼")){
                            data[countNumber] = 4;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("豹")){
                            data[countNumber] = 5;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("虎")){
                            data[countNumber] = 6;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("狮")){
                            data[countNumber] = 7;
                        }
                        if (this.model.getGrid()[i][j].getPiece().getName().equals("象")){
                            data[countNumber] = 8;
                        }
                    }
                if (this.model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("鼠")){
                        data[countNumber] = 11;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("猫")){
                        data[countNumber] = 12;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("狗")){
                        data[countNumber] = 13;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("狼")){
                        data[countNumber] = 14;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("豹")){
                        data[countNumber] = 15;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("虎")){
                        data[countNumber] = 16;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("狮")){
                        data[countNumber] = 17;
                    }
                    if (this.model.getGrid()[i][j].getPiece().getName().equals("象")){
                        data[countNumber] = 18;
                    }
                }
            }
                else{
                    data[countNumber] = 0;
                }
            }
        }
        //储存数组

        FileOutputStream fos = new FileOutputStream("save\\Jungle\\step"+steps+".txt");
        fos.write(data);
        fos.close();
    }

    // 存档
    // 存档 汤易博5/23
    public void saveGame (String fileName) throws IOException {
        String location = "save\\" + fileName;
        File playerSaveFile = new File(location);
        //5/22汤易博
        playerSaveFile.mkdir();
        //5/22汤易博

        File [] files = saveStepsFiles.listFiles();

        for(File file : files){
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File(playerSaveFile,file.getName()));
            byte [] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1){
                fos.write(bytes,0,len);
            }
            fos.close();
            fis.close();
        }
    }

    //定义一个根据存档重新摆棋子的方法
    //汤易博 5/23
    public void setPiecesAccordingToFile (int [] position){
        //先把棋子清空
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <7; j++) {
                this.model.getGrid()[i][j].removePiece();
                this.view.removeChessComponentAtGrid(new ChessboardPoint(i,j));
            }
        }
        view.repaint();
        //再确定是红方还是蓝方
        int countNumber = 1;
        if(position[0] == 99){
            currentPlayer=PlayerColor.BLUE;
        }
        else {
            currentPlayer=PlayerColor.RED;
        }

        steps = position[1];
        view.chessGameFrame.setCurrentStepsLabel2(steps);

        //根据传入数组摆好棋子

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                countNumber++;
                if (position[countNumber] == 1){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "鼠",1));
                }
                if (position[countNumber] == 2){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "猫",2));
                }
                if (position[countNumber] == 3){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狗",3));
                }
                if (position[countNumber] == 4){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狼",4));
                }
                if (position[countNumber] == 5){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "豹",5));
                }
                if (position[countNumber] == 6){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "虎",6));
                }
                if (position[countNumber] == 7){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狮",7));
                }
                if (position[countNumber] == 8){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "象",8));
                }
                if (position[countNumber] == 11){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "鼠",1));
                }
                if (position[countNumber] == 12){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "猫",2));
                }
                if (position[countNumber] == 13){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狗",3));
                }
                if (position[countNumber] == 14){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狼",4));
                }
                if (position[countNumber] == 15){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "豹",5));
                }
                if (position[countNumber]  == 16){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "虎",6));
                }
                if (position[countNumber]  == 17){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狮",7));
                }
                if (position[countNumber]  == 18){
                    model.getGrid()[i][j].setPiece(new ChessPiece(PlayerColor.RED, "象",8));
                }
            }
        }

        //再渲染好图片
        this.view.initiateChessComponent(model);
    }

    //定义一个悔棋的方法
    //绑定在悔棋按钮上
    //汤易博 5/23
    public void regret () throws IOException {
        if (steps == 1){
            reset();
            return;
        }
        //model.turn = model.turn - 1;///////////////////////////////////
        //先删掉上一步的存档
        //汤易博 5/23
        File deleteFile = new File("save\\Jungle\\step"+(steps)+".txt");
        deleteFile.delete();
        //读取上上个存档
        FileInputStream fis = new FileInputStream("save\\Jungle\\step"+(steps-1)+".txt");
        int b ;
        int countNumber = 0;
        int [] position = new int[65];
        while ((b = fis.read())!= -1){
            position[countNumber] = b;
            countNumber++;
        }
        //根据存档摆棋子
        setPiecesAccordingToFile(position);
        System.out.println("in the regret method ,step is"+steps);
    }

    //读档
    //绑定在读档按钮上
    //得把读取文件放进标准文件夹中
    //汤易博 5/23
    public void loadGame(String fileName) throws IOException {
        if(fileName.equals(" "))
        {
            return;
        }
        //找到要读取的文件
        String location = "save\\"+fileName ;
        File playerSaveFile = new File(location);

        File [] files = playerSaveFile.listFiles();
        //读取文件并载入棋盘
        //为何我不能直接 File loadFile = files[files.length-1];
        try {
            File loadFile = new File("save\\" + fileName + "\\step" + files.length + ".txt");
            System.out.println(files.length);
            System.out.println(loadFile.getPath());

            int[] position = new int[65];
            FileInputStream fis = new FileInputStream(loadFile);
            int b;
            int countNumber = 0;
            while ((b = fis.read()) != -1) {
                position[countNumber] = b;
                countNumber++;
            }
            //debug
            setPiecesAccordingToFile(position);
            //清空本地文件夹
            deleteLastSaveFile();
            //把存储文件夹拷贝到本地文件夹中
            File[] files1 = playerSaveFile.listFiles();

            for (File file : files1) {
                FileInputStream fis2 = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(new File(saveStepsFiles, file.getName()));
                byte[] bytes = new byte[1024];
                int len;
                while ((len = fis2.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                }
                fos.close();
                fis2.close();
            }

            //检测是否有人删改文件
            try{
                File [] files2 = saveStepsFiles.listFiles();
                for (int i = 1; i <= files2.length; i++) {
                    File checkFile = new File("save\\Jungle\\step" + i + ".txt");
                    //System.out.println("the length is "+checkFile.length());
                    if (checkFile.length() != 65) {
                        System.out.println(i +" times find exception");
                        JOptionPane.showMessageDialog(null, "检测到存档被修改\n已重新开始",
                                "FileChangedException", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return;
                    }
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "检测到存档被修改\n已重新开始",
                        "FileChangedException", JOptionPane.ERROR_MESSAGE);
                reset();
                return;
            }
        }


        catch (Exception e){
            JOptionPane.showMessageDialog(null, "检测不到存档",
                    "NotFoundException", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void playBack () throws IOException{
        File [] files = saveStepsFiles.listFiles();
        File loadFile = new File("save\\Jungle\\step"+playBackNumber+".txt");
        int b;
        int [] position = new int[65];
        int countNumber = 0;
        FileInputStream fis = new FileInputStream(loadFile);
        while ((b= fis.read())!= -1){
            position[countNumber] = b;
            countNumber++;
        }
        setPiecesAccordingToFile(position);
        playBackNumber++;
        if (playBackNumber == files.length+1){
            playBackNumber = 1;
        }
    }
    public void easyAImove (){
        //开关打开再执行
        if (this.easyAI) {
            ArrayList<ChessboardPoint> positionList  = searchChess();
            //随机抽一个可以走的棋子的坐标
            Random r = new Random();
            int index = r.nextInt(positionList.size());
            ChessboardPoint chosenPosition = positionList.get(index);
            //选择第一个可以走的坐标
            ArrayList <ChessboardPoint> chosenList = model.pointsCanMOveTo(chosenPosition);
            ChessboardPoint finalPosition = chosenList.get(0);
            //已知起点和终点，调用直白的AI走棋方法
            AIPlayMethod(chosenPosition,finalPosition);
            steps++;
            view.chessGameFrame.setCurrentStepsLabel2(steps);
        }
    }

    //汤易博 5/22 添加困难的AI算法
    public void hardAImove (){
        //其中判断语句无路可走时可能会报错
        if (this.hardAI) {
            ArrayList<ChessboardPoint> positionList = searchChess();
            //AI玩红方情况
            if (currentPlayer.equals(PlayerColor.RED)) {

                //先看看有没有一个能直接获胜
                for (int i = 0; i < positionList.size(); i++) {
                    ArrayList<ChessboardPoint> destinations = model.pointsCanMOveTo(positionList.get(i));
                    for (int j = 0; j < destinations.size(); j++) {
                        if (destinations.get(j).getRow() == 8 && destinations.get(j).getCol() == 3) {
                            AIPlayMethod(positionList.get(i), new ChessboardPoint(8, 3));
                            steps++;
                            view.chessGameFrame.setCurrentStepsLabel2(steps);
                            return;
                        }
                    }
                }
            } else {
                //先看看有没有一个能直接获胜
                for (int i = 0; i < positionList.size(); i++) {
                    ArrayList<ChessboardPoint> destinations = model.pointsCanMOveTo(positionList.get(i));
                    for (int j = 0; j < destinations.size(); j++) {
                        if (destinations.get(j).getRow() == 0 && destinations.get(j).getCol() == 3) {
                            AIPlayMethod(positionList.get(i), new ChessboardPoint(0, 3));
                            steps++;
                            view.chessGameFrame.setCurrentStepsLabel2(steps);
                            return;
                        }
                    }
                }
            }
            //没有直接获胜再来权衡吃与逃跑

            //吃子情况
            //这里采用完全镜像来排列
            ArrayList<ChessboardPoint> canEatDestinations = new ArrayList<>();
            ArrayList<ChessboardPoint> canEatPositions = new ArrayList<>();

            //先装好两个数组
            for (int i = 0; i < positionList.size(); i++) {
                ArrayList<ChessboardPoint> destinations = model.pointsCanMOveTo(positionList.get(i));
                for (int j = 0; j < destinations.size(); j++) {
                    //看看有没有可以吃的蓝方棋子
                    if (model.getChessPieceAt(destinations.get(j)) != null) {
                        canEatDestinations.add(destinations.get(j));
                        canEatPositions.add(positionList.get(i));
                    }
                }
            }


            //装完数组以后再来排序
            for (int i = 0; i < canEatDestinations.size(); i++) {
                for (int j = 0; j < canEatDestinations.size() - 1; j++) {
                    int rankFront = model.getChessPieceAt(canEatDestinations.get(j)).getRank();
                    int rankBack = model.getChessPieceAt(canEatDestinations.get(j + 1)).getRank();

                    ChessboardPoint frontDestination = canEatDestinations.get(j);
                    ChessboardPoint backDestination = canEatDestinations.get(j + 1);
                    ChessboardPoint frontPosition = canEatPositions.get(j);
                    ChessboardPoint backPosition = canEatPositions.get(j + 1);

                    //对两个数组同时进行换位操作
                    if (rankFront > rankBack) {
                        canEatDestinations.set(j + 1, frontDestination);
                        canEatDestinations.set(j, backDestination);
                        canEatPositions.set(j + 1, frontPosition);
                        canEatPositions.set(j, backPosition);
                    }

                }
            }
            //此时我们已经将能吃的rank最高的棋子放到了数组末尾


            //逃跑情况，找出最高的被吃的
            //为了调用方法这里需要暂时更改颜色
            swapColor();
            System.out.println(currentPlayer.getColor());

            ArrayList<ChessboardPoint> enemyPositionList = searchChess();
            //再将颜色改回来（好奇，会不会有人用这个时间差卡bug）
            swapColor();
            System.out.println(currentPlayer.getColor());
            //创造将被吃掉的红方棋子的数组
            ArrayList<ChessboardPoint> killedPositionList = new ArrayList<>();
            //填满这个数组
            for (int i = 0; i < enemyPositionList.size(); i++) {
                ArrayList<ChessboardPoint> destinations = model.pointsCanMOveTo(enemyPositionList.get(i));
                for (int j = 0; j < destinations.size(); j++) {
                    //看看有没有可以吃的红方棋子
                    if (model.getChessPieceAt(destinations.get(j)) != null) {
                        killedPositionList.add(destinations.get(j));
                    }
                }
            }
            //按照红方的rank对这个数组进行排序，rank高的向后排
            for (int i = 0; i < killedPositionList.size(); i++) {
                for (int j = 0; j < killedPositionList.size() - 1; j++) {
                    int frontRank = model.getChessPieceAt(killedPositionList.get(j)).getRank();
                    int backRank = model.getChessPieceAt(killedPositionList.get(j + 1)).getRank();

                    ChessboardPoint frontKilled = killedPositionList.get(j);
                    ChessboardPoint backKilled = killedPositionList.get(j + 1);

                    if (frontRank > backRank) {
                        killedPositionList.set(j + 1, frontKilled);
                        killedPositionList.set(j, backKilled);
                    }
                }
            }

            //比较看看是该逃跑还是该吃子

            //不能吃但需要跑
            if (canEatDestinations.size() == 0 && killedPositionList.size() != 0) {
                ArrayList<ChessboardPoint> canEscapeTo = model.pointsCanMOveTo(killedPositionList.get(killedPositionList.size() - 1));
                if (canEscapeTo != null) {
                    AIPlayMethod(killedPositionList.get(killedPositionList.size() - 1), canEscapeTo.get(0));
                    steps++;
                    view.chessGameFrame.setCurrentStepsLabel2(steps);
                }
                //跑不了了就走一个最短距离
                else {
                    ChessboardPoint firstMovePiece = positionList.get(0);
                    ArrayList<ChessboardPoint> canMOveTo = model.pointsCanMOveTo(firstMovePiece);
                    if (currentPlayer.equals(PlayerColor.RED)) {
                        //从小到大排列出下一步到对方巢穴的距离
                        for (int i = 0; i < canMOveTo.size(); i++) {
                            for (int j = 0; j < canMOveTo.size() - 1; j++) {
                                int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(8, 3));
                                int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(8, 3));

                                ChessboardPoint frontPoint = canMOveTo.get(j);
                                ChessboardPoint backPoint = canMOveTo.get(j + 1);

                                if (frontDistance > backDistance) {
                                    canMOveTo.set(j + 1, frontPoint);
                                    canMOveTo.set(j, backPoint);
                                }
                            }
                        }
                        //排完了以后按第一个最小的走
                        AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                        steps++;
                        view.chessGameFrame.setCurrentStepsLabel2(steps);

                    } else {
                        //从小到大排列出下一步到对方巢穴的距离
                        for (int i = 0; i < canMOveTo.size(); i++) {
                            for (int j = 0; j < canMOveTo.size() - 1; j++) {
                                int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(0, 3));
                                int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(0, 3));

                                ChessboardPoint frontPoint = canMOveTo.get(j);
                                ChessboardPoint backPoint = canMOveTo.get(j + 1);

                                if (frontDistance > backDistance) {
                                    canMOveTo.set(j + 1, frontPoint);
                                    canMOveTo.set(j, backPoint);
                                }
                            }
                        }
                        //排完了以后按第一个最小的走
                        AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                        steps++;
                        view.chessGameFrame.setCurrentStepsLabel2(steps);
                    }
                }
            }

            //不能跑但可以吃
            if (canEatDestinations.size() != 0 && killedPositionList.size() == 0) {
                AIPlayMethod(canEatPositions.get(canEatPositions.size() - 1), canEatDestinations.get(canEatDestinations.size() - 1));
                steps++;
                view.chessGameFrame.setCurrentStepsLabel2(steps);
            }


            //可以吃又需要跑
            if (canEatDestinations.size() != 0 && killedPositionList.size() != 0){

                int eatRank = model.getChessPieceAt(canEatDestinations.get(canEatDestinations.size() - 1)).getRank();
                int killedRank = model.getChessPieceAt(killedPositionList.get(killedPositionList.size() - 1)).getRank();
                //相同等级下优先吃子
                if (eatRank >= killedRank) {
                    AIPlayMethod(canEatPositions.get(canEatPositions.size() - 1), canEatDestinations.get(canEatDestinations.size() - 1));
                    steps++;
                    view.chessGameFrame.setCurrentStepsLabel2(steps);
                } else {
                    //如果可以跑再跑
                    ArrayList<ChessboardPoint> canEscapeTo = model.pointsCanMOveTo(killedPositionList.get(killedPositionList.size() - 1));
                    if (canEscapeTo != null) {
                        AIPlayMethod(killedPositionList.get(killedPositionList.size() - 1), canEscapeTo.get(0));
                        steps++;
                        view.chessGameFrame.setCurrentStepsLabel2(steps);
                        //走不了就按最短距离走
                    }
                    else {
                        ChessboardPoint firstMovePiece = positionList.get(0);
                        ArrayList<ChessboardPoint> canMOveTo = model.pointsCanMOveTo(firstMovePiece);
                        if (currentPlayer.equals(PlayerColor.RED)) {
                            //从小到大排列出下一步到对方巢穴的距离
                            for (int i = 0; i < canMOveTo.size(); i++) {
                                for (int j = 0; j < canMOveTo.size() - 1; j++) {
                                    int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(8, 3));
                                    int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(8, 3));

                                    ChessboardPoint frontPoint = canMOveTo.get(j);
                                    ChessboardPoint backPoint = canMOveTo.get(j + 1);

                                    if (frontDistance > backDistance) {
                                        canMOveTo.set(j + 1, frontPoint);
                                        canMOveTo.set(j, backPoint);
                                    }
                                }
                            }
                            //排完了以后按第一个最小的走
                            AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                            steps++;
                            view.chessGameFrame.setCurrentStepsLabel2(steps);

                        } else {
                            //从小到大排列出下一步到对方巢穴的距离
                            for (int i = 0; i < canMOveTo.size(); i++) {
                                for (int j = 0; j < canMOveTo.size() - 1; j++) {
                                    int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(0, 3));
                                    int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(0, 3));

                                    ChessboardPoint frontPoint = canMOveTo.get(j);
                                    ChessboardPoint backPoint = canMOveTo.get(j + 1);

                                    if (frontDistance > backDistance) {
                                        canMOveTo.set(j + 1, frontPoint);
                                        canMOveTo.set(j, backPoint);
                                    }
                                }
                            }
                            //排完了以后按第一个最小的走
                            AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                            steps++;
                            view.chessGameFrame.setCurrentStepsLabel2(steps);
                        }
                    }

                }
            }

            //接下来分红蓝，进行正常步骤,移动第一个扫到的棋子
            //不能吃也不需要跑
            if(canEatDestinations.size() == 0 && killedPositionList.size() == 0) {
                ChessboardPoint firstMovePiece = positionList.get(0);
                ArrayList<ChessboardPoint> canMOveTo = model.pointsCanMOveTo(firstMovePiece);
                if (currentPlayer.equals(PlayerColor.RED)) {
                    //从小到大排列出下一步到对方巢穴的距离
                    for (int i = 0; i < canMOveTo.size(); i++) {
                        for (int j = 0; j < canMOveTo.size() - 1; j++) {
                            int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(8, 3));
                            int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(8, 3));

                            ChessboardPoint frontPoint = canMOveTo.get(j);
                            ChessboardPoint backPoint = canMOveTo.get(j + 1);

                            if (frontDistance > backDistance) {
                                canMOveTo.set(j + 1, frontPoint);
                                canMOveTo.set(j, backPoint);
                            }
                        }
                    }
                    //排完了以后按第一个最小的走
                    AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                    steps++;
                    view.chessGameFrame.setCurrentStepsLabel2(steps);

                } else {
                    //从小到大排列出下一步到对方巢穴的距离
                    for (int i = 0; i < canMOveTo.size(); i++) {
                        for (int j = 0; j < canMOveTo.size() - 1; j++) {
                            int frontDistance = model.calculateDistance(canMOveTo.get(j), new ChessboardPoint(0, 3));
                            int backDistance = model.calculateDistance(canMOveTo.get(j + 1), new ChessboardPoint(0, 3));

                            ChessboardPoint frontPoint = canMOveTo.get(j);
                            ChessboardPoint backPoint = canMOveTo.get(j + 1);

                            if (frontDistance > backDistance) {
                                canMOveTo.set(j + 1, frontPoint);
                                canMOveTo.set(j, backPoint);
                            }
                        }
                    }
                    //排完了以后按第一个最小的走
                    AIPlayMethod(firstMovePiece, canMOveTo.get(0));
                    steps++;
                    view.chessGameFrame.setCurrentStepsLabel2(steps);
                }
            }
            //最后再把颜色换回来


        }
    }
    //汤易博 5/22 添加辅助AI算法的找棋子方法,这里会优先将靠近下端的棋子放在数组的靠前位置，根据当前颜色自动找对应的棋子
    public ArrayList<ChessboardPoint> searchChess (){
        //首先先建一个可以存储可动棋子坐标的数组
        ArrayList <ChessboardPoint> ableMovePiecesPosition = new ArrayList<>();
        //红方走了一步点AI的情况（此时AI在红方），先找最下端的棋子
        if (this.currentPlayer.equals(PlayerColor.RED)){
            for (int i = 8; i >= 0 ; i--) {
                for (int j = 0; j < 7; j++) {
                    //先看看是不是空的
                    if ( model.getGrid()[i][j].getPiece() != null){
                        //不是空的，再来判断这个棋是不是红方的
                        if (model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.RED)){
                            ChessboardPoint thisPosaition = new ChessboardPoint(i,j);
                            //debug
                            System.out.println(model.getChessPieceAt(thisPosaition).getName()+model.getChessPieceAt(thisPosaition).getOwner());
                            //再看看这个棋子能不能动
                            if (model.pointsCanMOveTo(thisPosaition) != null){
                                ableMovePiecesPosition.add(thisPosaition);
                            }
                        }
                    }
                }
            }
        }
        //蓝方走了一步后点AI的情况（此时AI在蓝方），先找最上端的棋子
        else{
            for (int i = 0; i < 9 ; i++) {
                for (int j = 0; j < 7; j++) {
                    //先看看是不是空的
                    if ( model.getGrid()[i][j].getPiece() != null){
                        //不是空的，再来判断这个棋是不是蓝方的
                        if (model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)){
                            ChessboardPoint thisPosaition = new ChessboardPoint(i,j);
                            //debug
                            System.out.println(model.getChessPieceAt(thisPosaition).getName()+model.getChessPieceAt(thisPosaition).getOwner());
                            //再看看这个棋子能不能动
                            if (model.pointsCanMOveTo(thisPosaition) != null){
                                ableMovePiecesPosition.add(thisPosaition);
                            }
                        }
                    }
                }
            }
        }
        return ableMovePiecesPosition;
    }

    //汤易博 5/22 添加直白的AI走棋方法
    public void AIPlayMethod (ChessboardPoint src , ChessboardPoint dest){
        //再直白的走规则棋盘,有障碍就直接删除（因为返回的是一定可走的格子）
        if (model.getChessPieceAt(dest) != null){
            model.getGridAt(dest).removePiece();
        }
        model.moveChessPiece(src,dest);
        //再直白的走图像棋盘，有障碍就直接删除
        if (view.getGridComponentAt(dest).getComponentCount() != 0){
            view.removeChessComponentAtGrid(dest);
        }
        view.setChessComponentAtGrid(dest,view.removeChessComponentAtGrid(src));
        view.repaint();
        swapColor();
    }

    //汤易博 5/22 添加按照从小到大顺序排的可以吃的棋子坐标的数组

    int drawNumber =0;

    public void draw () throws IOException {
        JOptionPane.showMessageDialog(null, "        握手言和\n      点击确定，再开一把",
                "draw", JOptionPane.INFORMATION_MESSAGE);
        drawNumber ++;
        reset();
        view.chessGameFrame.setdrawLabel2(drawNumber);
    }

    Run2 run2 = new Run2();
    Thread thread =new Thread(run2);

    boolean click = true;
    public void autoClick (){
        run2.setThread(thread);
        run2.setGo(click);
        System.out.println("成功启动");

        if (click ) {
            thread.start();
            JOptionPane.showMessageDialog(null, "      1秒后开始跟随鼠标点击",
                    "Click", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "      关闭跟随点击",
                    "Click", JOptionPane.INFORMATION_MESSAGE);
        }

        if (click){
            click =false;
        }
        else {
            click =true;
        }

    }

}
