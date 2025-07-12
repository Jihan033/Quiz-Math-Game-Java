import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Hard {
    private static int lives;
    private static int score;
    private static int questionIndex;
    private static ScoreManager scoreManager = ScoreManager.getInstance();

    private static final String[][] QUESTIONS = {
            {"\u221A16 + \u221A25 = ?", "9", "10", "11", "8", "12", "9"},
            {"\u221A49 - \u221A9 = ?", "4", "5", "3", "2", "6", "4"},
            {"80 \u00D7 30% = ?", "24", "22", "26", "25", "20", "24"},
            {"300 \u00D7 5% = ?", "12", "13", "14", "15", "10", "15"},
            {"(500 \u00D7 10%) - (50 \u00D7 50%) = ?", "20", "25", "30", "35", "40","25"}
    };

    public static void main(String[] args) {
        lives = 3;
        score = 0;
        questionIndex = 0;

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(212, 246, 255));
        frame.setTitle("Quiz Math - Hard");
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

        for (int i = 0; i < 5; i++) {
            options[i] = createRoundButton(QUESTIONS[questionIndex][i + 1]);
            options[i].setBounds(50 + (i % 2) * 200, 400 + (i / 2) * 200, 55, 55);
            options[i].setBackground(softColors[i]);
            frame.add(options[i]);
        }

        int[][] velocities = {
                {5, 3}, {4, -4}, {-3, 5}, {-5, -3}, {3, -5}
        };

        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < options.length; i++) {
                    if (i < velocities.length && options[i] != null) {
                        moveButtonWithBounce(options[i], velocities[i], frame, label);
                    }
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
        messageLabel.setBounds(50, 150, 250, 50);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel scoreDisplay = new JLabel("Your Score: " + score, JLabel.CENTER);
        scoreDisplay.setBounds(50, 200, 250, 50);
        scoreDisplay.setFont(new Font("Arial", Font.BOLD, 16));

        if (success) {
            messageLabel.setText("<html><div style='text-align: center;'>Kamu Sangat Jenius<br>Berhasil Melewati Level ini!</div></html>");
            JButton menuButton = new JButton("Menu"); // Tombol ini mengarah ke Level (menu)
            menuButton.setBounds(100, 310, 150, 50);
            menuButton.setBackground(Color.GREEN);
            menuButton.setForeground(Color.WHITE);
            menuButton.setFont(new Font("Arial", Font.BOLD, 16));
            menuButton.addActionListener(e -> {
                SoundManager.playClickSound("musik/klik.wav");
                frame.dispose();
                SwingUtilities.invokeLater(() -> Level.main(null));
            });
            frame.add(menuButton);
        } else {
            messageLabel.setText("<html><div style='text-align: center;'>Coba lagi!</div></html>");
        }

        frame.add(messageLabel);
        frame.add(scoreDisplay);

        JButton replayButton = new JButton("Replay");
        replayButton.setBounds(100, 250, 150, 50);
        replayButton.setBackground(Color.BLUE);
        replayButton.setForeground(Color.WHITE);
        replayButton.setFont(new Font("Arial", Font.BOLD, 16));
        replayButton.addActionListener(e -> {
            SoundManager.playClickSound("musik/klik.wav");
            frame.dispose();
            SwingUtilities.invokeLater(() -> Hard.main(null));
        });
        frame.add(replayButton);

        frame.revalidate();
        frame.repaint();
    }
}