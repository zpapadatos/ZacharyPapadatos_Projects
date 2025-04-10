package application;

import java.util.ArrayList;
import java.util.List;
import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerHomePage {
    private DatabaseHelper databaseHelper;
    private QuestionList questionList;
    private AnswerList answerList;
    private Message messageList;  

    public ReviewerHomePage(DatabaseHelper databaseHelper) {
        try {
            this.databaseHelper = databaseHelper;
            questionList = new QuestionList(databaseHelper);
            answerList = new AnswerList(databaseHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ListView<Question> questionListView = new ListView<>();
    private ListView<Answer> answerListView = new ListView<>();
    private ListView<Review> reviewListView = new ListView<>();
    private ListView<Message> messageListView = new ListView();
    private TextField messageField = new TextField();
    private TextField searchField = new TextField();   
    private TextField reviewInput = new TextField();
    private Button searchButton = new Button("Search Question");
    private Button addReviewButton = new Button("Add Review");
    private Button deleteReviewButton = new Button("Delete Review");
    private Button updateReviewButton = new Button("Update Review");
    private Button quitButton = new Button("Quit");
    private Button replyButton = new Button("Reply");
    


    public void show(Stage primaryStage) {
        VBox questionBox = new VBox(4, new Label("Questions"), questionListView);
        VBox answerBox = new VBox(5, new Label("Answers"), answerListView);
        VBox reviewBox = new VBox(6, new Label("Reviews"), reviewListView, reviewInput, addReviewButton, deleteReviewButton, updateReviewButton,quitButton);
        HBox searchBox = new HBox(4, new Label("Search:"), searchField, searchButton);
        VBox messageBox = new VBox(3, new Label("Messages: "), messageListView,replyButton); 
        VBox mainLayout = new VBox(10, searchBox,new HBox(10, questionBox, answerBox,reviewBox,messageBox)); 
        Scene scene = new Scene(mainLayout, 900, 400);
        setupActions();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reviewer Home Page");
        primaryStage.show();
        refreshQuestions();
    }

    private void setupActions() {
    	questionListView.setOnMouseClicked(e -> {
    	    Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
    	    if (selectedQuestion != null) {
    	         answerListView.getSelectionModel().clearSelection();
    	         refreshReviewsForQuestion(selectedQuestion);
    	    }
    	});
    	questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    	    answerListView.getSelectionModel().clearSelection();
    	    refreshAnswers(newVal);
    	    if (newVal != null) {
    	        refreshReviewsForQuestion(newVal);
    	    } else {
    	        reviewListView.getItems().clear();
    	    }
    	});

        answerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshReviewsForAnswer(newVal);
            } else {
                Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    refreshReviewsForQuestion(selectedQuestion);
                } else {
                    reviewListView.getItems().clear();
                }
            }
        });
        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText().trim();
            if (searchQuery.isEmpty()) {
                refreshQuestions();
                return;
            }
            try {
                ArrayList<Question> searchResults = new ArrayList<>();
                try {
                    int id = Integer.parseInt(searchQuery);
                    Question result = questionList.searchQuestionById(id);
                    if (result != null) {
                        searchResults.add(result);
                    }
                } catch (NumberFormatException ex) {
                    searchResults = questionList.searchQuestionsByKeyword(searchQuery);
                }
                questionListView.getItems().setAll(searchResults);
            } catch (Exception ex) {
                showAlert("Search Error", "An error occurred while searching for questions.");
                ex.printStackTrace();
            }
        });
        addReviewButton.setOnAction(e -> {
            String text = reviewInput.getText().trim();
            String username = databaseHelper.getCurrentUserFromDatabase(); 
            if (text.isEmpty()) {
                showAlert("Input Error", "Please enter a review.");
                return;
            }
            try {
                Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
                Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                Review review;
                if (selectedAnswer != null) {
                    review = new Review(0, 0, selectedAnswer.getAnswerID(), text, username, "defaultReviewType");
                    databaseHelper.addReview(review);
                    refreshReviewsForAnswer(selectedAnswer);
                } else if (selectedQuestion != null) {
                    review = new Review(0, selectedQuestion.getQuestionID(), 0, text, username, "defaultReviewType");
                    databaseHelper.addReview(review);
                    refreshReviewsForQuestion(selectedQuestion);
                } else {
                    showAlert("Selection Error", "Please select a question or answer to review.");
                    return;
                }
                reviewInput.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add review.");
            }
        });
        deleteReviewButton.setOnAction(e -> {
            Review selectedReview = reviewListView.getSelectionModel().getSelectedItem();
            if (selectedReview == null) {
                showAlert("Selection Error", "Select a review to delete.");
                return;
            }
            try {
                databaseHelper.deleteReview(selectedReview.getReviewID());
                Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
                Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                if (selectedAnswer != null) {
                    refreshReviewsForAnswer(selectedAnswer);
                } else if (selectedQuestion != null) {
                    refreshReviewsForQuestion(selectedQuestion);
                } else {
                    reviewListView.getItems().clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to delete review.");
            }
        });
        
        updateReviewButton.setOnAction(e -> {
            Review selectedReview = reviewListView.getSelectionModel().getSelectedItem();
            if (selectedReview == null) {
                showAlert("Selection Error", "Select a review to update.");
                return;
            }
            String newText = reviewInput.getText().trim();
            if (newText.isEmpty()) {
                showAlert("Error", "Enter new review text.");
                return;
            }
            try {
                databaseHelper.updateReview(selectedReview.getReviewID(), newText);
                // Refresh reviews based on selection.
                Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
                Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                if (selectedAnswer != null) {
                    refreshReviewsForAnswer(selectedAnswer);
                } else if (selectedQuestion != null) {
                    refreshReviewsForQuestion(selectedQuestion);
                } else {
                    reviewListView.getItems().clear();
                }
                reviewInput.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to update review.");
            }
        });
        replyButton.setOnAction(e -> {
            Message selectedmessage = messageListView.getSelectionModel().getSelectedItem();
            if (selectedmessage != null) {
            	Stage messageStage = new Stage();
                VBox messageLayout = new VBox(10);
                TextField messageField = new TextField();
                messageField.setPromptText("Enter your message to the student...");
                Button sendButton = new Button("Send");

                sendButton.setOnAction(ev -> {  
                    String messageText = messageField.getText().trim();
                    String sender = databaseHelper.getCurrentUserFromDatabase();  
                    String receiver = selectedmessage.getSender();
                    int reviewID = selectedmessage.getReviewID();
                    
                    if (!messageText.isEmpty()) {
                        try {
                            databaseHelper.insertMessage(reviewID, sender, receiver, messageText);
                            messageStage.close();
                            showAlert("Success", "Message sent successfully!");
                        } catch (Exception ex) { 
                            ex.printStackTrace();
                            showAlert("Error", "Failed to send message.");
                        }
                    } else {
                        showAlert("Input Error", "Please enter a message before sending.");
                    }
                });

                messageLayout.getChildren().addAll(new Label("Send a reply:"), messageField, sendButton);
                Scene messageScene = new Scene(messageLayout, 300, 150);
                messageStage.setScene(messageScene);
                messageStage.setTitle("Message");
                messageStage.show();
            } else {
                showAlert("Selection Error", "Please select an answer to message the reviewer.");
            }
        });

     // Button to quit the application
       
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
           
        });

    }

    private void refreshQuestions() {
        try {
            questionListView.getItems().setAll(databaseHelper.getAllQuestions());
            questionListView.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question question, boolean empty) {
                    super.updateItem(question, empty);
                    setText(empty || question == null ? null : question.getQuestionText() + " [" + question.getStatus() + "]");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAnswers(Question question) {
        try {
            if (question != null) {
                List<Answer> answers = answerList.getAnswersForQuestion(question.getQuestionID());
                answerListView.getItems().setAll(answers);
                answerListView.setCellFactory(param -> new ListCell<Answer>() {
                    @Override
                    protected void updateItem(Answer answer, boolean empty) {
                        super.updateItem(answer, empty);
                        setText(empty || answer == null ? null : answer.getAnswerText() + " [" + answer.getStatus() + "]");
                    }
                });
            } else {
                answerListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshReviewsForAnswer(Answer answer) {
        try {
            if (answer != null) {
                List<Review> reviews = databaseHelper.getReviewsForAnswer(answer.getAnswerID());
                reviewListView.getItems().setAll(reviews);
                reviewListView.setCellFactory(param -> new ListCell<Review>() {
                    @Override
                    protected void updateItem(Review review, boolean empty) {
                        super.updateItem(review, empty);
                        setText(empty || review == null ? null : review.getUsername() + ": " + review.getReviewContent());
                    }
                });
            } else {
                reviewListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshReviewsForQuestion(Question question) {
        try {
            if (question != null) {
                List<Review> reviews = databaseHelper.getReviewsForQuestion(question.getQuestionID());
                reviewListView.getItems().setAll(reviews);
                reviewListView.setCellFactory(param -> new ListCell<Review>() {
                    @Override
                    protected void updateItem(Review review, boolean empty) {
                        super.updateItem(review, empty);
                        setText(empty || review == null ? null : review.getUsername() + ": " + review.getReviewContent());
                    }
                });
            } else {
                reviewListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    
    
    reviewListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            List<Message> messages = databaseHelper.getMessagesForReview(newVal.getReviewID());
            messageListView.setItems(FXCollections.observableArrayList(messages));
        }
        
    });
      messageListView.setCellFactory(param -> new ListCell<Message>() {
        protected void updateItem(Message msg, boolean empty) {
            super.updateItem(msg, empty);
            if (empty || msg == null) {
                setText(null);
            } else {
                setText(msg.getSender() + " to " + msg.getReceiver() + ": " + msg.getContent());
            }
        }
    });
    
  
   
    
}


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}