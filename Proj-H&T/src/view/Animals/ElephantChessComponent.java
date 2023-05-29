package view.Animals;


import model.PlayerColor;
import view.TotalChessComponent;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class ElephantChessComponent extends TotalChessComponent {

    //just try try
    JLabel elephantPicture;
    public JLabel elephantPhoto;
    //try
    public ElephantChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        this.size = size;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image picture = new ImageIcon("Resource/re.png").getImage();
        if(owner == PlayerColor.BLUE)
        {
            picture = new ImageIcon("Resource/be.png").getImage();
        }
        picture = picture.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(picture);
        elephantPhoto = new JLabel(icon);
        elephantPhoto.setSize(size, size);
        elephantPhoto.setLocation(0, 0);
        elephantPicture = elephantPhoto;
        add(elephantPicture);
        /*
        ImageIcon photo = new ImageIcon("Resource/re.png");
        if(owner == PlayerColor.BLUE)
        {
            photo = new ImageIcon("Resource/be.png");
        }
        Image image = photo.getImage();
        photo = new ImageIcon(image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        JLabel label = new JLabel((photo));
        add(label);
        */
    }
}