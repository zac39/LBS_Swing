import javax.swing.*;

public class collision implements Runnable {

    private int hp;
    private JLabel cuore, obstacle;
    private boolean active = true;

    public collision(int hp, JLabel cuore, JLabel obstacle) {
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