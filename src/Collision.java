import javax.swing.*;

public class Collision implements Runnable {

    private int hp;
    private final JLabel cuore, obstacle;
    private final boolean active = true;

    public Collision(int hp, JLabel cuore, JLabel obstacle) {
        this.hp = hp;
        this.cuore = cuore;
        this.obstacle = obstacle;
    }
    @Override
    public void run() {
        while (active) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (cuore.getBounds().intersects(obstacle.getBounds())) {
                hp -= 1;
                System.out.println(hp);
            }
        }
    }
}