package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Displays the staff home page GUI as well as providing functionality for staff to
 * manage content based on validness/appropriateness such as reviews, questions, answers, and users.
 *
 * <p>This class is part of the final product of all of our team projects and allows the allocated staff members
 * to review all content (questions, answers, and reviews for both questions and answers), flag inappropriate material (per their discretion), and log out of the application.</p>
 *
 * <p>This class is part of the {@code application} package and relies on
 * {@link databasePart1.DatabaseHelper} for all database operations.</p>
 *
 * @author Zachary Papadatos
 * @version 1.0
 * @see databasePart1.DatabaseHelper
 */



public class StaffHomePage {
    private DatabaseHelper databaseHelper;

    /**
     * Constructs a new StaffHomePage instance with the specified database helper.
     *
     * @param db The {@link DatabaseHelper} used for database interactions.
     */
    
    // Constructor to initialize database helper
    public StaffHomePage(DatabaseHelper db) {
        this.databaseHelper = db;
    }

    /**
     * Displays the main interface for the staff user.
     *
     * @param primaryStage The primary {@link Stage} used to set the staff scene.
     */
    
    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Welcome Label
        Label staffLabel = new Label("Hello, Staff!");
        staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        layout.getChildren().add(staffLabel);

        // View Private Feedback Button
        Button viewPrivateFeedbackButton = new Button("View All Reviews");
        viewPrivateFeedbackButton.setOnAction(e -> {
            try {
            	List<Review> allReviews = databaseHelper.getReviews(); 
                StringBuilder feedback = new StringBuilder();
                for (Review r : databaseHelper.getReviews()) {
                    feedback.append("From: ").append(r.getUsername())
                            .append(" / question ID: ").append(r.getQuestionID())
                            .append(" / answer ID: ").append(r.getanswerID())
                            .append("\n")
                            .append(r.getReviewContent())
                            .append("\n\n");
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("The Review Feedback");
                alert.setHeaderText("All Reviews");
                alert.setContentText(feedback.length() > 0 ? feedback.toString() : "No  reviews found.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        layout.getChildren().add(viewPrivateFeedbackButton);
        Button viewAllQuestionsButton = new Button("View All Questions");
        viewAllQuestionsButton.setOnAction(e -> {
            try {
                List<Question> questions = databaseHelper.getAllQuestions();
                StringBuilder output = new StringBuilder();
                for (Question q : questions) {
                    output.append("question ID: ").append(q.getQuestionID())
                          .append(" / By: ").append(q.getUsername())
                          .append("\n")
                          .append(q.getQuestionText())
                          .append("\n\n");
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("All Questions");
                alert.setHeaderText("Submitted Questions");
                alert.setContentText(output.length() > 0 ? output.toString() : "No questions found.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        layout.getChildren().add(viewAllQuestionsButton);
        
        Button viewAllAnswersButton = new Button("View All Answers");

        viewAllAnswersButton.setOnAction(e -> {
            try {
                List<Answer> answers = databaseHelper.getAllAnswers();
                StringBuilder output = new StringBuilder();
                for (Answer a : answers) {
                    output.append("answer ID: ").append(a.getAnswerID())
                          .append(" / question ID: ").append(a.getQuestionID())
                          .append(" / By: ").append(a.getUserName())
                          .append("\n")
                          .append(a.getAnswerText())
                          .append("\n\n");
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("All Answers");
                alert.setHeaderText("Submitted Answers");
                alert.setContentText(output.length() > 0 ? output.toString() : "No answers found.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        layout.getChildren().add(viewAllAnswersButton);


        Button flagReviewButton = new Button("Flag a Review");
        flagReviewButton.setOnAction(e -> {
            try {
                List<Review> reviews = databaseHelper.getReviews();
                if (reviews.isEmpty()) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("No Reviews");
                    alert.setHeaderText(null);
                    alert.setContentText("There are no reviews available to flag.");
                    alert.showAndWait();
                    return;
                }

                Stage flagStage = new Stage();
                VBox box = new VBox(10);
                box.setStyle("-fx-padding: 10;");

                Label label = new Label("Select a review to flag:");
                ListView<Review> reviewListView = new ListView<>();
                reviewListView.getItems().addAll(reviews);

                reviewListView.setCellFactory(param -> new ListCell<Review>() {
                    @Override
                    protected void updateItem(Review review, boolean empty) {
                        super.updateItem(review, empty);
                        if (empty || review == null) {
                            setText(null);
                        } else {
                            setText("From: " + review.getUsername() +
                                    " / question ID: " + review.getQuestionID() +
                                    " / answer ID: " + review.getanswerID() +
                                    "\n" + review.getReviewContent());
                        }
                    }
                });

                Button flagButton = new Button("Flag Selected Review");
                flagButton.setOnAction(ev -> {
                    Review selected = reviewListView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        try {
                            databaseHelper.flagReview(selected.getReviewID());
                            flagStage.close();
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.setContentText("Review flagged successfully.");
                            alert.showAndWait();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please select a review to flag.");
                        alert.showAndWait();
                    }
                });

                box.getChildren().addAll(label, reviewListView, flagButton);
                flagStage.setScene(new Scene(box, 500, 300));
                flagStage.setTitle("Flag a Review");
                flagStage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        layout.getChildren().add(flagReviewButton);
        
        Button flagUserButton = new Button("Flag User Account");
        flagUserButton.setOnAction(e -> {
            try {
                List<String> allUsers = databaseHelper.getAllUsernames();  
                if (allUsers.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Users");
                    alert.setHeaderText(null);
                    alert.setContentText("There are no user accounts available to flag.");
                    alert.showAndWait();
                    return;
                }

                Stage flagStage = new Stage();
                VBox box = new VBox(10);
                box.setStyle("-fx-padding: 10;");

                Label label = new Label("Select a user account to flag:");
                ListView<String> userListView = new ListView<>();
                userListView.getItems().addAll(allUsers);

                userListView.setCellFactory(param -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String username, boolean empty) {
                        super.updateItem(username, empty);
                        setText((empty || username == null) ? null : "User: " + username);
                    }
                });

                Button flagButton = new Button("Flag Selected User");
                flagButton.setOnAction(ev -> {
                    String selected = userListView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        try {
                            databaseHelper.flagUserAccount(selected);
                            flagStage.close();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.setContentText("User account flagged successfully.");
                            alert.showAndWait();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please select a user to flag.");
                        alert.showAndWait();
                    }
                });

                box.getChildren().addAll(label, userListView, flagButton);
                flagStage.setScene(new Scene(box, 500, 300));
                flagStage.setTitle("Flag User Account");
                flagStage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        layout.getChildren().add(flagUserButton);


        

       
        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(a -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
        layout.getChildren().add(logoutButton);

        // Finalize scene
        Scene staffScene = new Scene(layout, 800, 400);
        primaryStage.setScene(staffScene);
        primaryStage.setTitle("Staff Page");
    }
}
