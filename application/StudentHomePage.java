//student home page 
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Callback;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentHomePage {
    private DatabaseHelper databaseHelper;
    private QuestionList questionList;
    private AnswerList answerList;
    private Message messageList; 
    private TrustedReviewer trustedReviewer;
    @SuppressWarnings("unused")
    private ListCell<Question> dummyCell;

    public StudentHomePage(DatabaseHelper databaseHelper) {
        try {
            this.databaseHelper = databaseHelper;
            questionList = new QuestionList(databaseHelper);
            answerList = new AnswerList(databaseHelper);
            trustedReviewer = new TrustedReviewer(databaseHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UI Components
    private ListView<TrustedReviewerEntry> reviewerListView = new ListView<>();
    private ListView<String> reviewersListView = new ListView<>();
    private TextField reviewerName = new TextField(); 
    private TextField reviewerScore = new TextField();
    private ListView<Question> questionListView = new ListView<>();
    private ListView<Answer> answerListView = new ListView<>();
    private TextField questionInput = new TextField();
    private TextField answerInput = new TextField();
    private TextField searchField = new TextField();
    private ListView<Review> reviewListView = new ListView<>();
    private ListView<Message> messageListView = new ListView();
    private TextField messageField = new TextField();
    {
        reviewerName.setPromptText("Reviewer username");
        reviewerScore.setPromptText("Score (1–10)");
    }
    

    // Buttons for actions
    private Button addQuestionButton = new Button("Add Question");
    private Button updateQuestionButton = new Button("Update Question");
    private Button addAnswerButton = new Button("Add Answer");
    private Button updateAnswerButton = new Button("Update Answer");
    private Button updateStatusButton = new Button("Update Status");
    private Button deleteQuestionButton = new Button("Delete Question");
    private Button deleteAnswerButton = new Button("Delete Answer");
    private Button searchButton = new Button("Search Question");
    private Button privateMessage = new Button("Private Message");
    private Button applicationButton = new Button("Apply for Reviewer");
    private Button quitButton = new Button("Quit");
    private Button replyButton = new Button("Reply");
    private Button deleteReviewer = new Button("Delete Reviewer"); 
    private Button addReviewer = new Button("Add Reviewer");
    private Button addToTrustedReviewersButton = new Button("Add To Trusted Reviewers");
    

 
    public void show(Stage primaryStage) {
        // Layout for questions
        VBox questionBox = new VBox(4, new Label("Questions"), questionListView, questionInput, addQuestionButton, updateQuestionButton, deleteQuestionButton);

        // Layout for answers
        VBox answerBox = new VBox(5, new Label("Answers"), answerListView, answerInput, addAnswerButton, updateAnswerButton, updateStatusButton, deleteAnswerButton);
        VBox reviewBox = new VBox (5, new Label("Reviews"), reviewListView, privateMessage);
        VBox messageBox = new VBox(3, new Label("Messages: "), messageListView,replyButton);
        VBox reviewerBox = new VBox(4, new Label("Reviewers"), reviewerListView, reviewerName, reviewerScore, addReviewer, deleteReviewer, addToTrustedReviewersButton);
        VBox trustedBox = new VBox(4, new Label("Trusted Reviewers"), reviewersListView);


        // Main layout containing both sections
        HBox searchBox = new HBox(5, new Label("Search:"), searchField, searchButton, applicationButton,quitButton);
        VBox mainLayout = new VBox(10, searchBox, new HBox(10, questionBox, answerBox,reviewBox,reviewerBox,trustedBox,messageBox));

        Scene scene = new Scene(mainLayout, 700, 400); 

        setupActions();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Home Page");
        primaryStage.show();

        refreshQuestions();
        refreshReviewers();
    }

    private void setupActions() {
        answerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshReviews(newVal);
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

        addQuestionButton.setOnAction(e -> {
            String text = questionInput.getText().trim();
            String name = databaseHelper.getCurrentUserFromDatabase();
            try {
                Question question = new Question(0, name, text, "Unresolved");
                questionList.addQuestion(question);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            refreshQuestions();
            questionInput.clear();
        });

        updateQuestionButton.setOnAction(e -> {
            Question selected = questionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String text = questionInput.getText().trim();
                if (text.isEmpty()) {
                    showAlert("Input Error", "Please enter a question.");
                } else {
                    try {
                        questionList.updateQuestion(selected.getQuestionID(), text, selected.getUsername());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    refreshQuestions();
                    questionInput.clear();
                }
            } else {
                showAlert("Selection Error", "Please select a question to update.");
            }
        });

        deleteQuestionButton.setOnAction(e -> {
            Question selected = questionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getUsername().equals(databaseHelper.getCurrentUserFromDatabase())) {
                    try {
                        questionList.deleteQuestion(selected.getQuestionID(), databaseHelper.getCurrentUserFromDatabase());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    refreshQuestions();
                    answerListView.getItems().clear();
                }
            } else {
                showAlert("Selection Error", "Please select a question to delete.");
            }
        });

        addAnswerButton.setOnAction(e -> {
            Question selected = questionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String text = answerInput.getText().trim();
                String name = databaseHelper.getCurrentUserFromDatabase();
                if (text.isEmpty()) {
                    showAlert("Input Error", "Please enter an answer.");
                } else {
                    Answer answer = new Answer(0, name, selected.getQuestionID(), text, "Unresolved");
                    try {
                        answerList.addAnswer(answer);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    refreshAnswers(selected);
                    answerInput.clear();
                }
            } else {
                showAlert("Selection Error", "Please select a question to add an answer to.");
            }
        });

        updateAnswerButton.setOnAction(e -> {
            Answer selected = answerListView.getSelectionModel().getSelectedItem();
            Question q = questionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String text = answerInput.getText().trim();
                if (text.isEmpty()) {
                    showAlert("Input Error", "Please enter an answer.");
                } else {
                    try {
                        answerList.updateAnswer(selected.getAnswerID(), text, selected.getUserName());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    refreshAnswers(q);
                    answerInput.clear();
                }
            } else {
                showAlert("Selection Error", "Please select an answer to update.");
            }
        });

        updateStatusButton.setOnAction(e -> {
            Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            Answer selectedAnswer = answerListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null && selectedAnswer != null) {
                try {
                    String currentUser = databaseHelper.getCurrentUserFromDatabase();
                    if (selectedQuestion.getUsername().equals(currentUser)) {
                        databaseHelper.updateQuestionStatus(selectedQuestion.getQuestionID(), "Resolved");
                        databaseHelper.updateAnswerStatus(selectedAnswer.getAnswerID(), "Resolved");
                        refreshQuestions();
                        refreshAnswers(selectedQuestion);
                        showAlert("Success", "Status updated successfully for both question and answer.");
                    } else {
                        showAlert("Permission Denied", "Only the creator of the question can update the status.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Update Error", "Failed to update the status.");
                }
            } else {
                showAlert("Selection Error", "Please select both a question and an answer to update the status.");
            }
        });

        deleteAnswerButton.setOnAction(e -> {
            Question selected = questionListView.getSelectionModel().getSelectedItem();
            Answer answerSelected = answerListView.getSelectionModel().getSelectedItem();
            if (selected != null && answerSelected != null) {
                try {
                    answerList.deleteAnswer(answerSelected.getAnswerID(), databaseHelper.getCurrentUserFromDatabase());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                refreshAnswers(selected);
            } else {
                showAlert("Selection Error", "Please select an answer to delete.");
            }
        });

        privateMessage.setOnAction(e -> {
            Review selectedreview = reviewListView.getSelectionModel().getSelectedItem();
            if (selectedreview != null) {
                Stage messageStage = new Stage();
                VBox messageLayout = new VBox(10);
                TextField messageField = new TextField();
                messageField.setPromptText("Enter your message to the reviewer...");
                Button sendButton = new Button("Send");

                sendButton.setOnAction(ev -> {  
                    String messageText = messageField.getText().trim();
                    String student = databaseHelper.getCurrentUserFromDatabase();  
                    String reviewer = selectedreview.getUsername();
                    int reviewID = selectedreview.getReviewID();
                   
                    if (!messageText.isEmpty()) {
                        try {
                            databaseHelper.insertMessage(reviewID, student, reviewer, messageText);
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

                messageLayout.getChildren().addAll(new Label("Send a Private Message:"), messageField, sendButton);
                Scene messageScene = new Scene(messageLayout, 300, 150);
                messageStage.setScene(messageScene);
                messageStage.setTitle("Private Message");
                messageStage.show();
            } else {
                showAlert("Selection Error", "Please select an answer to message the reviewer.");
            }
        });
        replyButton.setOnAction(e -> {
            Message selectedmessage = messageListView.getSelectionModel().getSelectedItem();
            if (selectedmessage != null) {
            	Stage messageStage = new Stage();
                VBox messageLayout = new VBox(10);
                TextField messageField = new TextField();
                messageField.setPromptText("Enter your message to the reviewer...");
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
        
        

        questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshAnswers(newVal);
            }
        });
        reviewListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshMessages(newVal);  
            }
        });
        //new for the TP3 (add the reviewer and the score to the table) 
        addReviewer.setOnAction(e -> {
            //gets members from the database
             String student = databaseHelper.getCurrentUserFromDatabase();
                String reviewer = reviewerName.getText().trim();
                String scoreText = reviewerScore.getText().trim();

                //validates that the inputs are not empty before entry 
                if (reviewer.isEmpty()||scoreText.isEmpty()) {
                    showAlert("Input Error", "Please enter both reviewer name and score.");
                    return;
                }

                //parse the score and if not inbetween the correct values throws an error 
                try {
                    int score = Integer.parseInt(scoreText);
                    if (score < 1||score > 10) {
                        showAlert("Input Error", "Score must be between 1 and 10.");
                        return;
                    }

                    //add tp the database and refresh so the next class can be entered 
                    trustedReviewer.addReviewer(student, reviewer, score);
                    refreshReviewers();
                    reviewerName.clear();
                    reviewerScore.clear();

                  //error handling 
                } catch (NumberFormatException ex) {
                    showAlert("Input Error", "Score must be a valid number.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Database Error", "Failed to add reviewer.");
                }

        });
        
         //gives the delete reviewer button functionality 
        deleteReviewer.setOnAction(e -> { 
            //get the current student and reviewer(only logged in) 
            String student = databaseHelper.getCurrentUserFromDatabase();
               String reviewer = reviewerName.getText().trim();

               //makes sure that the entry is not empty when button is pressed 
               if (reviewer.isEmpty()) {
                   showAlert("Input Error", "Please enter the reviewer's name to delete.");
                   return;
               }

               //removes the reviewer
               try {
                   trustedReviewer.deleteReviewer(student, reviewer);
                   refreshReviewers();
                   reviewerName.clear();
                   reviewerScore.clear();
               //error handling 
               } catch (Exception ex) {
                   ex.printStackTrace();
                   showAlert("Database Error", "Failed to delete reviewer.");
               }	
        });
        
        addToTrustedReviewersButton.setOnAction(e -> {
            TrustedReviewerEntry selected = reviewerListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Selection Error", "Please select a reviewer.");
                return;
            }
            String displayText = selected.getName() + " (Score: " + selected.getScore() + ")";
            reviewersListView.getItems().add(displayText);
        });

        applicationButton.setOnAction(e -> {
            String currentUser = databaseHelper.getCurrentUserFromDatabase();
            databaseHelper.saveStudentUsername(currentUser);
        });
             quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
        });
    }
    
    
    private void refreshQuestions() {
        try {
            questionListView.getItems().setAll(databaseHelper.getAllQuestions());
            questionListView.setCellFactory(param -> new javafx.scene.control.ListCell<Question>() {
                @Override
                protected void updateItem(Question question, boolean empty) {
                    super.updateItem(question, empty);
                    if (empty || question == null) {
                        setText(null);
                    } else {
                        setText(question.getQuestionText() + "[" + question.getStatus() + "]");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshMessages(Review review) { 
        try {
            if (review != null) {
                List<Message> messages = databaseHelper.getMessagesForReview(review.getReviewID());
                messageListView.getItems().setAll(messages);
                messageListView.setCellFactory(param -> new ListCell<Message>() {
                    @Override
                    protected void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (empty || message == null) {
                            setText(null);
                        } else {
                            setText(message.getSender() + ": " + message.getContent());
                        }
                    }
                });
            } else {
                messageListView.getItems().clear();
            }
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
                        if (empty || answer == null) {
                            setText(null);
                        } else {
                            setText(answer.getAnswerText() + "[" + answer.getStatus() + "]");
                        }
                    }
                });
            } else {
                answerListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshReviews(Answer answer) {
        try {
            if (answer != null) {
                List<Review> reviews = databaseHelper.getReviewsForAnswer(answer.getAnswerID());
                reviewListView.getItems().setAll(reviews);
                reviewListView.setCellFactory(param -> new ListCell<Review>() {
                    @Override
                    protected void updateItem(Review review, boolean empty) {
                        super.updateItem(review, empty);
                        if (empty || review == null) {
                            setText(null);
                        } else {
                            setText(review.getUsername() + ": " + review.getReviewContent());
                        }
                    }
                });
            } else {
                reviewListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshReviews(Question question) {
        try {
            if (question != null) {
                List<Review> reviews = databaseHelper.getReviewsForQuestion(question.getQuestionID());
                reviewListView.getItems().setAll(reviews);
                reviewListView.setCellFactory(param -> new ListCell<Review>() {
                    @Override
                    protected void updateItem(Review review, boolean empty) {
                        super.updateItem(review, empty);
                        if (empty || review == null) {
                            setText(null);
                        } else {
                            setText(review.getUsername() + ": " + review.getReviewContent());
                        }
                    }
                });
            } else {
                reviewListView.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshReviewers() {
        try {
            String student = databaseHelper.getCurrentUserFromDatabase();
            reviewerListView.getItems().clear();
            Map<String, Integer> reviewers = trustedReviewer.getReviewers(student);
            reviewers.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                    .map(entry -> new TrustedReviewerEntry(entry.getKey(), entry.getValue()))
                    .forEachOrdered(entry -> reviewerListView.getItems().add(entry));

            reviewerListView.setCellFactory(new Callback<>() {
                @Override
                public ListCell<TrustedReviewerEntry> call(ListView<TrustedReviewerEntry> listView) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(TrustedReviewerEntry entry, boolean empty) {
                            super.updateItem(entry, empty);
                            setText((empty || entry == null) ? null : entry.getName() + " (Score: " + entry.getScore() + ")");
                        }
                    };
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}