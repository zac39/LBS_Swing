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
        int dy = (int)(Math.random() * (280 - 20 + 1)) + 20;

        ImageIcon i = new ImageIcon("Assets/Images/Bone64Hor.png");
        JLabel bone = new JLabel(i);
        bone.setBounds(10, dy, 60, 10);

        pt.add(bone);
        AtomicBoolean active = new AtomicBoolean(true);
        Thread collisionT = new Thread(new Collision(hp, cuore, bone, gameRunning, active));
        collisionT.start();

        new Thread(() -> {
            while (bone.getX() < 300) {
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
            active.set(false);
            collisionT.interrupt();

        }).start();

    }
}