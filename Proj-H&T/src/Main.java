import model.BackgroundMusic;
import model.SoundEffect;
import view.GameBeginFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameBeginFrame beginFrame;
            try {
                beginFrame = new GameBeginFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            beginFrame.setVisible(true);
            new BackgroundMusic().playMusic("Resource\\bgm.wav");
        });
    }
}


//////babbababbabaa
