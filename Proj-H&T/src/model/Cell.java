package model;

import java.io.Serializable;
/**
 * This class describe the slot for Chess in Chessboard
 * */
public class Cell implements Serializable {
    // the position for chess
    private ChessPiece piece;
    private LandRiver type;

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public void setLandRiver (LandRiver type){
        this.type = type;
    }

    public LandRiver getLandRiver (){
        return type;
    }

    public Cell (LandRiver type){
        this.type =type;
    }

    public void removePiece() {
        this.piece = null;
    }
}

//a = b;
//a = b.location;
//b = null;

//规则棋盘是正确运行的
//Components棋盘在吃子的时候会发生以下的事故：
//吃子的一方会占据被吃一方的Component棋盘上的Component，而且居然还能绑定
//被吃的一方虽然在规则棋盘上消失了，但是图片并没有消失。

