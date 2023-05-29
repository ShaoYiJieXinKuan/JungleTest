package view;

import controller.GameController;
import model.Chessboard;
import model.Timer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class GameBeginFrame extends JFrame
{
    ChessGameFrame chessGameFrame;
    private final int WIDTH;
    private final int HEIGHT;
    JLabel beginBack;
    public final JLabel photo;
    public GameBeginFrame() throws IOException {
        setTitle("2023 CS109 Project");
        this.WIDTH = 360;
        this.HEIGHT = 480;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        ChessGameFrame chessGameFrame = new ChessGameFrame(1100, 810);
        GameController gameController = new GameController(chessGameFrame.getChessboardComponent(), new Chessboard());
        this.chessGameFrame = chessGameFrame;
        chessGameFrame.gameBeginFrame = this;

        addBeginButton();
        addGameRuleButton();

        Image picture = new ImageIcon("Resource/begin.jpg").getImage();
        picture = picture.getScaledInstance(360, 480, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(picture);
        photo = new JLabel(icon);
        photo.setSize(360, 480);
        photo.setLocation(0, 0);
        beginBack = photo;
        add(beginBack);
    }
    private void addBeginButton()
    {
        JButton button = new JButton("START");
        button.addActionListener((e) ->
        {
            this.setVisible(false);
            Timer.time = 11;
            if (GameController.timer == null)
            {
                GameController.timer = new Timer(chessGameFrame.getChessboardComponent().gameController);
                GameController.timer.start();
            }
            //chessGameFrame.turnLabel.setLocation(250, 40);
            chessGameFrame.repaint();
            //chessGameFrame.timeLabel.setVisible(true);
            chessGameFrame.setVisible(true);

        });
        button.setLocation(100, 260);
        button.setSize(160, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addGameRuleButton()
    {
        JButton button = new JButton("Game Rule");
        button.addActionListener((e) ->
        {
            JOptionPane.showMessageDialog(null, "\uF06CIntroduction \n" +
                    "Jungle or Dou Shou Qi (斗兽棋), is a modern Chinese board game with an obscure history.The game is played on a \n" +
                    "7×9 board and is a two-player strategy game.\n" +
                    "\uF06CPieces\n" +
                    "Each player owns 8 animal pieces representing different animals of various ranks, with:\n" +
                    "Elephant = 8, Lion = 7, Tiger = 6, Leopard = 5, Wolf = 4, Dog = 3, Cat = 2, and Rat = 1.\n" +
                    "Higher ranked animals can capture the animals of lower or equal rank. But there is a special case that Elephant\n" +
                    "cannot capture the Rat while the Rat can capture the Elephant. Each player moves alternatively, and all pieces\n" +
                    "can move one square horizontally or vertically, but not diagonally. There are some special movements for lion, \n" +
                    "tiger and rat. These will be explained in detail:\n" +
                    "\uF0A1 The Rat is the only animal that may go onto a water square. \n" +
                    "\uF0A1 After entering the river, the Rat cannot be captured by any piece on land. \n" +
                    "\uF0A1 Also, the Rat in river cannot capture the Elephant on the land.\n"+
                    "\uF0A1 The lion and tiger can jump over a river vertically or horizontally. They jump from a square on one edge\n" +
                    "of the river to the next non-water square on the other side. \n" +
                    "\uF0A1 If that square contains an enemy piece of equal or lower rank, the lion or tiger capture it as their jump.\n" +
                    "\uF0A1 However, a jumping move is blocked (not permitted) if a rat of either color currently occupies any of the\n" +
                    "intervening water squares.\n" +
                    "\uF06CChessboard \n" +
                    "Jungle game has an interesting chessboard with different terrains including dens, traps and rivers.After the\n" +
                    "initialization, the pieces start on squares with pictures corresponding to their animals, which are invariably\n" +
                    "shown on the Jungle board. The three kinds of special terrains are explained as:\n" +
                    "\uF0A1 Dens(兽穴): It is not allowed that the piece enters its own den. If the player's pieces enter the dens of\n" +
                    "his/her opponent, then the player wins.\n" +
                    "\uF0A1 Trap(陷阱): If a piece entering the opponent's trap, then its rank is reduced into 0 temporarily before\n" +
                    "exiting. The trapped piece could be captured by any pieces of the defensing side.\n" +
                    "\uF0A1 River(河流): They are located in the center of the chessboard, each comprising 6 squares in a 2×3 rectangle.\n" +
                    "Only rats could enter the river, and lions and tigers could jump across the river.\n" +
                    "\uF06CRules\n" +
                    "1. Game Initialization: At the beginning, all 16 pieces are put into the chessboard as the initial state.\n" +
                    "2. Game Progress: The player with blue moves first. Two players take the turn alternatively until the game is \n" +
                    "finished. When a player takes turn, he/she can select one of his pieces and do one of the following:\n" +
                    "\uF0A1 Moving one square horizontally or vertically. For Lion, Tiger and Rat, they also have special movements\n" +
                    "related to the river squares, which have been introduced previously.\n" +
                    "\uF0A1 Capturing an opposing piece. In all captures, the captured pieces is removed from the board and the square\n" +
                    "is occupied by the capturing piece. A piece can capture any enemy piece following the rules introduced in \"Pieces\".\n" +
                    "3. Game Termination: A player loses the game if one of the following happens:\n" +
                    "\uF0A1 The den is entered by his/her opponents.\n" +
                    "\uF0A1 All of his/her pieces are captured and he/her is unable to do any movement.\n" +
                    "\uF06C Enjoy your game! \uF06C", "game rule", JOptionPane.INFORMATION_MESSAGE);
        });
        button.setLocation(100, 340);
        button.setSize(160, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
}
