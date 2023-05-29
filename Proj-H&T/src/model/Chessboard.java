package model;

import java.util.ArrayList;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;
    public int turn = 0;

    //总构造方法
    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//9x7

        initGrid();
        initPieces();
    }

    //辅助构造方法一，对二位类数组的每一棋格进行空参构造,并先全部赋值为陆地，再把河流赋值为河流
    public void initGrid() {
        //全部格子都赋值为陆地
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell(LandRiver.LAND);
            }
        }
        //再开始赋值河流
        for (int i = 3; i < 6; i++) {
            for (int j = 1; j < 3; j++) {
                grid[i][j].setLandRiver(LandRiver.RIVER);
            }
            for (int j = 4; j < 6; j++) {
                grid[i][j].setLandRiver(LandRiver.RIVER);
            }
        }
    }
    //辅助构造方法二：在每一个该摆棋子的格子上摆好棋子
    public void initPieces() {
        //先设置红方的棋子
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "象",8));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "狮",7));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "虎",6));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "豹",5));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "狼",4));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "狗",3));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "猫",2));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "鼠",1));

        //再设置蓝方的棋子
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "象",8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "狮",7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "虎",6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "豹",5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "狼",4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "狗",3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "猫",2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "鼠",1));
    }
    //输入一个坐标，返回对应的棋子（如果有）
    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }
    //是找棋子方法的辅助，找棋格
    public Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }
    //输入两个坐标，返回两个坐标间的距离,辅助真正的移动方法和捕食方法
    public int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }
    //输入坐标，找到对应的棋子，选中并返回该棋子，再将那个棋格清空，辅助真正的移动方法
    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }
    //找到坐标对应的棋格，把要放的棋子放到那个棋格里去，辅助真正的移动方法
    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }
    //真正的移动方法
    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {

        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        else {
            new SoundEffect().playEffect("Resource\\chicken.wav");
            setChessPiece(dest, removeChessPiece(src));
            //turn++;/////////////////////////////////////////////////////////////////////////
            System.out.println("成功移动");
        }
    }
    //真正的捕食方法
    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (isValidCapture(src, dest)) {
            //TODO: Fix this!!!!!!!!!
            grid[dest.getRow()][dest.getCol()].removePiece();//

            //hgy
            new SoundEffect().playEffect("Resource\\capture.wav");
            //hgy
            //turn++;//////////////////////////////////////////////////////////////////////////
            System.out.println("成功捕食");
            System.out.println("Grid is as follows:");
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j <grid[0].length; j++) {
                    if(grid[i][j].getPiece() == null){
                        System.out.print("null"+" ");
                        continue;
                    }
                    System.out.print(grid[i][j].getPiece().getName()+" ");
                }
                System.out.println();
            }

        }
        else {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        // TODO: Finish the method.
    }
    //获取当前整个棋盘
    public Cell[][] getGrid() {
        return grid;
    }
    //debug方法
    public void outputCell(){
        System.out.println("Grid is as follows:");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j <grid[0].length; j++) {
                if(grid[i][j].getPiece() == null){
                    System.out.print("null"+" ");
                    continue;
                }
                System.out.print(grid[i][j].getPiece().getName()+" ");
            }
            System.out.println();
        }
    }
    //返回某一个坐标上棋子的主人
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).
                getPiece().
                getOwner();
    }
    //判断能不能走，辅助真正移动方法（可见是先看看能不能捕食，捕食完了再看能不能走）
    //5-22 汤易博
    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        //先把这个棋子拿出来
        ChessPiece walker = getChessPieceAt(src);
        //先看看是否选中棋子且目标为空0
        //不满足基本条件0
        //汤易博 5/22 增加了两个判断条件
        if (walker.getOwner().equals(PlayerColor.BLUE) && dest.getCol() == 3 && dest.getRow()== 8){
            return false;
        }
        if (walker.getOwner().equals(PlayerColor.RED) && dest.getCol() == 3 && dest.getRow()== 0){
            return false;
        }
        if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null) {
            return false;
        }
        //满足基本条件0
        else {
            //检测是否为单格移动1
            //若是单格移动1
            if (calculateDistance(src,dest) ==1){
                //检测下一步是不是要走到河里2
                //下一步要走到河里2
                if (isIntheRiver(dest)){
                    //检测移动者是不是鼠鼠3
                    //是鼠鼠3
                    if (walker.getName().equals("鼠")){
                        return true;
                    }
                    //不是鼠鼠3
                    else {
                        return false;
                    }
                }
                //下一步不要走到河里
                else {
                    return true;
                }
            }
            //若不是单格移动1
            else {
                //看看是不是狮子或老虎2
                //是狮子或者老虎2
                if (walker.getName().equals("狮") || walker.getName().equals("虎")){
                    //debug
                    System.out.println("是狮虎");
                    //看看是否满足狮虎跳河的条件3
                    //满足狮虎跳河3
                    if (isAbleJumpRiver(src,dest)){
                        //debug
                        System.out.println("满足狮虎跳河");
                        return true;
                    }
                    //不满足狮虎跳河3
                    else {
                        System.out.println("不满足狮虎跳河");
                        return false;
                    }
                }
                //不是狮子或者老虎2
                else{
                    //debug
                    System.out.println("不是狮子或者老虎");
                    return false;
                }
            }
        }
    }

    //辅助真正的捕食方法
    //5-22 汤易博
    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        // TODO:Fix this method
        //先把捕食者和猎物找出来
        ChessPiece Hunter = getChessPieceAt(src);
        ChessPiece Pray = getChessPieceAt(dest);
        // 5/22 汤易博 增加判断条件
        if (Hunter == null || Pray == null){
            return false;
        }


        //先看是不是一家的0
        //如果是一家的0
        if (Hunter.getOwner().equals(Pray.getOwner())) {
            return false;
        }
        //如果不是一家的0
        else{
            //看距离1
            //距离合适1
            if (calculateDistance(src, dest) == 1) {
                //看看特定位置2
                //捕食者在对方陷阱中2
                if (isInTrap(src, Hunter)) {
                    return false;
                }
                //猎物在对方陷阱中2
                else if (isInTrap(dest,Pray)){
                    return true;
                }
                //判断捕食者在不在河里
                //捕食者在河里2(那他必然是鼠鼠)
                else if (isIntheRiver(src)){
                    //判断猎物是否在河里3
                    //猎物也在河里3
                    if (isIntheRiver(dest)){
                        return true;
                    }
                    //猎物不在河里3
                    else {
                        return false;
                    }
                }
                //捕食者不在河里2
                else {
                    //猎物是否在河里3
                    //猎物在河里3
                    if(isIntheRiver(dest)){
                        return false;
                    }
                    //猎物不在河里3
                    else {
                        //开始正常的rank判断4
                        //rank更好
                        if (Hunter.canCapture(Pray)){
                            return true;
                        }
                        //rank更差
                        else {
                            return false;
                        }
                    }
                }
            }
            //距离不合适1
            else {
                //看看捕食者是不是狮子或者老虎2
                //是狮子或者老虎2
                if (Hunter.getName().equals("狮") || Hunter.getName().equals("虎")){
                    //看看捕食者和猎物的相对位置满不满足跳河捕食3
                    //能满足狮虎跳河捕食3
                    if(isAbleJumpRiver(src,dest)){
                        //最后比较rank4
                        //rank对了4
                        if (Hunter.canCapture(Pray)){
                            return true;
                        }
                        //rank不行4
                        else {
                            return false;
                        }
                    }
                    //不能满足狮虎跳河捕食
                    else {
                        return false;
                    }
                }
                //不是狮子或者老虎2
                else {
                    return false;
                }
            }
        }
    }
    //看看ta是否在对方陷阱里,辅助真正的判断能否捕食
    public boolean isInTrap (ChessboardPoint point,ChessPiece ta){
        if (ta.getOwner().equals(PlayerColor.BLUE)) {
            if (point.getCol() == 2 && point.getRow() == 0) {
                return true;
            }
            if (point.getCol() == 3 && point.getRow() == 1) {
                return true;
            }
            if (point.getCol() == 4 && point.getRow() == 0) {
                return true;
            }
        }
        if (ta.getOwner().equals(PlayerColor.RED)) {
            if (point.getCol() == 2 && point.getRow() == 8) {
                return true;
            }
            if (point.getCol() == 3 && point.getRow() == 7) {
                return true;
            }
            if (point.getCol() == 4 && point.getRow() == 8) {
                return true;
            }
        }
        return false;
    }
    //看看满不满足狮子与老虎的跳河捕食的位置条件，辅助真正的判断能否捕食
    public boolean isAbleJumpRiver (ChessboardPoint src, ChessboardPoint dest){
        //捕食者在河上面一排
        ChessPiece Hunter = getChessPieceAt(src);
        ChessPiece Pray = getChessPieceAt(dest);
        //汤易博 5/23
        if(isIntheRiver(dest)){
            return false;
        }
        //检查1行列2比大小3是否只有河4是否有东西5是否有对方鼠
        if (src.getRow() == dest.getRow()){
            //Debug
            System.out.println("他们的行相等");

            if (src.getCol() > dest.getCol()){

                for (int i = dest.getCol()+1; i < src.getCol(); i++) {
                    if (grid[src.getRow()][i].getLandRiver().equals(LandRiver.LAND)){
                        return false;
                    }
                }

                for (int i = dest.getCol()+1; i < src.getCol(); i++) {
                    if (grid[src.getRow()][i].getPiece() != null){
                        return false;
                    }
                }

            }
            else {
                for (int i = src.getCol()+1; i < dest.getCol(); i++) {
                    if (grid[src.getRow()][i].getLandRiver().equals(LandRiver.LAND)){
                        return false;
                    }
                }

                for (int i = src.getCol()+1; i < dest.getCol(); i++) {
                    if (grid[src.getRow()][i].getPiece() != null){
                        return false;
                    }
                }
            }
            return true;
        }

        if (src.getCol() == dest.getCol()){
            //debug
            System.out.println("他们的列相等");
            if (src.getRow() > dest.getRow()){
                //debug
                System.out.println("捕食者的行更大");

                for (int i = dest.getRow()+1; i < src.getRow(); i++) {
                    //debug
                    if (grid[i][src.getCol()].getLandRiver() == null){
                        System.out.println("这个格子的类型是null");
                    }
                    if (grid[i][src.getCol()].getLandRiver().equals(LandRiver.LAND)){
                        //debug
                        System.out.println("他们中间有陆地");
                        return false;
                    }
                }

                for (int i = dest.getRow()+1; i < src.getRow(); i++) {
                    if (grid[i][src.getCol()].getPiece() != null){
                        //debug
                        System.out.println("有脏东西");
                        return false;
                    }
                }
            }
            else {
                for (int i = src.getRow()+1; i < dest.getRow(); i++) {
                    if (grid[i][src.getCol()].getLandRiver().equals(LandRiver.LAND)){
                        return false;
                    }
                }

                for (int i = src.getRow()+1; i < dest.getRow(); i++) {
                    if (grid[i][src.getCol()].getPiece() != null){
                        return false;
                    }
                }
            }
            return true;
        }

        else {
            return false;
        }
    }

    //判断该对象是否在河里
    public boolean isIntheRiver (ChessboardPoint src){
        if (src.getRow()>= 3 && src.getRow() <= 5){
            if ((src.getCol()>=1 && src.getCol() <= 2) || (src.getCol()>=4 && src.getCol() <= 5)){
                return true;
            }
        }
        return false;
    }
    //返回可以移动的坐标

    //汤易博 5/22
    public ArrayList<ChessboardPoint> pointsCanMOveTo(ChessboardPoint src){
        ChessPiece walker = getChessPieceAt(src);
        ArrayList<ChessboardPoint>  pointsCanMOveTo = new ArrayList<>();

        //先判断是不是狮子或者老虎1
        //是狮子或者老虎1
        if (walker.getName().equals("狮") || walker.getName().equals("虎")){
            //先检查上方2
            if (src.getRow()> 0){
                ChessboardPoint dest = new ChessboardPoint(src.getRow()-1,src.getCol());
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看是不是河4
                    //是河4
                    if (getGridAt(dest).getLandRiver().equals(LandRiver.RIVER)){
                        //看看能不能跨河5
                        ChessboardPoint acrossRiverdDest = new ChessboardPoint(src.getRow()-4,src.getCol());
                        //能跨河5
                        if (isValidMove(src,acrossRiverdDest)){
                            pointsCanMOveTo.add(acrossRiverdDest);
                        }
                        //不能跨河5
                        else {
                            //看看能不能捕猎6
                            //能捕猎6
                            if (isValidCapture(src,acrossRiverdDest)){
                                pointsCanMOveTo.add(acrossRiverdDest);
                            }
                        }
                    }
                    //不是河4
                    else{
                        //看看能不能捕猎5
                        if (isValidCapture(src,dest)){
                            pointsCanMOveTo.add(dest);
                        }
                    }
                }
            }
            //检查右方2
            if (src.getCol() < 6){
                ChessboardPoint dest = new ChessboardPoint(src.getRow(),src.getCol()+1);
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看是不是河4
                    //是河4
                    if (getGridAt(dest).getLandRiver().equals(LandRiver.RIVER)){
                        //看看能不能跨河5
                        ChessboardPoint acrossRiverdDest = new ChessboardPoint(src.getRow(),src.getCol()+3);
                        //能跨河5
                        if (isValidMove(src,acrossRiverdDest)){
                            pointsCanMOveTo.add(acrossRiverdDest);
                        }
                        //不能跨河5
                        else {
                            //看看能不能捕猎6
                            //能捕猎6
                            if (isValidCapture(src,acrossRiverdDest)){
                                pointsCanMOveTo.add(acrossRiverdDest);
                            }
                        }
                    }
                    //不是河4
                    else{
                        //看看能不能捕猎5
                        if (isValidCapture(src,dest)){
                            pointsCanMOveTo.add(dest);
                        }
                    }
                }
            }
            //检查下方2
            if (src.getRow() < 8){
                ChessboardPoint dest = new ChessboardPoint(src.getRow()+1,src.getCol());
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看是不是河4
                    //是河4
                    if (getGridAt(dest).getLandRiver().equals(LandRiver.RIVER)){
                        //看看能不能跨河5
                        ChessboardPoint acrossRiverdDest = new ChessboardPoint(src.getRow()+4,src.getCol());
                        //能跨河5
                        if (isValidMove(src,acrossRiverdDest)){
                            pointsCanMOveTo.add(acrossRiverdDest);
                        }
                        //不能跨河5
                        else {
                            //看看能不能捕猎6
                            //能捕猎6
                            if (isValidCapture(src,acrossRiverdDest)){
                                pointsCanMOveTo.add(acrossRiverdDest);
                            }
                        }
                    }
                    //不是河4
                    else{
                        //看看能不能捕猎5
                        if (isValidCapture(src,dest)){
                            pointsCanMOveTo.add(dest);
                        }
                    }
                }
            }
            //检查左方2
            if (src.getCol()  >0){
                ChessboardPoint dest = new ChessboardPoint(src.getRow(),src.getCol()-1);
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看是不是河4
                    //是河4
                    if (getGridAt(dest).getLandRiver().equals(LandRiver.RIVER)){
                        //看看能不能跨河5
                        ChessboardPoint acrossRiverdDest = new ChessboardPoint(src.getRow(),src.getCol()-3);
                        //能跨河5
                        if (isValidMove(src,acrossRiverdDest)){
                            pointsCanMOveTo.add(acrossRiverdDest);
                        }
                        //不能跨河5
                        else {
                            //看看能不能捕猎6
                            //能捕猎6
                            if (isValidCapture(src,acrossRiverdDest)){
                                pointsCanMOveTo.add(acrossRiverdDest);
                            }
                        }
                    }
                    //不是河4
                    else{
                        //看看能不能捕猎5
                        if (isValidCapture(src,dest)){
                            pointsCanMOveTo.add(dest);
                        }
                    }
                }
            }
        }
        //不是狮子或者老虎1
        else{
            //判断上方2
            if (src.getRow()> 0){
                ChessboardPoint dest = new ChessboardPoint(src.getRow()-1,src.getCol());
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看能不能捕食4
                    //能捕食4
                    //debug
                    //System.out.println(getChessPieceAt(src).getName() + " " +getChessPieceAt(src).getOwner());
                    if (isValidCapture(src,dest)){
                        pointsCanMOveTo.add(dest);
                    }
                }
            }
            //判断下方2
            if (src.getRow() < 8){
                ChessboardPoint dest = new ChessboardPoint(src.getRow()+1,src.getCol());
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看能不能捕食4
                    //能捕食4
                    if (isValidCapture(src,dest)){
                        pointsCanMOveTo.add(dest);
                    }
                }
            }
            //判断左方2
            if (src.getCol() > 0){
                ChessboardPoint dest = new ChessboardPoint(src.getRow(),src.getCol()-1);
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看能不能捕食4
                    //能捕食4
                    if (isValidCapture(src,dest)){
                        pointsCanMOveTo.add(dest);
                    }
                }
            }
            //判断右方2
            if (src.getCol() < 6){
                ChessboardPoint dest = new ChessboardPoint(src.getRow(),src.getCol()+1);
                //判断能不能走3
                //能走3
                if (isValidMove(src,dest)){
                    pointsCanMOveTo.add(dest);
                }
                //不能走3
                else {
                    //看看能不能捕食4
                    //能捕食4
                    if (isValidCapture(src,dest)){
                        pointsCanMOveTo.add(dest);
                    }
                }
            }
        }
        // 5/ 22 汤易博
        if(pointsCanMOveTo.size()== 0){
            return null;
        }
        else {
            return pointsCanMOveTo;
        }
        //5/22 汤易博
    }

    //检测是否胜利
    //0 是都没赢 1是蓝方赢 2是红方赢


    //汤易博 5/22 修改chessboard 中所有方法改为public
}
