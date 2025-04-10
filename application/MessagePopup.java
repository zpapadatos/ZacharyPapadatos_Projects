package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MessagePopup {
    private StudentMessageManager manager;
    private int reviewID;
    private String student;
    private String reviewer;

    public MessagePopup(StudentMessageManager manager,int reviewID,String student, String reviewer) {
        this.manager = manager;
        this.reviewID = reviewID;
        this.student = student;
        this.reviewer = reviewer;
         
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Send Message to Reviewer");

        Label instruction = new Label("Enter your message:");
        TextArea messageArea = new TextArea();
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            String content = messageArea.getText();
            try {
                manager.sendMessage(reviewID,student, reviewer, content);
                stage.close(); // close the popup after sending
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instruction, messageArea, sendButton);
        Scene scene = new Scene(layout, 400, 250);

        stage.setScene(scene);
        stage.show();
    }
}