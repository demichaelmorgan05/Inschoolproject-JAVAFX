//DeMichael Morgan
//COMP167.001
//11.25.24
import edu.ncat.brickbreakerbackend.Level;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    private int pointValue;
    private static final int BRICK_WIDTH = 35; // Given by instructions
    private static final int BRICK_HEIGHT = 20;

    public Brick(int xLoc, int yLoc) {
        super(BRICK_WIDTH, BRICK_HEIGHT);
        this.setLayoutX(xLoc);
        this.setLayoutY(yLoc);
        this.setFill(Color.DARKBLUE);
        this.pointValue = pointValue;
    }

    public Brick(Level level) {
        // Placeholder constructor if needed later
    }

    public static int getBrickWidth() {
        return BRICK_WIDTH;
    }

    public static int getBrickHeight() {
        return BRICK_HEIGHT;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }
}
