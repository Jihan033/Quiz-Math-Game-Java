import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static Clip backgroundMusicClip;
    private static String currentMusicFilepath;

    // Metode untuk memainkan musik latar belakang
    public static synchronized void playBackgroundMusic(String filepath) {
        if (filepath.equals(currentMusicFilepath) && backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            return;
        }

        // Hentikan musik sebelumnya jika ada atau jika musik yang diminta berbeda
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
            backgroundMusicClip = null; // Pastikan direset
        }

        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioInput);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusicClip.start();
                currentMusicFilepath = filepath; 
                // System.out.println("SoundManager: Memainkan " + filepath); // Untuk debugging
            } else {
                System.out.println("SoundManager: File musik tidak ditemukan: " + filepath);
                currentMusicFilepath = null;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            if (backgroundMusicClip != null) {
                backgroundMusicClip.close();
            }
            backgroundMusicClip = null;
            currentMusicFilepath = null;
        }
    }

    // Metode untuk menghentikan musik latar belakang
    public static synchronized void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            if (backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
            }
            backgroundMusicClip.close();
            backgroundMusicClip = null;
        }
        currentMusicFilepath = null; 
        
    }

    // Metode untuk memainkan suara klik
    public static void playClickSound(String filepath) {
        try {
            File soundFile = new File(filepath);
            if (soundFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
                Clip clickClip = AudioSystem.getClip();
                clickClip.open(audioInput);
                clickClip.start();
                clickClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                });
            } else {
                System.out.println("SoundManager: File klik tidak ditemukan: " + filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Panggil ini saat aplikasi benar-benar ditutup untuk membersihkan sumber daya
    public static synchronized void shutdown() {
        stopBackgroundMusic();
       
    }
}
