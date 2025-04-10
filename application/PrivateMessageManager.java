package application;

import java.util.ArrayList;
import databasePart1.DatabaseHelper;

public class PrivateMessageManager {
	 private DatabaseHelper databaseHelper;
	 
	 public PrivateMessageManager(DatabaseHelper databaseHelper) {
	        this.databaseHelper = databaseHelper;
	    }
	 public ArrayList<Message> getMessagesForReview(int reviewID) {
	        return databaseHelper.getMessagesForReview(reviewID);
	         }
	 public void replyToMessage(int reviewID, String sender, String receiver, String replyContent) throws Exception {
	        databaseHelper.insertMessage(reviewID, sender, receiver, replyContent);
	    }
	 
}