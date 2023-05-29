package view;


import controller.GameController;
import model.*;
import view.Animals.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class represents the checkerboard component object on the panel
 */
public class ChessboardComponent extends JPanel {
    public CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];

    public CellComponent[][] getGridComponents() {
        return gridComponents;
    }

    public final int CHESS_SIZE;
    public boolean isDay = true;
    JLabel waterPicture;
    public JLabel turnLabel;
    public JLabel timeLabel;
    public Set<ChessboardPoint> riverCell = new HashSet<>();
    public Set<ChessboardPoint> trapCell = new HashSet<>();
    public Set<ChessboardPoint> denCell = new HashSet<>();

    public GameController gameController;

    public ChessGameFrame chessGameFrame;
    public void turnNight()
    {
        isDay = false;
    }
    public void turnDay()
    {
        isDay = true;
    }

    public ChessboardComponent(int chessSize,  JLabel timeLabel, ChessGameFrame chessGameFrame) {
        this.timeLabel = timeLabel;
        this.turnLabel = turnLabel;
        CHESS_SIZE = chessSize;
        int width = CHESS_SIZE * 7;
        int height = CHESS_SIZE * 9;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);// Allow mouse events to occur
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        System.out.printf("chessboard width, height = [%d : %d], chess size = %d\n", width, height, CHESS_SIZE);
        this.chessGameFrame = chessGameFrame;
        initiateGridComponents();
    }


    /**
     * This method represents how to initiate ChessComponent
     * according to Chessboard information
     */
    public void initiateChessComponent(Chessboard chessboard) {
        Cell[][] grid = chessboard.getGrid();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                // TODO: Implement the initialization checkerboard
                gridComponents[i][j].removeAll();//这会导致水波的图片加载不出来，但是让水波的图片加载出来会导致很多bug需要处理
                if (grid[i][j].getPiece() != null) {
                    ChessPiece chessPiece = grid[i][j].getPiece();
                    if(chessPiece.getName().equals("虎"))
                    {
                        gridComponents[i][j].add(new TigerChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("象"))
                    {
                        gridComponents[i][j].add(new ElephantChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("猫"))
                    {
                        gridComponents[i][j].add(new CatChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("鼠"))
                    {
                        gridComponents[i][j].add(new MouseChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("狼"))
                    {
                        gridComponents[i][j].add(new WolfChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("豹"))
                    {
                        gridComponents[i][j].add(new LeopardChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("狮"))
                    {
                        gridComponents[i][j].add(new LionChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                    else if(chessPiece.getName().equals("狗"))
                    {
                        gridComponents[i][j].add(new DogChessComponent(chessPiece.getOwner(), CHESS_SIZE));
                    }
                }
            }
        }
    }

    public void initiateGridComponents() {
        riverCell.add(new ChessboardPoint(3,1));
        riverCell.add(new ChessboardPoint(3,2));
        riverCell.add(new ChessboardPoint(4,1));
        riverCell.add(new ChessboardPoint(4,2));
        riverCell.add(new ChessboardPoint(5,1));
        riverCell.add(new ChessboardPoint(5,2));

        riverCell.add(new ChessboardPoint(3,4));
        riverCell.add(new ChessboardPoint(3,5));
        riverCell.add(new ChessboardPoint(4,4));
        riverCell.add(new ChessboardPoint(4,5));
        riverCell.add(new ChessboardPoint(5,4));
        riverCell.add(new ChessboardPoint(5,5));

        trapCell.add(new ChessboardPoint(0,2));
        trapCell.add(new ChessboardPoint(0,4));
        trapCell.add(new ChessboardPoint(1,3));

        trapCell.add(new ChessboardPoint(8,2));
        trapCell.add(new ChessboardPoint(8,4));
        trapCell.add(new ChessboardPoint(7,3));

        denCell.add(new ChessboardPoint(0,3));
        denCell.add(new ChessboardPoint(8,3));

        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint temp = new ChessboardPoint(i, j);
                CellComponent cellComponent;
                repaint();
                if(riverCell.contains(temp))
                {
                    if(isDay)
                    {
                        cellComponent = new CellComponent(1, calculatePoint(i, j), CHESS_SIZE);
                        //System.out.println("water day");//////debug
                    }
                    else
                    {
                        cellComponent = new CellComponent(2, calculatePoint(i, j), CHESS_SIZE);
                        //System.out.println("water night");//////debug
                    }
                    this.add(cellComponent);
                }
                else if(trapCell.contains(temp))
                {
                    cellComponent = new CellComponent(3, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cellComponent);
                }
                else if(denCell.contains(temp))
                {
                    cellComponent = new CellComponent(4, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cellComponent);
                }
                else
                {
                    if(isDay)
                    {
                        cellComponent = new CellComponent(5, calculatePoint(i, j), CHESS_SIZE);
                        //System.out.println("grass day");//////debug
                    }
                    else
                    {
                        cellComponent = new CellComponent(6, calculatePoint(i, j), CHESS_SIZE);
                        //System.out.println("grass night");//////debug
                    }
                    this.add(cellComponent);
                }
                gridComponents[i][j] = cellComponent;
                //repaint();
            }
        }
    }
    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setChessComponentAtGrid(ChessboardPoint point, TotalChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }

    public TotalChessComponent removeChessComponentAtGrid(ChessboardPoint point) {
        // Note re-validation is required after remove / removeAll.
        if(getGridComponentAt(point).getComponentCount() == 0) {
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            //getGridComponentAt(point).repaint();
            return null;
        }
        TotalChessComponent chess = (TotalChessComponent) getGridComponentAt(point).getComponents()[0];
        getGridComponentAt(point).removeAll();
        getGridComponentAt(point).revalidate();
        //getGridComponentAt(point).repaint();
        chess.setSelected(false);///////////////////after move, the highlight disappear
        return chess;
    }

    public CellComponent getGridComponentAt(ChessboardPoint point) {
        return gridComponents[point.getRow()][point.getCol()];
    }
    //把鼠标点击的像素点转换为当前的棋格的坐标点
    private ChessboardPoint getChessboardPoint(Point point) {
        System.out.println("[" + point.y/CHESS_SIZE +  ", " +point.x/CHESS_SIZE + "] Clicked");
        return new ChessboardPoint(point.y/CHESS_SIZE, point.x/CHESS_SIZE);
    }
    public Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    protected void processMouseEvent(MouseEvent e)
    {
        if (e.getID() == MouseEvent.MOUSE_PRESSED)
        {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            ChessboardPoint c = getChessboardPoint(e.getPoint());
            if (gameController.model.getChessPieceAt(c) == null)//格子上没有棋子
            {
                System.out.print("None chess here and ");
                try {
                    gameController.onPlayerClickCell(getChessboardPoint(e.getPoint()), (CellComponent) clickedComponent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else
            {
                new SoundEffect().playEffect("Resource\\click.wav");
                System.out.print("One chess here and it is "+gameController.model.getChessPieceAt(c).getName());
                try {
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()),
                            (TotalChessComponent) clickedComponent.getComponents()[0]);

/*
                    for(int i = 0 ; i < gameController.model.pointsCanMOveTo(getChessboardPoint(e.getPoint())).size() ; i++)
                    {
                        gameController.onPlayerClickChessPiece(gameController.model.pointsCanMOveTo(getChessboardPoint(e.getPoint())).get(i),
                                (TotalChessComponent) clickedComponent.getComponents()[0]);
                    }
*/


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
