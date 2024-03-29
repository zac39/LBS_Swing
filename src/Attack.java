import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger; // An int value that may be updated atomically. https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html

public class Attack implements Runnable {
    private final JPanel jpMain;
    private final JLabel cuore;
    private final AtomicInteger hp;
    private final Timer attackTimer;
    private int attackCounter, nAtt;
    private final AtomicBoolean gameRunning;
    private final CountDownLatch latch;


    public Attack(JPanel jpMain, JLabel cuore, AtomicInteger hp, AtomicBoolean gameRunning, CountDownLatch latch, int nAtt) {
        this.jpMain = jpMain;
        this.cuore = cuore;
        this.hp = hp;
        this.attackCounter = 0;
        this.attackTimer = new Timer();
        this.gameRunning = gameRunning;
        this.nAtt=nAtt;
        this.latch = latch;
    }

    public void run() {
        switch (nAtt){
            case(1):
                attackTimer.scheduleAtFixedRate(new TimerTask() { // Thread safe, un timer per tutti i thread
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

            case(2):
                attack2(0,10,130);
                try {
                    TimeUnit.SECONDS.sleep(6);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                attack2(0,140,130);
        }
    }

    private void attack1() {
        int dy = (int)(Math.random() * 220) + 20;

        ImageIcon i = new ImageIcon("Assets/Images/Bone64Hor.png");
        JLabel bone = new JLabel(i);
        bone.setBounds(10, dy, 60, 10);

        jpMain.add(bone, JLayeredPane.POPUP_LAYER);
        AtomicBoolean active = new AtomicBoolean(true);
        Thread collisionT = new Thread(new Collision(hp, cuore, bone, gameRunning, active));
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
            active.set(false);
            collisionT.interrupt();

        }).start();
    }

    private void attack2(int x,int y, int height){
        JLabel laser = new JLabel();
        laser.setOpaque(true);
        laser.setBackground(Color.white);
        laser.setBounds(x,y,0,height);
        pt.add(laser, JLayeredPane.POPUP_LAYER);

        AtomicBoolean active = new AtomicBoolean(true);
        Thread collisionT = new Thread(new Collision(hp, cuore, laser, gameRunning, active));
        collisionT.start();

        new Thread(() -> {
            while (laser.getWidth() < pt.getWidth()) {
                laser.setSize(laser.getWidth() + 1, laser.getHeight());
                synchronized (this) {
                    try {
                        // Wait for 5 milliseconds or until notified
                        wait(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pt.remove(laser);
            pt.repaint();
            pt.revalidate();
            active.set(false);
            collisionT.interrupt();

        }).start();

    }

}