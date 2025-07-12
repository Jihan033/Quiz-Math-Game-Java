import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Level extends JFrame {
    private Clip backgroundMusicClip;

    public Level() {
        setTitle("Quiz Math - Pilih Level");
        setSize(350, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("Pilih Level", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBounds(0, 80, 350, 50);

        // Label untuk menampilkan rata-rata skor
        JLabel averageLabel = new JLabel("Rata-rata Skor: " + String.format("%.1f", ScoreManager.getInstance().getAverageScore()), JLabel.CENTER);
        averageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        averageLabel.setForeground(new Color(0, 102, 204));
        averageLabel.setBounds(0, 140, 350, 30);

        getContentPane().setBackground(new Color(212, 246, 255));
        add(titleLabel);
        add(averageLabel);

        int buttonWidth = 250;
        int buttonHeight = 50;
        int buttonX = (350 - buttonWidth) / 2;
        int startY = 220;
        int gap = 20;

        String[] buttonTexts = {"Easy", "Medium", "Hard"};

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = new JButton(buttonTexts[i]);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setBounds(buttonX, startY + i * (buttonHeight + gap), buttonWidth, buttonHeight);
            button.setBackground(new Color(0, 102, 204));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            String levelName = buttonTexts[i];
            button.addActionListener(e -> {
                SoundManager.playClickSound("musik/klik.wav");
                dispose();

                if (levelName.equals("Easy")) {
                    Easy.main(null);
                } else if (levelName.equals("Medium")) {
                    Medium.main(null);
                } else if (levelName.equals("Hard")) {
                    Hard.main(null);
                }
            });
            add(button);
        }

        // Tombol Reset Skor
        JButton resetButton = new JButton("Reset Skor");
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setBounds(120, 520, 110, 40);
        resetButton.setBackground(new Color(255, 69, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            ScoreManager.getInstance().clearScores();
            averageLabel.setText("Rata-rata Skor: 0.0");
        });
        add(resetButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBounds(10, 570, 100, 40);
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            dispose();
            SwingUtilities.invokeLater(() -> new Main());
        });
        add(backButton);

        setVisible(true);
        SoundManager.playBackgroundMusic("musik/musik.wav");
    }

    // ... (metode playMusic dan playClickSound tetap sama)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Level());
    }
}