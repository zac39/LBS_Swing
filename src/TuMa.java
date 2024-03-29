import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TuMa extends JFrame implements MouseListener {

    // Costanti
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 960;
    private static final String IMAGE_PATH = "Assets/Images/";
    private static final String MUSIC_PATH = "Assets/Audio/Music/";
    private static final String SOUND_PATH = "Assets/Audio/Sounds/";
    private static final String FONT_PATH = "Assets/Font/";

    // Variabili
    private JLayeredPane jlpAtt;
    private BackJPanel jpMain, jpSans;
    private JPanel jpGastSx, jpGastUp, jpGastDx, jpAttTot;
    private JLabel jlSans, jlHeart, jlHp;
    private MyKeyListener keyListener;
    private final AtomicBoolean gameRunning = new AtomicBoolean(true);
    private final AtomicInteger hp = new AtomicInteger(100);
    private Clip clip;

    public TuMa() {
        initializeComponents();
        createLayout();
        addListeners();
        startGameLoop();
        startBattle();
    }

    private void initializeComponents() {
        jlSans = new JLabel(new ImageIcon(IMAGE_PATH + "Sans.png"));
        jlHeart = new JLabel(new ImageIcon(IMAGE_PATH + "Heart.png"));

        jlHp = new JLabel("HP: " + hp.get());

        jpMain = new BackJPanel(setBackground("Black.png"));
        jpSans = new BackJPanel(setBackground("transparentBack.png"));

        jlpAtt = new JLayeredPane();

        jpGastSx = new JPanel();
        jpGastUp = new JPanel();
        jpGastDx = new JPanel();
        jpAttTot = new JPanel();

        jlSans.setName("jlSans");
        jlHeart.setName("jlHeart");
        jlHp.setName("jlHp");
        jpMain.setName("jpMain");
        jpSans.setName("jpSans");
        jlpAtt.setName("jlpAtt");
        jpGastSx.setName("jpGastSx");
        jpGastUp.setName("jpGastUp");
        jpGastDx.setName("jpGastDx");
        jpAttTot.setName("jpAttTot");

        jpSans.setOpaque(false);
        jlpAtt.setOpaque(false);
        jpAttTot.setOpaque(false);
        jpGastSx.setOpaque(false);
        jpGastDx.setOpaque(false);
        jpGastUp.setOpaque(false);

        keyListener = new MyKeyListener(jlHeart, jlpAtt);

        setSize(WIDTH,HEIGHT);

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(MUSIC_PATH + "sans.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }
    }

    private void createLayout() {
        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpSans.setLayout(new BoxLayout(jpSans, BoxLayout.Y_AXIS));
        jlpAtt.setLayout(null);
        jpAttTot.setLayout(new GridBagLayout());
        jpGastDx.setLayout(null);
        jpGastSx.setLayout(null);
        jpGastUp.setLayout(null);

        jpAttTot.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlpAtt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpSans.setAlignmentX(Component.CENTER_ALIGNMENT);

        jlpAtt.setBorder(BorderFactory.createLineBorder(Color.white, 10));
        //jpAttTot.setBorder(BorderFactory.createLineBorder(Color.green, 10));
        //jpGastSx.setBorder(BorderFactory.createLineBorder(Color.magenta, 10));
        //jpGastDx.setBorder(BorderFactory.createLineBorder(Color.magenta, 10));
        //jpGastUp.setBorder(BorderFactory.createLineBorder(Color.magenta, 10));
        // jlHeart.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        jpSans.setMaximumSize(new Dimension(200, 250));
        jlpAtt.setMinimumSize(new Dimension(600, 300));
        jpAttTot.setMaximumSize(new Dimension(900, 500));

        jlHeart.setBounds(300, 150, 32, 32);

        try {
            File fontFile = new File(FONT_PATH + "Undertale.ttf");
            Font undertaleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            undertaleFont = undertaleFont.deriveFont(Font.PLAIN, 32);
            jlHp.setFont(undertaleFont);
            jlHp.setForeground(Color.white);
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del font: " + e.getMessage());
        } catch (FontFormatException e) {
            System.out.println("Formato del font non valido: " + e.getMessage());
        }

        jpSans.add(jlSans);

        GridBagConstraints gbc = new GridBagConstraints();

        // Impostazione dei vincoli per far riempire i pannelli in altezza
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;


        // Aggiunta di jpGastSx nella prima colonna
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.35; // Larghezza di 100 pixel
        jpAttTot.add(jpGastSx, gbc);

        // Aggiunta di jlpAtt nella seconda colonna
        gbc.gridx = 1;
        gbc.weightx = 0.6; // Larghezza di 600 pixel
        jpAttTot.add(jlpAtt, gbc);

        // Aggiunta di jpGastDx nella terza colonna
        gbc.gridx = 2;
        gbc.weightx = 0.35; // Larghezza di 100 pixel
        jpAttTot.add(jpGastDx, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.35;
        jpAttTot.add(jpGastUp, gbc);

        jlpAtt.add(jlHeart, JLayeredPane.DEFAULT_LAYER);

        jpMain.add(jlHp);
        jpMain.add(jpSans);
        jpMain.add(jpAttTot);

        add(jpMain);

        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    private void addListeners(){
        addKeyListener(keyListener);
        addMouseListener(this);
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (gameRunning.get()) {
                try {
                    MyKeyListener.aggiornaMovimento();
                    jlHp.setText("HP: " + hp.get());

                    synchronized (this) {
                        // Wait for 10 milliseconds or until notified
                        wait(10);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Errore non so quale");
                }
            }
            gameOver();
        }).start();
    }

    private void gameOver() {
        clip.stop();
        removeAll();
        repaint();
        revalidate();

        setVisible(false);
        dispose();
        Restart restart = new Restart();
    }

    private void startBattle() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread bone1 = new Thread(new Attack(jpMain,hp,gameRunning,latch,1));
        bone1.start();

        try {
            // Attende fino a quando il conteggio non raggiunge zero
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        Thread blaster = new Thread(new Attack(jpMain,hp,gameRunning,latch,2));
        blaster.start();
    }

    private Image setBackground(String file) {
        Image backImg = null;
        try {
            File imageFile = new File(IMAGE_PATH + file);
            backImg = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image format or file: " + e.getMessage());
        }

        return backImg;
    }















    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(Thread.activeCount());

        try{
            JPanel panelClicked = (JPanel) jpMain.getComponentAt(e.getX(), e.getY());

            Rectangle r = getBounds();
            int h = r.height;
            int w = r.width;

            System.out.println(w + "x" + h);

            if (panelClicked != null) {
                int xOffset = e.getX() - panelClicked.getX();
                int yOffset = e.getY() - panelClicked.getY();

                System.out.println("Pannello cliccato: " + panelClicked.getName());
                System.out.println("Posizione del clic all'interno del pannello: (" + xOffset + ", " + yOffset + ")");
            }
        } catch(ClassCastException ex){
            System.out.println("Non è un JPanel");
        }

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
