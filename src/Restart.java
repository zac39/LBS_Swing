import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Restart extends JFrame implements ActionListener {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 960;
    private BackJPanel jpMain;
    private JButton jbRestart, jbQuit;

    public Restart(){

        jpMain = new BackJPanel(setBackground("Black.png"));


        setSize(WIDTH,HEIGHT);

        Clip clip;
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/dSounds/SoulBreak.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }

        JLabel cuoreRotto = new JLabel(new ImageIcon("Assets/Images/BrokenHeart.png"));
        cuoreRotto.setBounds((WIDTH / 2) - 16, (HEIGHT / 2) - 16, 32, 32);
        JLabel goGaster = new JLabel(new ImageIcon("Assets/Images/goGaster.png"));
        goGaster.setBounds((WIDTH / 2) - 40, (HEIGHT / 3) - 104, 80, 208);
        JLabel jlGay = null;

        try {
            File fontFile = new File("Assets/Font/Undertale.ttf");
            Font undertaleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            undertaleFont = undertaleFont.deriveFont(Font.PLAIN, 28);
            jlGay = new JLabel("Gay");
            jlGay.setFont(undertaleFont);
            jlGay.setForeground(Color.white);
            jlGay.setBounds(goGaster.getX() + 90, goGaster.getY(), 50, 50);
            jlGay.setOpaque(false);
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del font: " + e.getMessage());
        } catch (FontFormatException e) {
            System.out.println("Formato del font non valido: " + e.getMessage());
        }

        jbRestart = new JButton("Restart");
        jbQuit = new JButton("Quit");

        jbRestart.setBounds(cuoreRotto.getX()-50-32,cuoreRotto.getY()+132,100,50);
        jbQuit.setBounds(cuoreRotto.getX()+50-32,cuoreRotto.getY()+132,100,50);

        jbRestart.setBackground(Color.WHITE);
        jbQuit.setBackground(Color.WHITE);

        jbRestart.addActionListener(this);
        jbQuit.addActionListener(this);

        cuoreRotto.setOpaque(false);
        goGaster.setOpaque(false);
        jpMain.setOpaque(false);

        jpMain.setLayout(null);
        jpMain.add(goGaster);
        jpMain.add(jlGay);
        jpMain.add(cuoreRotto);
        jpMain.add(jbRestart);
        jpMain.add(jbQuit);

        add(jpMain);

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/Music/Determination.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbRestart){
            removeAll();
            repaint();
            revalidate();

            setVisible(false);
            dispose();
            TuMa tuma = new TuMa();
        } else {
            System.exit(0);
        }
    }

    private Image setBackground(String file) {
        Image backImg = null;
        try {
            File imageFile = new File("Assets/Images/" + file);
            backImg = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image format or file: " + e.getMessage());
        }

        return backImg;
    }
}

