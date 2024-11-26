//DeMichael Morgan
//COMP167.001
//11.25.24
//The PlayArea class serves as the primary game board, managing the ball, paddle, bricks, and collision detection within the game boundaries.
import edu.ncat.brickbreakerbackend.Level;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class PlayArea extends Pane {
    private Brick[][] bricks;        // 2D array of Brick objects
    private final int BASE_Y = 50;   // Starting Y position for bricks
    private int paWidth;             // Width of the play area
    private int paHeight;            // Height of the play area
    private Paddle paddle;           // The paddle object
    private Ball ball;               // The ball object
    private boolean ballInMotion = false;  // Keeps track of whether the ball is moving
    private ScorePane scorePane;     // For updating the player's score

    public PlayArea(int paWidth, int paHeight, Level level, ScorePane scorePane) {
        this.paWidth = paWidth; // Initialize play area width
        this.paHeight = paHeight; // Initialize play area height
        this.scorePane = scorePane; // Assign ScorePane for scoring

        this.setPrefSize(paWidth, paHeight); // Set preferred size for the play area

        // Create and add the paddle
        this.paddle = new Paddle(paWidth, paHeight);
        this.getChildren().add(paddle); // Add paddle to the play area

        // Create bricks for the current level
        createBricks(level);

        // Initialize the ball and set its starting position
        this.ball = new Ball(paWidth, paHeight, 90); // Ball starts with a straight-up direction
        resetBallPosition(); // Reset the ball position before starting
        this.getChildren().add(ball); // Add ball to the play area

        // Setup mouse event handlers
        setOnMouseMoved(this::handleMouseMoved);
        setOnMouseClicked(this::handleMouseClicked);

        // Start the game loop
        startGameLoop();
    }

    private void createBricks(Level level) {
        int rows = level.getNumBrickRows(); // Get the number of brick rows from the level
        int cols = paWidth / 35;            // Calculate the number of columns (fixed brick width)

        bricks = new Brick[rows][cols]; // Initialize the brick array

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Brick brick = new Brick(col * 35, BASE_Y + row * 20); // Create a brick
                brick.setPointValue(10); // Set points for hitting the brick
                bricks[row][col] = brick; // Add the brick to the array
                this.getChildren().add(brick); // Add brick to the play area
            }
        }
    }

    private void resetBallPosition() {
        double brickRowsHeight = bricks.length * 20; // Calculate the total height of the brick rows
        double ballStartY = BASE_Y + brickRowsHeight + 30; // Start below the last row of bricks
        ball.setCenterX(paddle.getLayoutX() + paddle.getWidth() / 2); // Align ball with paddle center
        ball.setCenterY(ballStartY); // Set ball's Y position
        ballInMotion = false; // Ball is stationary initially
    }

    private void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX(); // Get the X position of the mouse
        paddle.move(mouseX); // Move the paddle to match mouse position

        // Keep the ball on the paddle if it hasn't been launched
        if (!ballInMotion) {
            ball.setCenterX(paddle.getLayoutX() + paddle.getWidth() / 2);
        }
    }

    private void handleMouseClicked(MouseEvent event) {
        // Launch the ball on left mouse click if it's not already moving
        if (event.getButton() == MouseButton.PRIMARY && !ballInMotion) {
            ball.setDirection(randomLaunchAngle()); // Set a random launch angle
            ballInMotion = true; // Mark the ball as in motion
        }
    }

    private double randomLaunchAngle() {
        return 40 + Math.random() * 100; // Generate a random angle between 40 and 140 degrees
    }

    private void startGameLoop() {
        // Game loop to handle ball movement and collisions
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (ballInMotion) {
                    ball.move(); // Move the ball
                    checkPaddleCollision(); // Check if the ball hits the paddle
                    checkWallCollisions(); // Check if the ball hits a wall
                    handleBrickCollision(); // Handle collisions with bricks
                }
            }
        };
        gameLoop.start();
    }

    private void handleBrickCollision() {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                Brick brick = bricks[row][col]; // Get the brick

                // Check if the brick exists and if the ball intersects with it
                if (brick != null && brick.getBoundsInParent().intersects(ball.getBoundsInParent())) {
                    // Reverse ball direction based on collision position
                    if (ball.getCenterY() < brick.getLayoutY() || ball.getCenterY() > brick.getLayoutY() + brick.getHeight()) {
                        ball.setDirection(-ball.getDirection());
                    } else {
                        ball.setDirection(180 - ball.getDirection());
                    }

                    // Update the score and remove the brick
                    scorePane.incrementScore(brick.getPointValue());
                    bricks[row][col] = null; // Mark the brick as destroyed
                    this.getChildren().remove(brick); // Remove brick from play area
                    return; // Exit after handling one collision
                }
            }
        }
    }

    private void checkPaddleCollision() {
        // Check if the ball hits the paddle
        if (ball.getBottomEdge() >= paddle.getLayoutY() &&
                ball.getBottomEdge() <= paddle.getLayoutY() + Paddle.PADDLE_HEIGHT &&
                ball.getCenterX() >= paddle.getLayoutX() &&
                ball.getCenterX() <= paddle.getLayoutX() + Paddle.PADDLE_WIDTH) {

            ball.setDirection(-ball.getDirection()); // Reverse vertical direction

            // Adjust angle based on where the ball hits the paddle
            double paddleCenter = paddle.getLayoutX() + Paddle.PADDLE_WIDTH / 2.0;
            double hitPosition = ball.getCenterX() - paddleCenter;
            double angleAdjustment = (hitPosition / (Paddle.PADDLE_WIDTH / 2)) * 45;
            ball.setDirection(ball.getDirection() + angleAdjustment);

            // Keep ball above the paddle
            ball.setCenterY(paddle.getLayoutY() - ball.getRadius() - 1);
        }

        // Reset the ball if it falls below the paddle
        if (ball.getBottomEdge() >= paHeight) {
            resetBallPosition();
        }
    }

    private void checkWallCollisions() {
        // Check for collision with the left wall
        if (ball.getLeftEdge() <= 0) {
            ball.setCenterX(-ball.getCenterX()); // Reverse horizontal speed
            ball.setCenterX(ball.getRadius() + 1); // Keep ball inside bounds
        }

        // Check for collision with the right wall
        if (ball.getRightEdge() >= paWidth) {
            ball.setCenterX(-ball.getCenterX()); // Reverse horizontal speed
            ball.setCenterX(paWidth - ball.getRadius() - 1); // Keep ball inside bounds
        }

        // Check for collision with the top wall
        if (ball.getTopEdge() <= 0) {
            ball.getCenterY(); // Reverse vertical speed
            ball.setCenterY(ball.getRadius() + 1); // Keep ball inside bounds
        }
    }

}

