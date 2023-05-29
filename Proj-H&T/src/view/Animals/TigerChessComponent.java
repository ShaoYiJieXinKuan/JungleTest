package view.Animals;


import model.PlayerColor;
import view.TotalChessComponent;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class TigerChessComponent extends TotalChessComponent {
    //just try try
    JLabel tigerPicture;
    public JLabel tigerPhoto;
    //try
    public TigerChessComponent(PlayerColor owner, int size) {
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

        Image picture = new ImageIcon("Resource/rt.png").getImage();
        if(owner == PlayerColor.BLUE)
        {
            picture = new ImageIcon("Resource/bt.png").getImage();
        }
        picture = picture.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(picture);
        tigerPhoto = new JLabel(icon);
        tigerPhoto.setSize(size, size);
        tigerPhoto.setLocation(0, 0);
        tigerPicture = tigerPhoto;
        add(tigerPicture);


        /*
        ImageIcon photo = new ImageIcon("Resource/tiger1.jpg");
        if(owner == PlayerColor.BLUE)
        {
            photo = new ImageIcon("Resource/tiger2.jpg");
        }
        Image image = photo.getImage();
        photo = new ImageIcon(image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(photo);
        add(label);
        */
    }
}
