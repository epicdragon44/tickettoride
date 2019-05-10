import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;
	private AudioInputStream input;
	private Clip clip;

	public GameFrame(String str) throws Exception {
		super(str);
		setContentPane(new BackgroundPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel(this);
		add(gamePanel);
		setVisible(true);
	}

	public void startDuluth()
	{
		
	}
	
	public void endDuluth()
	{
		
	}
	
	public void startCanada() throws Exception
	{
		setContentPane(new BackgroundPanel(ImageIO.read(new File("Canada.png"))));
		add(gamePanel);
		input= AudioSystem.getAudioInputStream(new File("Anthem.wav").getAbsoluteFile()); 
        clip = AudioSystem.getClip(); 
        clip.open(input);
        clip.loop(clip.LOOP_CONTINUOUSLY);
        clip.start();
		setVisible(true);
	}
	
	public void endCanada() throws Exception
	{
		setContentPane(new BackgroundPanel());
		add(gamePanel);
        clip.stop();
		setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
		JWindow window = new JWindow();
		window.setContentPane(new LoadingPanel(ImageIO.read(new File("yeet.jpg"))));
		window.setBounds(576, 250, 768, 550);
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

class LoadingPanel extends JComponent {
    private Image image;
    private Timer animateTimer2;

    private int x, y, length, height;
    public LoadingPanel(Image im) {
		image = im;
		x = 0;
		y = 495;
		length = 0;
		height = 18;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		animateTimer2 = new Timer(900, new AnimateLine());
		animateTimer2.start();
		g.setColor(new ColorType(170, 85, 85));
		g.fillRect(x, y, length, height);
		this.repaint();
	}

	class AnimateLine implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			length++;
		}
	}
}

class BackgroundPanel extends JComponent {
	private Image image;
	private boolean def;
	
	public BackgroundPanel() {
        image = null;
        def=true;
    }
	
    public BackgroundPanel(Image im) {
        image = im;
        def=false;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(def)
        {
        	g.setColor(Color.DARK_GRAY);
        	g.fillRect(0, 0, 1920, 1080);
        }
        else
        	g.drawImage(image, 0, 0, this);
    }
}
