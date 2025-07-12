import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private static ScoreManager instance;
    private List<Integer> scores = new ArrayList<>();

    private ScoreManager() {} // Private constructor untuk singleton

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public List<Integer> getScores() {
        return new ArrayList<>(scores); // Return copy untuk menjaga immutability
    }

    public double getAverageScore() {
        if (scores.isEmpty()) return 0;

        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return (double) sum / scores.size();
    }

    public void clearScores() {
        scores.clear();
    }
}