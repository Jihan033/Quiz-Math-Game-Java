import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private Clip clip;

    public Main() {
        JLabel label = new JLabel();
        ImageIcon gambar = new ImageIcon("gambar.png");
        Image img = gambar.getImage();
        Image newImg = img.getScaledInstance(170, 200, Image.SCALE_SMOOTH);
        gambar = new ImageIcon(newImg);

        JFrame frame = new JFrame();
        frame.setTitle("Quiz Math");
        frame.setSize(350, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                SoundManager.shutdown();
            }
        });

        JLabel titleLabel = new JLabel("QUIZ MATH", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBounds(0, 80, 350, 50);

        frame.getContentPane().setBackground(new Color(212, 246, 255));

        JLayeredPane layeredPane = new JLayeredPane();
        frame.setContentPane(layeredPane);

        layeredPane.add(titleLabel, JLayeredPane.PALETTE_LAYER);

        label.setIcon(gambar);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setBounds(10, 400, 170, 200);
        layeredPane.add(label, JLayeredPane.PALETTE_LAYER);

        RandomNumbersPanel randomPanel = new RandomNumbersPanel();
        randomPanel.setBounds(0, 0, 350, 650);
        layeredPane.add(randomPanel, JLayeredPane.DEFAULT_LAYER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(125, 250, 110, 40);
        startButton.setBackground(new Color(0, 102, 204));
        startButton.setForeground(Color.WHITE);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        layeredPane.add(startButton, JLayeredPane.PALETTE_LAYER);

        startButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");

            frame.dispose();
            Level level = new Level();
            level.setVisible(true);
        });

        frame.setVisible(true);
        SoundManager.playBackgroundMusic("musik/musik.wav");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }

    public void playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInput);
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                this.clip.start();
            } else {
                // System.out.println("File musik tidak ditemukan (di Main.playMusic instance): " + filepath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playClickSound(String filepath) {
        try {
            File soundFile = new File(filepath);
            if (soundFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
                Clip clickClipInstance = AudioSystem.getClip();
                clickClipInstance.open(audioInput);
                clickClipInstance.start();
                clickClipInstance.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                });
            } else {
                // System.out.println("File klik tidak ditemukan (di Main.playClickSound instance): " + filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RandomNumbersPanel extends JPanel {
    private List<RandomNumber> randomNumbers = new ArrayList<>();
    private Random rand = new Random();

    public RandomNumbersPanel() {
        setBackground(new Color(212, 246, 255));
        for (int i = 0; i <= 20; i++) {
            randomNumbers.add(new RandomNumber());
        }
        Timer timer = new Timer(20, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (RandomNumber randomNumber : randomNumbers) {
            g2d.setFont(new Font("Arial", Font.BOLD, randomNumber.size));
            g2d.setColor(randomNumber.color);
            g2d.drawString(String.valueOf(randomNumber.number), randomNumber.xPos, randomNumber.yPos);

            randomNumber.xPos += randomNumber.xSpeed;
            randomNumber.yPos += randomNumber.ySpeed;

            if (randomNumber.xPos <= 0 || randomNumber.xPos >= getWidth() - randomNumber.size) {
                randomNumber.xSpeed = -randomNumber.xSpeed;
                if (randomNumber.xPos <= 0) randomNumber.xPos = 0;
                if (randomNumber.xPos >= getWidth() - randomNumber.size) randomNumber.xPos = getWidth() - randomNumber.size;
            }

            if (randomNumber.yPos <= randomNumber.size || randomNumber.yPos >= getHeight()) {
                randomNumber.ySpeed = -randomNumber.ySpeed;
                if (randomNumber.yPos <= randomNumber.size) randomNumber.yPos = randomNumber.size;
                if (randomNumber.yPos >= getHeight()) randomNumber.yPos = getHeight();
            }
        }
    }

    private class RandomNumber {
        int number;
        int xPos, yPos;
        int xSpeed, ySpeed;
        Color color;
        int size;

        public RandomNumber() {
            this.number = rand.nextInt(10);
            this.size = rand.nextInt(21) + 20;
            this.xPos = rand.nextInt(Math.max(1, 350 - this.size));
            this.yPos = rand.nextInt(Math.max(1, 650 - this.size)) + this.size;
            this.xSpeed = (rand.nextInt(2) + 1) * (rand.nextBoolean() ? 1 : -1);
            this.ySpeed = (rand.nextInt(2) + 1) * (rand.nextBoolean() ? 1 : -1);
            this.color = new Color(rand.nextInt(156) + 100, rand.nextInt(156) + 100, rand.nextInt(156) + 100);
        }
    }
}