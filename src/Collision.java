import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Collision implements Runnable {
    private final AtomicInteger hp;
    private final JLabel cuore, obstacle;
    private final AtomicBoolean gameRunning;
    private final AtomicBoolean active;
    private Clip clip;
    private AudioInputStream audio;

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
                    // System.out.println(obstacle.getBounds());
                    // System.out.println(cuore.getBounds());
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

                if(clip == null){
                    try {
                        audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/Sounds/SoulDamage.wav").getAbsoluteFile());
                        clip = AudioSystem.getClip();
                        clip.open(audio);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(!clip.isActive()){
                    clip.start();
                }
            }
        }
    }

}