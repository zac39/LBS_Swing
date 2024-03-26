import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keylis implements KeyListener{
    private int dx;
    private int dy;
    private JLabel cuore;

    public keylis(int dx, int dy, JLabel cuore) {
        this.dx = dx;
        this.dy = dy;
        this.cuore = cuore;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A && dx>320) {
            cuore.setLocation(dx-=10,dy);
            System.out.println(dx);
        }

        if (key == KeyEvent.VK_D && dx<1120) {
            cuore.setLocation(dx+=10,dy);
            System.out.println(dx);
        }

        if (key == KeyEvent.VK_W && dy>520) {
            cuore.setLocation(dx,dy-=10);
            System.out.println(dy);
        }

        if (key == KeyEvent.VK_S && dy<870) {
            cuore.setLocation(dx,dy+=10);
            System.out.println(dy);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
