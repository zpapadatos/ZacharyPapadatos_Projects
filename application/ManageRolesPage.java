package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ManageRolesPage {
    private DatabaseHelper databaseHelper;		//Helper class for database
    
    //Constructor for the database helper class
    public ManageRolesPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    //Method to display the role management GUI
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);		//vertical layout

        //Label and input for entering in a username
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        
        //Label and a list view for selecting which roles are to be given
        Label roleLabel = new Label("Select Role:");
        ListView<String> roleListView = new ListView<>();
        roleListView.getItems().addAll("admin", "student", "instructor", "staff", "reviewer");
        roleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Button to update the role(s) of a specific user
        Button updateButton = new Button("Update Role");
        updateButton.setOnAction(e -> {
            String username = usernameField.getText();
            ObservableList<String> selectedRoles = roleListView.getSelectionModel().getSelectedItems();
            try {
            	//Calls method updateRole with the username and selected role(s) as inputs
                updateRole(username, selectedRoles);
            } catch (SQLException ex) {
                ex.printStackTrace();		//prints an error if the update fails
            }
        });

        //Adding all the components to the layout
        layout.getChildren().addAll(usernameLabel, usernameField, roleLabel, roleListView, updateButton);
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Roles");
    }

    //Method for updating the role(s) of a user inside the SQL database
    public void updateRole(String username, ObservableList<String> newRoles) throws SQLException {
    	//Check to make sure there is a username and role(s) as an input
        if (newRoles == null || username.isEmpty()) {
            showAlert("Error", "Username or role cannot be empty.");
            return;
        }

        //Prevents the admin from removing their own role as admin
        if (newRoles.contains("admin") && isAdmin(username)) {
            showAlert("Error", "You cannot remove the admin role from yourself.");
            return;
        }

        // Update the user's role(s) inside the database
        if (databaseHelper.updateUserRole(username, newRoles)) {
            showAlert("Success", "User role updated successfully.");
        } else {
            showAlert("Error", "Failed to update role. User may not exist.");
        }
    }

    //Method to check if the given username belongs to an admin
    public boolean isAdmin(String username) throws SQLException {
        ResultSet rs = databaseHelper.getUserByUsername(username);
        if (rs != null && rs.next()) {
            return "admin".equals(rs.getString("role"));
        }
        return false;
    }

    //Method that displays a pop out alert for success or error messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}