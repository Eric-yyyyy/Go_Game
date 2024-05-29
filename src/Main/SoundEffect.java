package Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class SoundEffect {
    private static Clip clip;
    public static void playLuoZiSound(String soundFileName) {
        try {
            URL url = SoundEffect.class.getResource(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void setVolume(float volumePercentage) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Get the range it can be set to
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            // Compute the value that should be set based on the percentage
            float value = min + (max - min) * (volumePercentage / 100.0f);
            // Ensure the value is within the allowable range
            if (value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }
            gainControl.setValue(value);
        }
    }

    private static String Music_Files[] = {
        "/Assets/Music1.wav"
    };
    public static Runnable MusicPlayer = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) { // Continuous playback
                    if (clip != null && clip.isRunning()) {
                        clip.stop(); // Stop the currently playing clip if any
                        clip.close(); // Close the previous clip to release resources
                    }

                    // Randomly select a music file
                    String soundFileName = Music_Files[new Random().nextInt(Music_Files.length)];
                    URL url = SoundEffect.class.getResource(soundFileName);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                    clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    setVolume(80.0f);
                    clip.start();

                    // Wait for the clip to finish playing
                    while (!clip.isRunning()) {
                        Thread.sleep(10); // Check every 10 milliseconds
                    }
                    while (clip.isRunning()) {
                        Thread.sleep(10); // Check every 10 milliseconds
                    }

                    // Here you could also add some logic if you want to pause between tracks, etc.
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    };
    public static void playBackGroundMusic() {
        new Thread(MusicPlayer).start();
    }
}
