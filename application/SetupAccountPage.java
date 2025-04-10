package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Users First / Last name"); 
        nameField.setMaxWidth(250); 
        
        //email
        TextField emailnameField = new TextField();
        emailnameField.setPromptText("Enter Email"); 
        emailnameField.setMaxWidth(250); 
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            String email = emailnameField.getText(); //added email
            String name = nameField.getText(); //added password 
            
          //Check password and userName
			String userNameValidationResult = UserNameRecognizer.checkForValidUserName(userName);
			if(!userNameValidationResult.isEmpty()) { 
				errorLabel.setText("Username error: " + userNameValidationResult); 
				return;
			}

			String passwordValidationResult = PasswordEvaluator.evaluatePassword(password);
            if (!passwordValidationResult.isEmpty()) {
                errorLabel.setText("**Error**\nPassword error: " + passwordValidationResult);
                return;
            }
            String emailValidationResult = EmailRecognizer.checkForValidEmail(email);
            if (!emailValidationResult.isEmpty()) {
                errorLabel.setText("Email error: " + emailValidationResult);
                return;
            }

            
            //name 
            String nameValidationResult = nameRecognizer.EvaluateName(name); 
            if(!nameValidationResult.isEmpty()) { 
            	errorLabel.setText("Name error: " + nameValidationResult); 
            	return;
            }
    		
            try {
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		// Validate the invitation code
            		if(databaseHelper.validateInvitationCode(code)) {
            			
            			// Create a new user and register them in the database
            			User user=new User(userName, password, name, email, "user");
		                databaseHelper.register(user);
		                
		             // Navigate to the Welcome Login Page
		                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            		}
            		else {
            			errorLabel.setText("Please enter a valid invitation code");
            		}
            	}
            	else {
            		errorLabel.setText("This userName is taken!!.. Please use another to setup an account");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField, nameField, emailnameField, setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}