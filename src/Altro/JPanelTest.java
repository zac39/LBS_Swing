package Altro;

import javax.swing.*;
import java.awt.*;

public class JPanelTest extends JFrame {

    private final JPanel jp1, jp2, jpMain, jp1Int1, jp1Int3;

    public JPanelTest(){

        jpMain = new JPanel();
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp1Int1 = new JPanel();
        jp1Int3 = new JPanel();

        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
        jp1Int1.setLayout(null);
        jp1Int3.setLayout(null);

        jp1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp2.setAlignmentX(Component.CENTER_ALIGNMENT);

        jpMain.setBorder(BorderFactory.createLineBorder(Color.magenta, 15));
        jp1.setBorder(BorderFactory.createLineBorder(Color.yellow, 15));
        jp2.setBorder(BorderFactory.createLineBorder(Color.blue, 15));
        jp1Int1.setBorder(BorderFactory.createLineBorder(Color.green, 15));
        jp1Int3.setBorder(BorderFactory.createLineBorder(Color.red, 15));

        jp1.setMaximumSize(new Dimension(600,300));
        jp2.setMaximumSize(new Dimension(150, 225));

        jp1Int1.setMaximumSize(new Dimension(100, 50));

        jp1.add(jp1Int3); // red
        jp2.add(jp1Int1); // green

        jpMain.add(jp2); // blue
        jpMain.add(jp1); // yellow

        add(jpMain);

        setSize(800,600);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JPanelTest test = new JPanelTest();
        System.out.println(test);
    }
}

