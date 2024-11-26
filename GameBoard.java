//DeMichael Morgan
//COMP167.001
//11.25.24
import edu.ncat.brickbreakerbackend.Level;
import edu.ncat.brickbreakerbackend.GameProfiles;
import edu.ncat.brickbreakerbackend.BrickRow;
import javafx.scene.layout.BorderPane;

public class GameBoard extends BorderPane {
    private PlayArea playArea;

    public GameBoard(Level[] levels, GameProfiles profiles, String profilesFilename) {
        // If levels are not provided, create default levels
        if (levels == null) {
            levels = createLevels();
        }

        // Create the ScorePane to display the score
        ScorePane scorePane = new ScorePane();

        // Initialize the PlayArea with the first level and the ScorePane
        this.playArea = new PlayArea(700, 600, levels[0], scorePane); // Add the missing comma

        // Set the PlayArea in the center and ScorePane at the bottom of the layout
        this.setCenter(playArea);
        this.setBottom(scorePane);
    }

    // Helper method to create default levels with brick rows
    private Level[] createLevels() {
        Level level1 = new Level(1, 3); // Level 1 with 3 rows of bricks
        Level level2 = new Level(2, 4); // Level 2 with 4 rows of bricks

        // Populate brick rows for level 1
        for (int i = 0; i < 3; i++) {
            level1.setBrickRow(i, new BrickRow());
        }

        // Populate brick rows for level 2
        for (int i = 0; i < 4; i++) {
            level2.setBrickRow(i, new BrickRow());
        }

        // Return an array of levels
        return new Level[]{level1, level2};
    }
}
