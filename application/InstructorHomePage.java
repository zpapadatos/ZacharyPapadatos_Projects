//instructor home page
package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import databasePart1.DatabaseHelper;

public class InstructorHomePage {
    private DatabaseHelper databaseHelper;
    private ListView<String> studentListView = new ListView<>();
    private ListView<String> questionsAndAnswersView = new ListView<>();
    private Button acceptButton = new Button("Accept");
    private Button denyButton = new Button("Deny");

    public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        List<String> studentUsernames = databaseHelper.getAllStudentUsernames(); // Fetch from DB
        studentListView.getItems().setAll(studentUsernames);

        // Set up button actions
        acceptButton.setOnAction(event -> {
            try {
                handleAccept();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        denyButton.setOnAction(event -> {
            try {
                handleDeny();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Event to load questions and answers on student selection
        studentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    displayQuestionsAndAnswers(newValue);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        // Layout: VBox for list and Label, HBox for buttons, and a separate VBox for questions and answers
        HBox buttonLayout = new HBox(10, acceptButton, denyButton);
        VBox layout = new VBox(10, new Label("Students who want Reviewer role: "), studentListView, buttonLayout);

        VBox rightSideLayout = new VBox(10, new Label("Questions and Answers:"), questionsAndAnswersView);
        HBox mainLayout = new HBox(20, layout, rightSideLayout);

        Scene scene = new Scene(mainLayout, 1000, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Instructor Home Page");
        primaryStage.show();
    }

    private void handleAccept() throws SQLException {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Add the reviewer role to the student
            databaseHelper.addReviewerRole(selectedStudent);
            // Remove the student from the list view and database
            studentListView.getItems().remove(selectedStudent);
            databaseHelper.removeStudentFromRequestList(selectedStudent);
        }
    }

    private void handleDeny() throws SQLException {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Just remove the student from the list view and database
            studentListView.getItems().remove(selectedStudent);
            databaseHelper.removeStudentFromRequestList(selectedStudent);
        }
    }

    private void displayQuestionsAndAnswers(String studentUsername) throws Exception {
        ArrayList<Question> allQuestions = databaseHelper.getAllQuestions();
        ArrayList<Answer> allAnswers = databaseHelper.getAllAnswers();

        List<String> studentQuestionsAndAnswers = new ArrayList<>();

        // Filter questions and answers by the selected student's username
        for (Question question : allQuestions) {
            if (question.getUsername().equals(studentUsername)) {
                // Add the question to the list
                studentQuestionsAndAnswers.add("Q: " + question.getQuestionText());
            }
        }
                
                // Find and add corresponding answers
                for (Answer answer : allAnswers) {
                    if (answer.getUserName().equals(studentUsername)) {
                        studentQuestionsAndAnswers.add("A: " + answer.getAnswerText());
                    }
                }

        // Display the list of questions and answers in the right-side ListView
        questionsAndAnswersView.getItems().setAll(studentQuestionsAndAnswers);
    }
}