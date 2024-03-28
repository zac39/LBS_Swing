import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class MyKeyListener implements KeyListener {

    private static JLabel label;
    private final int velocitaBase = 3;
    private static int velocitaX = 0;
    private static int velocitaY = 0;
    private Set<Integer> keysPressed = new HashSet<>();

    /*
    Il codice private Set<Integer> keysPressed = new HashSet<>(); crea un insieme (Set) chiamato keysPressed che tiene traccia dei codici dei tasti premuti.
    Un Set è una collezione che non consente duplicati, quindi ogni codice del tasto sarà presente al massimo una volta nell'insieme.

        - Quando viene premuto un tasto (keyPressed), il codice del tasto viene aggiunto all'insieme keysPressed utilizzando il metodo add().
        - Quando viene rilasciato un tasto (keyReleased), il codice del tasto viene rimosso dall'insieme keysPressed utilizzando il metodo remove().
        - In updateVelocity(), scorriamo tutti i codici dei tasti presenti nell'insieme keysPressed e aggiorniamo le variabili velocitaX e velocitaY in base ai tasti premuti. Questo ci consente di gestire i casi in cui vengono premuti o rilasciati più tasti contemporaneamente.

    In sostanza, l'insieme keysPressed è utilizzato per tenere traccia di quali tasti sono attualmente premuti, consentendo una gestione più complessa e dinamica dei movimenti del personaggio.
    */

    public MyKeyListener(JLabel label) {
        this.label = label;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.add(keyCode);
        updateVelocity();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.remove(keyCode);
        updateVelocity();
    }

    private void updateVelocity() {
        velocitaX = 0;
        velocitaY = 0;

        for (int keyCode : keysPressed) {
            switch (keyCode) {
                case KeyEvent.VK_W:
                    velocitaY -= velocitaBase;
                    break;
                case KeyEvent.VK_S:
                    velocitaY += velocitaBase;
                    break;
                case KeyEvent.VK_A:
                    velocitaX -= velocitaBase;
                    break;
                case KeyEvent.VK_D:
                    velocitaX += velocitaBase;
                    break;
            }
        }
    }

    public static void aggiornaMovimento() {
        int nuovaX = label.getX() + velocitaX;
        int nuovaY = label.getY() + velocitaY;
        label.setLocation(nuovaX, nuovaY);
    }
}
