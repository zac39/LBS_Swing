import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Attack implements Runnable {
    private final JPanel pt;
    private JLabel bone, cuore;
    private final int nAttacchi,hp;
    private Thread collisonT;

    public Attack(JPanel pt, JLabel cuore, int nAttacchi, int hp) {
        this.pt = pt;
        this.cuore = cuore;
        this.nAttacchi = nAttacchi;
        this.hp=hp;
    }

    public void run() {

        for (int j = 0; j < nAttacchi; j++) {
            int dx = cuore.getX();
            int dy = cuore.getY();

            ImageIcon i = new ImageIcon("Assets/Images/bone64.png");
            bone = new JLabel(rotateImageIcon(i, 90));
            bone.setBounds(dx - 100, dy, 60, 10);

            pt.add(bone);
            collisonT=new Thread(new Collision(hp,cuore,bone));
            collisonT.start();
            while (bone.getX() < 1100) {
                bone.setLocation(bone.getX() + 1, dy);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            collisonT.interrupt();
        }

    }

    static private ImageIcon rotateImageIcon(ImageIcon picture, double angle) {
        int w = picture.getIconWidth();
        int h = picture.getIconHeight();
        BufferedImage image = new BufferedImage(h, w, BufferedImage.TYPE_INT_RGB); // other options, see api
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
}