import javax.swing.*;
import java.awt.*;
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
    private int attackCounter, nAtt;
    private final AtomicBoolean gameRunning;

    public Attack(JPanel pt, JLabel cuore, AtomicInteger hp, AtomicBoolean gameRunning, int nAtt) {
        this.pt = pt;
        this.cuore = cuore;
        this.hp = hp;
        this.attackCounter = 0;
        this.attackTimer = new Timer();
        this.gameRunning = gameRunning;
        this.nAtt=nAtt;
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
                        }
                    }
                }, 0, 500);
                break;

            case(2):
                attack2();
        }
    }

    private void attack1() {
        int dy = (int)(Math.random() * 220) + 20;

        ImageIcon i = new ImageIcon("Assets/Images/Bone64Hor.png");
        JLabel bone = new JLabel(i);
        bone.setBounds(10, dy, 60, 10);

        pt.add(bone);
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

    private void attack2(){
        JLabel laser = new JLabel();
        laser.setOpaque(true);
        laser.setBackground(Color.white);
        laser.setBounds(0,20,0,130);
        pt.add(laser);

        Thread collisionT = new Thread(new Collision(hp, cuore, laser, gameRunning));
        collisionT.start();

    }   /*
        new Thread(() -> {
            while (laser.getWidth() < pt.getWidth()) {
                laser.setSize(laser.getWidth() + 1, laser.getY());
                synchronized (this) {
                    try {
                        // Wait for 5 milliseconds or until notified
                        wait(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            laser.setSize(0,0);
            pt.remove(laser);
            pt.repaint();
            pt.revalidate();
            collisionT.interrupt();
            try {
                collisionT.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("tuma");
        }).start();
        */
}