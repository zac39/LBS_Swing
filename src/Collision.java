import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Collision implements Runnable {
    private final AtomicInteger hp;
    private final JLabel cuore, obstacle;
    private final AtomicBoolean gameRunning;
    private final AtomicBoolean active;

    public Collision(AtomicInteger hp, JLabel cuore, JLabel obstacle, AtomicBoolean gameRunning, AtomicBoolean active) {
        this.hp = hp;
        this.cuore = cuore;
        this.obstacle = obstacle;
        this.active = active;
        this.gameRunning = gameRunning;
    }

    @Override
    public void run() {
        while (active.get() && gameRunning.get()) {
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
                if (hp.get() <= 0) {
                    gameRunning.set(false);
                }
            }
        }
    }

}