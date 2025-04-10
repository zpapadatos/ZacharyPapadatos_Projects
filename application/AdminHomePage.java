package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminHomePage {
    private final DatabaseHelper databaseHelper;
	private boolean confirmDeletionOfUser = false; // tracks if admin has pressed delete twice to confirm
	private String mostRecentUserName = ""; // tracks if the second click's username matches the first click's username

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

public void show(Stage primaryStage) {
    VBox layout = new VBox();
    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
    // label to display the welcome message for the admin
    Label adminLabel = new Label("Hello, Admin!");
    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
    TextField userNameField = new TextField();
    userNameField.setPromptText("Enter username");
    userNameField.setMaxWidth(100);
    
    Label errorLabel = new Label();
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
    
    // "Invite" button for admin to generate invitation codes
    
    
    Button inviteButton = new Button("Invite");
        inviteButton.setOnAction(a -> {
            new InvitationPage().show(databaseHelper, primaryStage);
        });
        layout.getChildren().add(inviteButton);
   
        
    Button viewUsersButton = new Button("View Users");
    viewUsersButton.setOnAction(e -> {
            try {
            UserListPage userListPage = new UserListPage(databaseHelper);
            userListPage.show(primaryStage);} catch (Exception ex) {
            ex.printStackTrace();}});//Button to navigate to the ManageRolesPage
    
    Button manageRolesButton = new Button("Manage Roles");
    manageRolesButton.setOnAction(e -> {
        ManageRolesPage manageRolesPage = new ManageRolesPage(databaseHelper);
        manageRolesPage.show(primaryStage);});
    //Adds components to the layout
    
    Button removeUserButton = new Button("Remove User");
    removeUserButton.setOnAction(a -> {
    	String userName = userNameField.getText();
    	try {
    		if (userName.isEmpty()) {
    			errorLabel.setText("Cannot Delete Empty User");
    			mostRecentUserName = "";
    			return;
    		}
    		if (!databaseHelper.doesUserExist(userName)) {
    			errorLabel.setText("User doesn't exist.");
    			mostRecentUserName = "";
    			return;
    		}
    		String role = databaseHelper.getUserRole(userName);
    		if (role != null && role.equalsIgnoreCase("admin")) {
    			errorLabel.setText("Cannot Delete Admin");
    			mostRecentUserName = "";
    			return;
    		}
    		if (!confirmDeletionOfUser || !userName.equals(mostRecentUserName)) {
    			confirmDeletionOfUser = true;
    			mostRecentUserName = userName;
                errorLabel.setText("Are you sure? Click again to confirm user removal.");
    		}
    		else {
    			databaseHelper.removeUser(userName);
    			errorLabel.setText("Successfully removed " + userName + ".");
    			confirmDeletionOfUser = false;
    			mostRecentUserName = "";
    			
    		}
    	}
    	catch (Exception e) {
    		errorLabel.setText("Error:" + e.getMessage());
    	}
    });
    
    Button generateTempPassButton = new Button("Generate Temporary Password For User");
    generateTempPassButton.setOnAction(a -> {
    	String userName = userNameField.getText();
    	try {
    		if (userName.isEmpty()) {
    			errorLabel.setText("Cannot Delete Empty User");
    			return;
    		}
    		if (!databaseHelper.doesUserExist(userName)) {
    			errorLabel.setText("User doesn't exist.");
    			return;
    		}
    		else {
    			String tempPassword = databaseHelper.createTemporaryPassword(userName);
    			errorLabel.setText(tempPassword);
    		}
    	}
    	catch (Exception e) {
    		errorLabel.setText("Error:" + e.getMessage());
    	}
    });
    
    // Logout Button for users
    Button logoutButton = new Button("Logout");
    
    
    // logging out will return user to login selection screen
    logoutButton.setOnAction(a -> {
    	databaseHelper.closeConnection();
        new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
    });
    layout.getChildren().add(logoutButton); 
    
    layout.getChildren().addAll(adminLabel, userNameField, removeUserButton, generateTempPassButton, viewUsersButton, manageRolesButton, errorLabel);
    Scene adminScene = new Scene(layout, 800, 400);

        // Set the scene to primary stage
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
    }
}