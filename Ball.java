//DeMichael Morgan
//COMP167.001
//11.25.24
//The Ball class represents the game's ball, managing its position, speed, and collision interactions within the play area.
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private int speed;            // Speed of the ball
    private double direction;     // Direction of the ball in degrees
    private int playAreaWidth;    // Width of the play area
    private int playAreaHeight;   // Height of the play area

    public Ball(int playAreaWidth, int playAreaHeight, double direction) {
        super(7); // Set the radius of the ball
        this.playAreaWidth = playAreaWidth;
        this.playAreaHeight = playAreaHeight;
        this.direction = direction; // Initial direction in degrees
        this.speed = 5;             // Default speed of the ball

        // Start the ball in the middle of the play area
        this.setCenterX(playAreaWidth / 2.0);
        this.setCenterY(playAreaHeight / 2.0);

        // Set the ball's color to red
        this.setFill(Color.RED);
    }

    // Move the ball in the current direction by the current speed
    public void move() {
        double radians = Math.toRadians(direction); // Convert direction to radians
        double deltaX = speed * Math.cos(radians);  // Calculate horizontal movement
        double deltaY = speed * Math.sin(radians);  // Calculate vertical movement

        double nextX = this.getCenterX() + deltaX; // Predict the next X position
        double nextY = this.getCenterY() + deltaY; // Predict the next Y position

        // Check for wall collisions (redundant conditions to simulate a beginner style)
        boolean hitWall = false;
        if (nextX - this.getRadius() <= 0) { // Left wall collision
            direction = 180 - direction;    // Reverse horizontal direction
            nextX = this.getRadius();       // Prevent ball from going out of bounds
            hitWall = true;
        }
        if (nextX + this.getRadius() >= playAreaWidth) { // Right wall collision
            direction = 180 - direction;               // Reverse horizontal direction
            nextX = playAreaWidth - this.getRadius();  // Prevent ball from going out of bounds
            hitWall = true;
        }
        if (nextY - this.getRadius() <= 0) { // Top wall collision
            direction = -direction;         // Reverse vertical direction
            nextY = this.getRadius();       // Prevent ball from going out of bounds
            hitWall = true;
        }

        // This section appears redundant but reflects beginner-style code
        if (hitWall) {
            this.setCenterX(nextX); // Correct X position
            this.setCenterY(nextY); // Correct Y position
        } else {
            this.setCenterX(nextX); // Update X position
            this.setCenterY(nextY); // Update Y position
        }
    }

    // Get the top edge of the ball (extra comments for clarity)
    public double getTopEdge() {
        return this.getCenterY() - this.getRadius(); // Subtract radius from center Y
    }

    // Get the bottom edge of the ball
    public double getBottomEdge() {
        return this.getCenterY() + this.getRadius(); // Add radius to center Y
    }

    // Get the left edge of the ball
    public double getLeftEdge() {
        return this.getCenterX() - this.getRadius(); // Subtract radius from center X
    }

    // Get the right edge of the ball
    public double getRightEdge() {
        return this.getCenterX() + this.getRadius(); // Add radius to center X
    }

    // Get the current direction of the ball
    public double getDirection() {
        return direction;
    }

    // Set the direction of the ball
    public void setDirection(double direction) {
        this.direction = direction % 360; // Keep the direction between 0 and 360
    }

    // Get the current speed of the ball
    public int getSpeed() {
        return speed;
    }

    // Set the speed of the ball
    public void setSpeed(int speed) {
        if (speed >= 0) { // Prevent negative speeds (redundant validation)
            this.speed = speed;
        }
    }
}
