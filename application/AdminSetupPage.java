package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

//name and email validation 
/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        //user can enter their name 
        TextField nameField = new TextField();
        nameField.setPromptText("Users First / Last name"); 
        nameField.setMaxWidth(250); 
        
        //email
        TextField emailnameField = new TextField();
        emailnameField.setPromptText("Enter Email"); 
        emailnameField.setMaxWidth(250);

        Button setupButton = new Button("Setup");
        
        //Created an error label in red to show error messages
        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");
        error.setVisible(false);
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String email = emailnameField.getText(); //added email
            String name = nameField.getText(); //added password
            //Validate Username and password
            String userValidation = UserNameRecognizer.checkForValidUserName(userName);
            String passwordValidation = PasswordEvaluator.evaluatePassword(password);
            String nameValidation = nameRecognizer.EvaluateName(name); 
            String emailValidation = EmailRecognizer.checkForValidEmail(email); 
            
            
            
            
            //Will output an error line if the username and password are invalid
            if (!userValidation.isEmpty()) {
            	error.setText("Invalid Username: " + userValidation);
            	error.setVisible(true);
            } else if (!passwordValidation.isEmpty()) {
            	error.setText("Invalid Password: " + passwordValidation);
            	error.setVisible(true);
            }else if (!nameValidation.isEmpty()) {
            	error.setText("Invalid Name: " + nameValidation);
            	error.setVisible(true);
            } else if (!emailValidation.isEmpty()) {
            	error.setText("Invalid Email: " + emailValidation);
            	error.setVisible(true);
            }
            else {		//If it is valid, then will continue to the next process
                try {
                    // Create a new User object with admin role and register in the database
                    User user=new User(userName, password, email, name, "admin"); // add email and name 
                    databaseHelper.register(user);
                    System.out.println("Administrator setup completed.");

                    error.setText(""); 

                    // Navigate to the Welcome Login Page
                    new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
                } catch (SQLException e) {
                    System.err.println("Database error: " + e.getMessage());
                    e.printStackTrace();
                }
            }});
        
        //Added an error box to the layout to show the error
        VBox layout = new VBox(10, userNameField, passwordField, nameField, emailnameField, setupButton, error);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}