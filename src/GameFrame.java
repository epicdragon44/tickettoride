import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;

	public GameFrame(String str) throws Exception {
		super(str);
		getContentPane().setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		add(gamePanel);
		setVisible(true);
	}

	public void startDuluth()
	{
		
	}
	
	public void endDuluth()
	{
		
	}
	
	public void startCanada()
	{
		
	}
	
	public void endCanada()
	{
		
	}
	
	public void startSleep()
	{
		
	}
	
	public void endSleep()
	{
		
	}
	
	public static void main(String[] args) throws Exception {
		JWindow window = new JWindow();
		window.setContentPane(new BackgroundPanel(ImageIO.read(new File("yeet.jpg"))));
		window.setBounds(576, 250, 768, 513);
		window.setVisible(true);
		try {
		    Thread.sleep(2000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		window.setVisible(false);
		GameFrame game = new GameFrame("Ticket to Ride");
	}
}

class BackgroundPanel extends JComponent {
    private Image image;
    public BackgroundPanel(Image im) {
        image = im;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
