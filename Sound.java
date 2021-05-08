
import javax.sound.sampled.*;

public class Sound {
    public static synchronized void play(final String fileName)
    {
    	//This class was found on the internet, I do not write this.
    	//https://noobtuts.com/java/play-sounds
        // Note: use .wav files            
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
    }
}
