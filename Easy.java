import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Easy {
    private static int lives;
    private static int score;
    private static int questionIndex;
    private static ScoreManager scoreManager = ScoreManager.getInstance();

    private static final String[][] QUESTIONS = {
            {"123 + 456 + 78 + 32 = ?", "689", "700", "695", "680", "750", "689"},
            {"700 - 250 - 100 - 75 = ?", "270", "275", "280", "265", "260", "275"},
            {"(150 + 250) - (100 + 50) = ?", "300", "200", "150", "250", "350", "250"},
            {"84 ÷ 7 = ?", "12", "14", "16", "13", "15", "12"},
            {"120 ÷ 15 = ?", "8", "10", "6", "9", "7", "8"}
    };

    public static void main(String[] args) {
        lives = 3;
        score = 0;
        questionIndex = 0;

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(212, 246, 255));
        frame.setTitle("Quiz Math - Easy");
        frame.setSize(350, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                SoundManager.shutdown();
            }
        });

        JLabel label = new JLabel(QUESTIONS[questionIndex][0], JLabel.CENTER);
        label.setBounds(23, 45, 290, 290);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        frame.add(label);

        JButton backButton = new JButton("<-");
        backButton.setBounds(10, 10, 50, 30);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.RED);
        backButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            frame.dispose();
            SwingUtilities.invokeLater(() -> new Level());
        });
        frame.add(backButton);

        JLabel livesLabel = new JLabel("Lives: " + lives, JLabel.RIGHT);
        livesLabel.setBounds(200, 10, 125, 30);
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.add(livesLabel);

        JLabel scoreLabel = new JLabel("Score: " + score, JLabel.LEFT);
        scoreLabel.setBounds(85, 10, 125, 30);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        frame.add(scoreLabel);

        JButton[] options = new JButton[5];
        Color[] softColors = {
                new Color(173, 216, 230),
                new Color(255, 182, 193),
                new Color(152, 251, 152),
                new Color(240, 230, 140),
                new Color(221, 160, 221)
        };

        int optionButtonSize = 55;
        int optionStartY = 350;
        int optionGapX = 100;
        int optionGapY = 70;
        int optionStartX = (frame.getWidth() - (2 * optionButtonSize + optionGapX)) / 2 + 20;

        for (int i = 0; i < 5; i++) {
            options[i] = createRoundButton(QUESTIONS[questionIndex][i + 1]);
            if (i < 2) {
                options[i].setBounds(optionStartX + i * (optionButtonSize + optionGapX), optionStartY, optionButtonSize, optionButtonSize);
            } else {
                options[i].setBounds(optionStartX - (optionButtonSize+optionGapX)/2 + (i - 2) * (optionButtonSize + optionGapX), optionStartY + optionGapY, optionButtonSize, optionButtonSize);
            }
            options[i].setBackground(softColors[i]);
            frame.add(options[i]);
        }

        Random random = new Random();
        int[][] velocities = new int[options.length][2];
        for(int i=0; i < velocities.length; i++){
            velocities[i][0] = (random.nextInt(7) + 2) * (random.nextBoolean() ? 1 : -1);
            velocities[i][1] = (random.nextInt(7) + 2) * (random.nextBoolean() ? 1 : -1);
        }

        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < options.length; i++) {
                    moveButtonWithBounce(options[i], velocities[i], frame, label);
                }
            }
        });
        timer.start();

        ActionListener checkAnswer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClickSound("musik/klik.wav");
                JButton source = (JButton) e.getSource();
                String correctAnswer = QUESTIONS[questionIndex][6];

                if (source.getText().equals(correctAnswer)) {
                    score += 20;
                    scoreLabel.setText("Score: " + score);

                    questionIndex++;
                    if (questionIndex < QUESTIONS.length) {
                        updateQuestion(label, options);
                    } else {
                        timer.stop();
                        showEndMessage(frame, true);
                    }
                } else {
                    lives--;
                    score -= 20;
                    if (score < 0) score = 0;

                    livesLabel.setText("Lives: " + lives);
                    scoreLabel.setText("Score: " + score);

                    if (lives <= 0) {
                        timer.stop();
                        showEndMessage(frame, false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Wrong Answer!", "Result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        for (JButton option : options) {
            option.addActionListener(checkAnswer);
        }

        frame.setLayout(null);
        frame.setVisible(true);
        SoundManager.playBackgroundMusic("musik/musik.wav");
    }

    private static JButton createRoundButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2.setColor(Color.LIGHT_GRAY);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                int radius = getWidth() / 2;
                return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= Math.pow(radius, 2);
            }
        };

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(55, 55));
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        return button;
    }

    private static void moveButtonWithBounce(JButton button, int[] velocity, JFrame frame, JLabel label) {
        int x = button.getX();
        int y = button.getY();

        x += velocity[0];
        y += velocity[1];

        int minX = 0;
        int minY = label.getY() + label.getHeight() + 5;
        int maxX = frame.getContentPane().getWidth() - button.getWidth();
        int maxY = frame.getContentPane().getHeight() - button.getHeight();

        if (x <= minX || x >= maxX) {
            velocity[0] = -velocity[0];
            x = Math.max(minX, Math.min(x, maxX));
        }

        if (y <= minY || y >= maxY) {
            velocity[1] = -velocity[1];
            y = Math.max(minY, Math.min(y, maxY));
        }

        button.setLocation(x, y);
    }

    private static void updateQuestion(JLabel label, JButton[] options) {
        label.setText(QUESTIONS[questionIndex][0]);
        for (int i = 0; i < options.length; i++) {
            if (QUESTIONS[questionIndex].length > i + 1) {
                options[i].setText(QUESTIONS[questionIndex][i + 1]);
            }
        }
    }

    private static void showEndMessage(JFrame frame, boolean success) {
        scoreManager.addScore(score);
        frame.getContentPane().removeAll();

        JLabel messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setBounds(50, 100, 250, 70);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel scoreDisplay = new JLabel("Your Score: " + score, JLabel.CENTER);
        scoreDisplay.setBounds(50, 170, 250, 50);
        scoreDisplay.setFont(new Font("Arial", Font.BOLD, 16));

        if (success) {
            messageLabel.setText("<html><div style='text-align: center;'>Kamu Sangat Hebat<br>Berhasil Melewati Level ini!</div></html>");
            JButton nextLevelButton = new JButton("Next Level");
            nextLevelButton.setBounds(100, 280, 150, 50);
            nextLevelButton.setBackground(Color.GREEN);
            nextLevelButton.setForeground(Color.WHITE);
            nextLevelButton.setFont(new Font("Arial", Font.BOLD, 16));
            nextLevelButton.addActionListener(e -> {
                SoundManager.playClickSound("musik/klik.wav");
                frame.dispose();
                SwingUtilities.invokeLater(() -> Medium.main(null));
            });
            frame.add(nextLevelButton);
        } else {
            messageLabel.setText("<html><div style='text-align: center;'>Coba lagi!</div></html>");
        }

        frame.add(messageLabel);
        frame.add(scoreDisplay);

        JButton replayButton = new JButton("Replay");
        replayButton.setBounds(100, 220, 150, 50);
        replayButton.setBackground(Color.BLUE);
        replayButton.setForeground(Color.WHITE);
        replayButton.setFont(new Font("Arial", Font.BOLD, 16));
        replayButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            frame.dispose();
            SwingUtilities.invokeLater(() -> Easy.main(null));
        });
        frame.add(replayButton);

        JButton backToMenuButton = new JButton("Menu Level");
        backToMenuButton.setBounds(100, success ? 340 : 280, 150, 50);
        backToMenuButton.setBackground(Color.ORANGE);
        backToMenuButton.setForeground(Color.WHITE);
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToMenuButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            frame.dispose();
            SwingUtilities.invokeLater(() -> new Level());
        });
        frame.add(backToMenuButton);

        frame.revalidate();
        frame.repaint();
    }
}