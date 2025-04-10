//ADAM
package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;

public class ChangePasswordPage {
    
    private final DatabaseHelper databaseHelper;

    public ChangePasswordPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    public void show(Stage primaryStage, User user) {
    	// new password text input
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        newPasswordField.setMaxWidth(250);
        
        // confirmation text input
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setMaxWidth(250);
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button updateButton = new Button("Set New Password");
        updateButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            
            // check if both inputs match
            if (!newPassword.equals(confirmPassword)) {
                errorLabel.setText("Passwords do not match.");
            } else if (newPassword.isEmpty()) {
                errorLabel.setText("Password cannot be empty.");
            } else {
                try {
                	// update user password
                    databaseHelper.updatePassword(user.getUserName(), newPassword);
                    errorLabel.setText("Password updated successfully.");
                    
                } catch (SQLException ex) {
                    errorLabel.setText("Error updating password.");
                    ex.printStackTrace();
                }
            }
        });
        
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(newPasswordField, confirmPasswordField, updateButton, errorLabel);
        
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }
}
//ADAM