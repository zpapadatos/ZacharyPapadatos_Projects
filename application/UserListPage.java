package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserListPage {
    private DatabaseHelper databaseHelper;

    public UserListPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;        //Constructor of database helper instance to fetch user data
    }

    @SuppressWarnings("unchecked")
    public void show(Stage primaryStage) {

        //Creates a table for the user data
        TableView<User> table = new TableView<>();

        //Each column is defined by user attributes
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        table.getColumns().addAll(usernameColumn, nameColumn, emailColumn, roleColumn);

        //Ensure the table gets updated on the JavaFX application thread
        Platform.runLater(() -> {
            ObservableList<User> userList = getUserList();
            System.out.println("User List Size (on UI thread): " + userList.size());  //Debugging
            table.setItems(userList);  //Bind the ObservableList to the table
        });

        VBox layout = new VBox(table);
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("User List");
    }

    //Method for retrieving a list of users from the database
    private ObservableList<User> getUserList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            ResultSet rs = databaseHelper.getAllUsers();        //Gets all users from database
            if (rs != null) {
                //Creates a new User Object and adds it to a list
                while (rs.next()) {
                    users.add(new User(rs.getString("userName"), rs.getString("password"), rs.getString("name"), rs.getString("email"), rs.getString("role")));
            }
          }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;        //Returns the list of all users
    }
}