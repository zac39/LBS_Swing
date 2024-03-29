import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean; // Thread-proof
import java.util.concurrent.atomic.AtomicInteger;

public class TuMa extends JFrame implements MouseListener {

    // Costanti
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String IMAGE_PATH = "Assets/Images/";
    private static final String MUSIC_PATH = "Assets/Audio/Music/";
    private static final String FONT_PATH = "Assets/Font/";

    // Variabili
    private BackJPanel jpMain, jpSans, jpAtt;
    private JPanel jpGastSx,jpGastUp,jpGastDx;
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
        jlSans = new JLabel(new ImageIcon(IMAGE_PATH + "sans.png"));
        jlHeart = new JLabel(new ImageIcon(IMAGE_PATH + "cuore.png"));
        jlHp = new JLabel("HP: " + hp.get());

        jpMain = new BackJPanel(setBackground("background.png"));
        jpSans = new BackJPanel(setBackground("transparentBack.png"));
        jpAtt = new BackJPanel(setBackground("transparentBack.png"));

        jpGastSx = new JPanel();
        jpGastUp = new JPanel();
        jpGastDx = new JPanel();

        jpSans.setOpaque(false);
        jpAtt.setOpaque(false);

        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpSans.setLayout(new BoxLayout(jpSans, BoxLayout.Y_AXIS));
        jpAtt.setLayout(null);

        keyListener = new MyKeyListener(jlHeart);

        setSize(WIDTH,HEIGHT);
        jpAtt.setMaximumSize(new Dimension(600,300));
        jpSans.setMaximumSize(new Dimension(150, 225));

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
        jpAtt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpSans.setAlignmentX(Component.CENTER_ALIGNMENT);

        jpAtt.setBorder(BorderFactory.createLineBorder(Color.white, 10));

        jlHeart.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        jpSans.setMaximumSize(new Dimension(200, 250));
        jpAtt.setMaximumSize(new Dimension(600,300));

        jlHeart.setBounds(300, 150, 64 ,64);

        try {
            File fontFile = new File(FONT_PATH + "Undertale.ttf");
            System.out.println("Percorso del file del font: " + fontFile.getAbsolutePath());
            Font undertaleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            undertaleFont = undertaleFont.deriveFont(Font.PLAIN, 32);
            jlHp.setFont(undertaleFont);
            jlHp.setForeground(Color.white);
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del font: " + e.getMessage());
        } catch (FontFormatException e) {
            System.out.println("Formato del font non valido: " + e.getMessage());
        }


        jpAtt.add(jlHeart);
        jpSans.add(jlSans);

        jpMain.add(jlHp);
        jpMain.add(jpSans);
        jpMain.add(jpAtt);

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

    private void startBattle() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread bone1 = new Thread(new Attack(jpAtt,jlHeart,hp,gameRunning,latch,1));
        bone1.start();

        try {
            // Attende fino a quando il conteggio non raggiunge zero
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        Thread blaster = new Thread(new Attack(jpAtt,jlHeart,hp,gameRunning,latch,2));
        blaster.start();
    }

    private void gameOver() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false); // Fa cagare perchè non chiude letteralmente il gioco
            clip.stop();
            JOptionPane.showMessageDialog(null, "Coglione hai perso", "Game Over", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });

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

            JPanel panelClicked = (JPanel) jpMain.getComponentAt(e.getX(), e.getY());

            if (panelClicked != null) {
                int xOffset = e.getX() - panelClicked.getX();
                int yOffset = e.getY() - panelClicked.getY();

                System.out.println("Pannello cliccato: " + panelClicked.getName());
                System.out.println("Posizione del clic all'interno del pannello: (" + xOffset + ", " + yOffset + ")");
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

