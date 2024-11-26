//DeMichael Morgan
//COMP167.001
//11.25.24
//The Paddle class represents the player's paddle,
// its allows movement within the game
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    public static final int PADDLE_WIDTH = 70;
    public static final int PADDLE_HEIGHT = 10;
    private static final int BASE_Y = 550;

    public Paddle(int paWidth, int canvasWidth) {
        super(PADDLE_WIDTH, PADDLE_HEIGHT);
        this.setLayoutX((canvasWidth - PADDLE_WIDTH) / 2);
        this.setLayoutY(BASE_Y);
        this.setFill(Color.ROYALBLUE);
    }

    public void move(double xLoc) {
        double halfWidth = PADDLE_WIDTH / 2.0;
        double newPosition = xLoc - halfWidth;
        this.setLayoutX(newPosition);
    }
}
