package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
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
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user=new User(userName, password, "", "", "");
            	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
            	ChangePasswordPage changePasswordPage = new ChangePasswordPage(databaseHelper);
            	AdminHomePage adminHomePage = new AdminHomePage(databaseHelper);
            	StudentHomePage studentHomePage = new StudentHomePage(databaseHelper); 
            	InstructorHomePage instructorHomePage = new InstructorHomePage(databaseHelper); 
            	StaffHomePage staffHomePage = new StaffHomePage(databaseHelper); 
            	ReviewerHomePage reviewerHomePage = new ReviewerHomePage(databaseHelper);
            	//add others

            	// Retrieve the user's role from the database using userName
            	String role = databaseHelper.getUserRole(userName);
            	
            	if(role!=null) {
            		user.setRole(role);
            		if(databaseHelper.login(user)) {
            			// when a user logs in, check if they have the isTempPass flag TRUE, if so, direct them to the changePasswordPage
            			if(databaseHelper.isPasswordTemporary(userName)) {
            				changePasswordPage.show(primaryStage,user);
            				return;
            			}
            			else if (user.getRoles().equals("admin")) {
                			adminHomePage.show(primaryStage);
            			}
            			else if (user.getRoles().equals("student")) {
                			studentHomePage.show(primaryStage);
            			}
            			else if (user.getRoles().equals("instructor")) {
                			instructorHomePage.show(primaryStage);
            			} 
            			else if (user.getRoles().equals("staff")) {
                			staffHomePage.show(primaryStage);
            			}
            			else if (user.getRoles().equals("reviewer")) {
                			reviewerHomePage.show(primaryStage);
            			}
            			else { // else, continue as normal
            			welcomeLoginPage.show(primaryStage,user);
            			}	
            			//ADAM
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
