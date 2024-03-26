import javax.swing.*;
import java.awt.*;
public class BackJPanel extends JPanel {

    private final Image backgroundImage;

    public BackJPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();

            for (int x = 0; x < width; x += backgroundImage.getWidth(this)) {
                for (int y = 0; y < height; y += backgroundImage.getHeight(this)) {
                    g.drawImage(backgroundImage, x, y, this);
                }
            }
        } else {
            // Gestisci il caso in cui non c'è un'immagine di sfondo
            // Esempio:
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
