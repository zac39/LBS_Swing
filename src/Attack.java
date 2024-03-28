import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger; // An int value that may be updated atomically. https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html

public class Attack implements Runnable {
    private final JPanel pt;
    private final JLabel cuore;
    private final AtomicInteger hp;
    private final int nAttacchi;
    private final Timer attackTimer;
    private int attackCounter;
    private final AtomicBoolean gameRunning;

    public Attack(JPanel pt, JLabel cuore, int nAttacchi, AtomicInteger hp, AtomicBoolean gameRunning) {
        this.pt = pt;
        this.cuore = cuore;
        this.hp = hp;
        this.nAttacchi = nAttacchi;
        this.attackCounter = 0;
        this.attackTimer = new Timer();
        this.gameRunning = gameRunning;
    }

    public void run() {
        attackTimer.scheduleAtFixedRate(new TimerTask() { // Thread safe, un timer per tutti i thread
            @Override
            public void run() {
                if (attackCounter < nAttacchi) {
                    launchAttack();
                    attackCounter++;
                } else {
                    attackTimer.cancel();
                }
            }
        }, 0, 500);
    }

    private void launchAttack() {
        int dy = (int)(Math.random() * 220) + 20;

        ImageIcon i = new ImageIcon("Assets/Images/Bone64Hor.png");
        JLabel bone = new JLabel(i);
        bone.setBounds(10, dy, 60, 10);

        pt.add(bone);
        Thread collisionT = new Thread(new Collision(hp, cuore, bone, gameRunning));
        collisionT.start();

        new Thread(() -> {
            while (bone.getX() < pt.getWidth()) {
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
            pt.remove(bone);
            collisionT.interrupt();
        }).start();

    }

    /*
    static private ImageIcon rotateImageIcon(ImageIcon picture, double angle) {
        int w = picture.getIconWidth();
        int h = picture.getIconHeight();
        BufferedImage image = new BufferedImage(h, w, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        double x = (h - w) / 2.0;
        double y = (w - h) / 2.0;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        g2.drawImage(picture.getImage(), at, null);
        g2.dispose();
        picture = new ImageIcon(image);
        return picture;
    }
    */
}