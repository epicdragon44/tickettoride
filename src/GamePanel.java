import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements MouseListener {
    private GameEngine g;
    public GamePanel() {
        setLayout(null);
        setPreferredSize(new Dimension(1900, 1000));
        setVisible(true);
    }
    
    public void initGame() {
    	
    }
    
    public void lastRound() {
    	
    }

    public void play() {

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBackground(g);
    }
    public void drawConnection(Node n1, Node n2) {

    }
    public void drawBoard(Graphics g) {

    }
    public void drawTracks(Graphics g) {

    }
    public void drawDecks(Graphics g) {

    }
    public void drawCities(Graphics g) {

    }
    public void drawNumbers(Graphics g) {

    }
    public void drawBackground(Graphics g) {
        try {
            BufferedImage backgroundImg = ImageIO.read(new File("Background.png"));
            g.drawImage(backgroundImg, 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void drawHand(Graphics g) {

    }
    public void drawRankings(Graphics g) {

    }
    public void drawCards(Graphics g) {

    }
    public void drawContracts(Graphics g) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
