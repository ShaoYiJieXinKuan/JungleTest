package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private Color background;
    public int number;
    int size;
    public static final Color grass = new Color(96, 194, 73);
    public static final Color grass1 = new Color(0, 100, 0);
    public static final Color water = new Color(161, 227, 240);
    public static final Color water1 = new Color(0, 227, 240);

    public CellComponent(int number, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.number = number;
        this.size = size;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        if(number == 5)
        {
            background = grass;
        }
        else if(number == 6)
        {
            background = grass1;
        }
        else if(number == 3)
        {
            background = Color.GRAY;
        }
        else if(number == 4)
        {
            background = Color.orange;
        }
        else if(number == 1)
        {
            background = water;
        }
        else if(number == 2)
        {
            background = water1;
        }
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);

/*

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 253, 87, 150));
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
        g2d.fill(roundedRectangle);
*/
    }

    //the "CellView" class in dalao project can teach us how to highlights the chess
}
