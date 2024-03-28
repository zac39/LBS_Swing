import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Collision implements Runnable {
    private final AtomicInteger hp;
    private final JLabel cuore, obstacle;
    // private final boolean active; // Volatile -> modificata da più thread contemporaneamente
    private final AtomicBoolean gameRunning;

    public Collision(AtomicInteger hp, JLabel cuore, JLabel obstacle, AtomicBoolean gameRunning) {
        this.hp = hp;
        this.cuore = cuore;
        this.obstacle = obstacle;
        // this.active = true;
        this.gameRunning = gameRunning;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    // Wait for 10 milliseconds or until notified
                    wait(10);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread() + " -> Sleep Interrupted");
                }
            }
            if (cuore.getBounds().intersects(obstacle.getBounds())) {
                hp.decrementAndGet();
                // System.out.println(hp);
                if (hp.get() <= 0) {
                    gameRunning.set(false);
                }
            }
        }
    }

}