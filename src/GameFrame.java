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
	private Clip clip, backMusic, hover, click, ching, wrong;
	private boolean mute;

	public GameFrame(String str) throws Exception {
		super(str);
		setContentPane(new BackgroundPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel(this);
		add(gamePanel);
		mute=false;
		clip=null;
        backMusic = AudioSystem.getClip(); 
        backMusic.open(AudioSystem.getAudioInputStream(new File("resources/Ragtime.wav").getAbsoluteFile()));
        backMusic.loop(Clip.LOOP_CONTINUOUSLY);
        backMusic.start();
        click = AudioSystem.getClip(); 
        click.open(AudioSystem.getAudioInputStream(new File("resources/click.wav").getAbsoluteFile()));
        hover = AudioSystem.getClip(); 
        hover.open(AudioSystem.getAudioInputStream(new File("resources/hover.wav").getAbsoluteFile()));
        ching = AudioSystem.getClip(); 
        ching.open(AudioSystem.getAudioInputStream(new File("resources/chaChing.wav").getAbsoluteFile()));
        wrong = AudioSystem.getClip(); 
        wrong.open(AudioSystem.getAudioInputStream(new File("resources/wrong.wav").getAbsoluteFile()));
		setVisible(true);
	}

	public void startDuluth() throws Exception
	{
		if(mute||(clip!=null&&clip.isRunning()))
			return;
		backMusic.stop();
		setContentPane(new BackgroundPanel(ImageIO.read(new File("resources/Duluth.png"))));
		add(gamePanel);
		input= AudioSystem.getAudioInputStream(new File("resources/RealDuluth.wav").getAbsoluteFile()); 
        clip = AudioSystem.getClip(); 
        clip.open(input);
        clip.loop(clip.LOOP_CONTINUOUSLY);
        clip.start();
		setVisible(true);
	}
	
	public void endDuluth()
	{
		setContentPane(new BackgroundPanel());
		add(gamePanel);
		if(clip!=null)
			clip.stop();
        clip=null;
        backMusic.start();
		setVisible(true);
	}
	
	public void startCanada() throws Exception
	{
		if(mute||(clip!=null&&clip.isRunning()))
			return;
		backMusic.stop();
		setContentPane(new BackgroundPanel(ImageIO.read(new File("resources/Canada.png"))));
		add(gamePanel);
		input= AudioSystem.getAudioInputStream(new File("resources/Anthem.wav").getAbsoluteFile()); 
        clip = AudioSystem.getClip(); 
        clip.open(input);
        clip.loop(clip.LOOP_CONTINUOUSLY);
        clip.start();
		setVisible(true);
	}
	
	public void endCanada()
	{
		setContentPane(new BackgroundPanel());
		add(gamePanel);
		if(clip!=null)
			clip.stop();
        clip=null;
        backMusic.start();
		setVisible(true);
	}
	
	public void mute()
	{
		mute=true;
		if(clip!=null)
			clip.stop();
		clip=null;
		if(backMusic!=null)
			backMusic.stop();
		setContentPane(new BackgroundPanel());
		add(gamePanel);
		setVisible(true);
	}
	
	public void unmute()
	{
		mute=false;
		backMusic.start();
	}
	
	public void click() throws Exception
	{
		if(mute)
			return;
		click.stop();
		click = AudioSystem.getClip(); 
        click.open(AudioSystem.getAudioInputStream(new File("resources/click.wav").getAbsoluteFile()));
		click.start();
	}
	
	public void hover() throws Exception
	{
		if(mute)
			return;
		hover.stop();
		hover = AudioSystem.getClip(); 
        hover.open(AudioSystem.getAudioInputStream(new File("resources/hover.wav").getAbsoluteFile()));
		hover.start();
	}
	
	public void ching() throws Exception
	{
		if(mute)
			return;
		ching.stop();
		ching = AudioSystem.getClip(); 
        ching.open(AudioSystem.getAudioInputStream(new File("resources/chaChing.wav").getAbsoluteFile()));
		ching.start();
	}
	
	public void wrong() throws Exception
	{
		if(mute)
			return;
		wrong.stop();
		wrong = AudioSystem.getClip(); 
        wrong.open(AudioSystem.getAudioInputStream(new File("resources/wrong.wav").getAbsoluteFile()));
		wrong.start();
	}
	
	public static void main(String[] args) throws Exception {
		JWindow window = new JWindow();
		window.setContentPane(new LoadingPanel(ImageIO.read(new File("resources/yeet.jpg"))));
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
