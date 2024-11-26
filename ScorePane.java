//DeMichael Morgan
//COMP167.001
//11.25.24
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ScorePane extends HBox {
    private int score;
    private Label lblScore;

    public ScorePane() {
        this.score = 0;
        this.lblScore = new Label("Score: " + score);
        this.lblScore.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        this.getChildren().add(lblScore);
    }

    public void incrementScore(int points) {
        this.score += points;
        lblScore.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }
}
