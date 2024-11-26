//DeMichael Morgan
//COMP167.001
//11.25.24
import edu.ncat.brickbreakerbackend.GameProfiles;
import edu.ncat.brickbreakerbackend.BrickBreakerIO;
import edu.ncat.brickbreakerbackend.PlayerProfile;
import edu.ncat.brickbreakerbackend.Level;
import edu.ncat.brickbreakerbackend.BrickRow;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;

// ProfilePane class handles the player profile management GUI
public class ProfilePane extends VBox {
    private GameProfiles profiles; // Holds all player profiles
    private String profileFilename; // Name of profile file
    private String configFilename; // Name of config file

    // GUI controls
    private ComboBox<String> playerSelector; // Dropdown for players
    private TextField newPlayerField; // Field to enter new player name
    private Button searchButton; // Search for player button
    private Button createProfileButton; // Create new profile button
    private Button showProfilesButton; // Show all profiles button
    private Button startGameButton; // Start game button
    private Text statusMessage; // Displays messages to user
    private Label titleLabel; // Title of the pane

    // Colors for status messages
    private Color messageGood = Color.BLUE;
    private Color messageBad = Color.RED;

    // Constructor for ProfilePane
    public ProfilePane(String profileFilename, String configFilename) {
        this.profileFilename = profileFilename; // Save profile filename
        this.configFilename = configFilename; // Save config filename

        // Initialize GUI controls
        playerSelector = new ComboBox<>();
        playerSelector.setPromptText("Select a player...");

        newPlayerField = new TextField();
        newPlayerField.setPromptText("Type new player name here");

        searchButton = new Button("Search For Player");
        createProfileButton = new Button("Create New Profile");
        showProfilesButton = new Button("Show Profiles");
        startGameButton = new Button("Start Game");

        statusMessage = new Text("BrickBreaker Homescreen");
        statusMessage.setFill(messageGood);

        titleLabel = new Label("Player Profiles");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Load player profiles
        profiles = new GameProfiles();
        BrickBreakerIO.readProfiles(profiles, profileFilename);
        for (int i = 0; i < profiles.getNumPlayerProfiles(); i++) {
            playerSelector.getItems().add(profiles.getPlayerProfile(i).getName());
        }

        setupLayout(); // Setup the GUI layout
        setupButtonActions(); // Define button actions
    }

    // Sets up the layout of the ProfilePane
    private void setupLayout() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(15));

        // Add components to layout
        HBox profileBox = new HBox(10);
        profileBox.getChildren().addAll(new Label("Select Player:"), playerSelector);

        HBox newPlayerBox = new HBox(10);
        newPlayerBox.getChildren().addAll(new Label("New Player:"), newPlayerField);

        HBox buttonBox = new HBox(20);
        buttonBox.getChildren().addAll(searchButton, createProfileButton, showProfilesButton, startGameButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Add all sections to main container
        mainContainer.getChildren().addAll(titleLabel, profileBox, newPlayerBox, buttonBox, statusMessage);
        this.getChildren().add(mainContainer);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #f0f0f0;");
    }

    // Sets up actions for buttons
    private void setupButtonActions() {
        searchButton.setOnAction(e -> {
            String nameToSearch = newPlayerField.getText().trim();
            if (nameToSearch.isEmpty()) {
                updateStatus("Please enter a name to search!", messageBad);
            } else {
                if (searchPlayer(nameToSearch)) {
                    updateStatus("Player '" + nameToSearch + "' already exists", messageBad);
                } else {
                    updateStatus("Player name is available!", messageGood);
                }
            }
        });

        createProfileButton.setOnAction(e -> {
            String newPlayerName = newPlayerField.getText().trim();
            if (newPlayerName.isEmpty()) {
                updateStatus("Please enter a name!", messageBad);
            } else {
                if (!searchPlayer(newPlayerName)) {
                    addNewPlayer(newPlayerName);
                } else {
                    updateStatus("That player already exists!", messageBad);
                }
            }
        });

        showProfilesButton.setOnAction(e -> displayAllProfiles());

        startGameButton.setOnAction(e -> startGameBoard());
    }

    // Starts the game board
    private void startGameBoard() {
        Level level1 = new Level(1, 3);
        Level level2 = new Level(2, 3);

        // Setup levels
        for (int i = 0; i < 3; i++) {
            level1.setBrickRow(i, new BrickRow());
            level2.setBrickRow(i, new BrickRow());
        }

        Level[] levels = {level1, level2};
        GameBoard gameBoard = new GameBoard(levels, profiles, profileFilename);

        Scene gameScene = new Scene(gameBoard, 700, 600);
        Stage gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setTitle("Brick Breaker Game");
        gameStage.show();

        Stage currentStage = (Stage) this.getScene().getWindow();
        currentStage.hide();
    }

    // Searches for a player by name
    private boolean searchPlayer(String playerName) {
        for (int i = 0; i < profiles.getNumPlayerProfiles(); i++) {
            if (profiles.getPlayerProfile(i).getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    // Adds a new player to profiles
    private void addNewPlayer(String playerName) {
        PlayerProfile newProfile = new PlayerProfile(playerName);
        profiles.addPlayerProfile(newProfile);
        playerSelector.getItems().add(playerName);
        BrickBreakerIO.writeProfiles(profiles, profileFilename);
        updateStatus("Created new player: " + playerName, messageGood);
        newPlayerField.clear();
    }

    // Displays all profiles
    private void displayAllProfiles() {
        String selectedPlayer = playerSelector.getValue();
        if (selectedPlayer == null) {
            updateStatus("Please select a player first!", messageBad);
        } else {
            Stage profileStage = new Stage();
            profileStage.setTitle("Player Profiles");

            TextArea profilesText = new TextArea();
            profilesText.setEditable(false);

            StringBuilder sb = new StringBuilder();
            sb.append("Selected Player: ").append(selectedPlayer).append("\n\n");
            sb.append("All Player Profiles:\n");

            for (int i = 0; i < profiles.getNumPlayerProfiles(); i++) {
                PlayerProfile profile = profiles.getPlayerProfile(i);
                sb.append(profile.getName());
                if (profile.getName().equals(selectedPlayer)) {
                    sb.append(" (SELECTED)");
                }
                sb.append("\n");
            }
            profilesText.setText(sb.toString());

            VBox profileBoxDisplay = new VBox(10);
            profileBoxDisplay.setPadding(new Insets(10));
            profileBoxDisplay.getChildren().addAll(new Label("Player Profiles"), profilesText);

            profileStage.setScene(new Scene(profileBoxDisplay));
            profileStage.show();
        }
    }

    // Updates the status message
    private void updateStatus(String message, Color color) {
        statusMessage.setText(message);
        statusMessage.setFill(color);
    }
}
