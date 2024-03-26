import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class tuMa extends JFrame implements ActionListener, MouseListener { //test git

    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static final int HEART_WIDTH = 65;
    private static final int HEART_HEIGHT = 62;
    private static final int BONE_WIDTH = 10;
    private static final int BONE_HEIGHT = 60;
    private static final String IMAGE_PATH = "Assets/Images/";
    private static final String MUSIC_PATH = "Assets/Audio/Music/";

    private JLabel sans, heart, border, bone;
    private BackJPanel panel;
    //private JPanel panel;
    private Font font;
    private int hp = 100;
    private boolean gameRunning;
    private Clip clip;
    private AudioInputStream audio;
    private Image dirtImg;

    private keylis k;
    public tuMa() {
        initializeComponents();
        createLayout();
        addListeners();
        startGameLoop();
        att1();
    }

    private void initializeComponents() {
        sans = new JLabel(new ImageIcon(IMAGE_PATH + "sans.png"));
        heart = new JLabel(new ImageIcon(IMAGE_PATH + "cuore.png"));
        bone = new JLabel(new ImageIcon(IMAGE_PATH + "bone64.png"));
        border = new JLabel();

        dirtImg = null;
        try{
            Image dirtImg = ImageIO.read(getClass().getResourceAsStream(IMAGE_PATH + "dirt.png"));
        } catch (IOException e){
            System.err.println("Error loading dirt image: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println(e);
        }

        panel = new BackJPanel(dirtImg);
        panel.setLayout(null);

        k=new keylis(550,600,heart);

        font = new Font("sans", Font.PLAIN, 25);
        gameRunning = true;

        setSize(WIDTH, HEIGHT);
        panel.setBackground(Color.BLACK);

        border.setBorder(BorderFactory.createLineBorder(Color.WHITE, 25));
        heart.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        try {
            audio = AudioSystem.getAudioInputStream(new File(MUSIC_PATH + "sans.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formauto audio o la presenza di esso");
        }
    }

    private void createLayout() {
        sans.setBounds(550, 0, 400, 450);
        heart.setBounds(k.getDx(), k.getDy(), HEART_WIDTH, HEART_HEIGHT);
        bone.setBounds(750, 600, BONE_WIDTH, BONE_HEIGHT);
        border.setBounds(300, 500, 900, 450);

        panel.add(bone);
        panel.add(border);
        panel.add(heart);
        panel.add(sans);
        add(panel);
    }

    private void addListeners() {
        addKeyListener(k);
        addMouseListener(this);
        this.setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (gameRunning) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Errore non so quale");
                }
            }
        }).start();
    }

    private void att1(){
        bone1 bone1=new bone1(k.getDx(),k.getDy(),panel);
        bone1.bone();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }



    public static void main(String[] args) {
        tuMa tuMaJF = new tuMa();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getX()+ " "+e.getY());
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