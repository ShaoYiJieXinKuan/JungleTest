package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;

    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }

    public boolean canCapture(ChessPiece target) {
        // TODO: Finish this method!
        //这里应该就是不考虑异常情况的捕食方法
        //先看看自己是不是鼠鼠1
        //是鼠鼠1
        if (this.name.equals("鼠")){
            //看对方是不是大象2
            //是大象2
            if (target.getName().equals("象")){
                return true;
            }
            //不是大象2
            else {
                return false;
            }
        }
        //不是鼠鼠1
        else {
            //看看自己是不是大象2
            //自己是大象2
            if (this.name.equals("象")) {
                //看看对面是不是鼠鼠3
                //对面是鼠鼠3
                if (target.getName().equals("鼠")) {
                    return false;
                }
                //对面不是鼠鼠3
                else {
                    return true;
                }
            }
            //自己不是大象2
            else {
                //开始通过正常比较等级来捕食3
                //自己等级更好3
                if (this.rank >= target.rank) {
                    return true;
                }
                //自己等级不行
                else {
                    return false;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }

    public int getRank() {
        return rank;
    }
}
