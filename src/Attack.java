import java.awt.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Attack extends AWTHelper implements Runnable {
    private final JPanel jpMain;
    private JPanel jpGastSx, jpGastDx;
    private final AtomicInteger hp;
    private final Timer attackTimer;
    private int attackCounter, nAtt;
    private final AtomicBoolean gameRunning;
    private final CountDownLatch latch;
    private JLayeredPane jlpAtt;
    private final JLabel jlHeart;

    public Attack(JPanel jpMain, AtomicInteger hp, AtomicBoolean gameRunning, CountDownLatch latch, int nAtt) {
        this.jpMain = jpMain;
        this.hp = hp;
        this.attackCounter = 0;
        this.attackTimer = new Timer();
        this.gameRunning = gameRunning;
        this.nAtt = nAtt;
        this.latch = latch;

        jlpAtt = (JLayeredPane) findChildComponentByName(jpMain, "jlpAtt");
        jlHeart = (JLabel) findChildComponentByName(jpMain, "jlHeart");
        jpGastSx = (JPanel) findChildComponentByName(jpMain, "jpGastSx");
        jpGastDx = (JPanel) findChildComponentByName(jpMain, "jpGastDx");
    }

    public void run() {
        if(gameRunning.get()){
            switch (nAtt) {
                case (1):
                    attackTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (attackCounter < 20) {
                                attack1();
                                attackCounter++;
                            } else {
                                attackTimer.cancel();
                                latch.countDown();
                            }
                        }
                    }, 0, 500);
                    break;

                case (2):
                        attack2(0, 10, 130);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    attack2(0, 150, 135);
            }
        }
    }

    private void attack1() {
        int dy = (int) (Math.random() * 220) + 20;

        ImageIcon i = new ImageIcon("Assets/Images/Bone64Hor.png");
        JLabel bone = new JLabel(i);
        bone.setBounds(10, dy, 60, 10);

        jlpAtt.add(bone, JLayeredPane.POPUP_LAYER);
        AtomicBoolean active = new AtomicBoolean(true);
        Thread collisionT = new Thread(new Collision(hp, jlHeart, bone, gameRunning, active));
        collisionT.start();

        new Thread(() -> {
            while (bone.getX() < jlpAtt.getWidth()) {
                bone.setLocation(bone.getX() + 1, dy);
                synchronized (this) {
                    try {
                        // Wait for 5 milliseconds or until notified
                        wait(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            jlpAtt.remove(bone);
            active.set(false);
            collisionT.interrupt();

        }).start();
    }

    private void attack2(int x, int y, int height) {
        JLabel laser = new JLabel();
        laser.setOpaque(true);
        laser.setBackground(Color.white);
        laser.setBounds(x, y, 0, height);
        //laser.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
        jlpAtt.add(laser, JLayeredPane.POPUP_LAYER);

        JLabel gasterBlaster = new JLabel(new ImageIcon("Assets/Images/GasterBlasterClosedSx.png"));
        gasterBlaster.setBounds(jpGastSx.getWidth() - 128, y, 128, 128);
        //gasterBlaster.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));

        AtomicBoolean active = new AtomicBoolean(true);
        Thread collisionT = new Thread(new Collision(hp, jlHeart, laser, gameRunning, active));
        collisionT.start();

        jpGastSx.add(gasterBlaster);
        jpGastSx.repaint();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterOpenedSx.png"));

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/Sounds/Blaster.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }

        new Thread(() -> {
            while (laser.getWidth() < jlpAtt.getWidth()) {
                laser.setSize(laser.getWidth() + 1, laser.getHeight());
                laser.repaint(); // Aggiorna il laser dopo aver modificato le dimensioni
                synchronized (this) {
                    try {
                        // Wait for 1 millisecond or until notified
                        wait(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterClosedSx.png"));

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            jlpAtt.remove(laser);
            jpMain.repaint();
            jpMain.revalidate();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            jpGastSx.remove(gasterBlaster);
            jpMain.repaint();
            jpMain.revalidate();
            active.set(false);
            collisionT.interrupt();

        }).start();
    }
}
