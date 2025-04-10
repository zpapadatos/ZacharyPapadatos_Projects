package application;

import javafx.scene.Scene;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {
	
	private final DatabaseHelper databaseHelper;
	private int numOfRoles;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    public void show( Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Button to navigate to the user's respective page based on their role
	    
	    Button continueButton = new Button("Admin");
	    continueButton.setOnAction(a -> {
	    	String role = "admin";	    	
	    	
	    	//take user to their specific role's Home page
	    	if(role.equals("admin")) {
	    		new AdminHomePage(databaseHelper).show(primaryStage);
	    	}
	    	if(role.equals("student")) {
	    		new StudentHomePage(databaseHelper).show(primaryStage);
	    	}
	    	if(role.equals("staff")) {
	    		new StaffHomePage(databaseHelper).show(primaryStage);
	    	}
	    	if(role.equals("instructor")) {
	    		new InstructorHomePage(databaseHelper).show(primaryStage);
	    	}
	    	if(role.equals("reviewer")) {
	    		new ReviewerHomePage(databaseHelper).show(primaryStage);
	    	}
	    	else if(role.equals("user")) {
	    		new UserHomePage().show(primaryStage);
	    	}
	    });
    
	    List <String> rolesList = Arrays.asList(user.getRoles().split(","));
	    System.out.println("ROLES:" + user.getRoles());
	    this.numOfRoles = rolesList.size();
	    System.out.println("NUM: " + numOfRoles);
	    
	    
	 // This welcome page message will only display for users with multiple roles

	    if (numOfRoles > 1) {
	    Label MultUserLabel = new Label("Welcome! Which of your roles would you like to play?");
	    MultUserLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    layout.getChildren().add(MultUserLabel);
	    }
	        
	    //specific role buttons to display to user based on the roles they have
        Button studentButton = new Button("Student");
        Button instructorButton = new Button("Instructor");
        Button reviewerButton = new Button("Reviewer");
        Button staffButton = new Button("Staff");
  	
	    	//if the user has more than one role, multiple buttons will display
        	//with each of their rules to choose from
        
	    		if (rolesList.contains("student")) {
	    		studentButton.setOnAction(a -> {
                new StudentHomePage(databaseHelper).show(primaryStage);
            });
	    		layout.getChildren().add(studentButton);
	    	}       
	    		if (rolesList.contains("instructor")) {
	    		instructorButton.setOnAction(a -> {
                new InstructorHomePage(databaseHelper).show(primaryStage);
	    	});
	    		layout.getChildren().add(instructorButton);
	    	}
	    		if (rolesList.contains("reviewer")) {
	    		reviewerButton.setOnAction(a -> {
                new ReviewerHomePage(databaseHelper).show(primaryStage);
            });
	    		layout.getChildren().add(reviewerButton);
	    	}           
	    		if (rolesList.contains("staff")) {
	    		staffButton.setOnAction(a -> {
                new StaffHomePage(databaseHelper).show(primaryStage);
            });
	    		layout.getChildren().add(staffButton);
	    	}     
	    		
       // A user with no roles will receive a no roles message 
	    if (rolesList.contains("user")) {
	    	Label UserLabel = new Label("Welcome, User! Ask admin to give you a role!");
	 	    UserLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	 	    
	    	layout.getChildren().add(UserLabel);
	    }
	    	
      // Button to quit the application
 	    Button quitButton = new Button("Quit");
 	    quitButton.setOnAction(a -> {
 	    	databaseHelper.closeConnection();
 	    	Platform.exit(); // Exit the JavaFX application
 	    });

 	    
	    layout.getChildren().addAll(quitButton);
	    Scene welcomeScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(welcomeScene);
	    primaryStage.setTitle("Welcome Page");
    }
}