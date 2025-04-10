package application;

import databasePart1.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button and expires after 40 seconds.
 */

public class InvitationPage {

    private Timer timer;

    /**
     * Displays the Invite Page in the provided primary stage.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the title of the page
        Label userLabel = new Label("Invite");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to generate the invitation code
        Button showCodeButton = new Button("Generate Invitation Code");

        // Label to display the generated invitation code
        Label inviteCodeLabel = new Label("");
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

        // Label to display the expiration status of the code
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
        
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit(); // Exit the JavaFX application
        });

        showCodeButton.setOnAction(a -> {
            // Cancel any existing timer if a new code is generated
            if (timer != null) {
                timer.cancel();
            }

            // Generate the invitation code using the databaseHelper and set it to the label
            String invitationCode = databaseHelper.generateInvitationCode();
            inviteCodeLabel.setText("Invitation Code: " + invitationCode);
            statusLabel.setText("This code will expire in 120 seconds.");
            

            // Set a timer to invalidate the code after 120 seconds
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        inviteCodeLabel.setText("");
                        statusLabel.setText("The code has timed out. Please generate a new one.");
                    });
                }
            }, 120000); // 120 seconds
        });

        // Handle window close event to ensure console remains active
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel, statusLabel,quitButton);
        Scene inviteScene = new Scene(layout, 800, 400);

        // Set the scene to primary stage
        primaryStage.setScene(inviteScene);
        primaryStage.setTitle("Invite Page");
    }
}