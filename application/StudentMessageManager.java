package application;

import java.util.ArrayList;
import databasePart1.DatabaseHelper;

public class StudentMessageManager {
    private DatabaseHelper databaseHelper;

    public StudentMessageManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Students send a private message to a reviewer regarding a review
    public void sendMessage(int reviewID,String student, String reviewer, String messageContent) throws Exception {
        databaseHelper.insertMessage(reviewID,student, reviewer, messageContent);
    }

    // Students retrieve all messages related to a review they are involved in
    public ArrayList<Message> getMessagesForReview(int reviewID) {
        return databaseHelper.getMessagesForReview(reviewID);
    }

    // Launches a new message window
    public void openMessageWindow(int reviewID,String student, String reviewer) {
        MessagePopup messagePopup = new MessagePopup(this,reviewID,student, reviewer);
        messagePopup.show();
    }
}