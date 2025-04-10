package application;

public class Message {
	 private int messageID;
	 private int reviewID;
	 private String sender;
	 private String receiver;
	 private String content;
	 public Message(int messageID, int reviewID, String sender, String receiver, String content) {
	        this.messageID = messageID;
	        this.reviewID = reviewID;
	        this.sender = sender;
	        this.receiver = receiver;
	        this.content = content;
	    }
	 //Getters
	 public int getMessageID(){
		 return messageID; 
		 }
	 public int getReviewID() {
		 return reviewID; 
		 }
	 public String getSender() { 
		 return sender; 
		 }
	 public String getReceiver() { 
		 return receiver;
		 }
	 public String getContent() { 
		 return content; 
		 }
	 
	 //Setter
	 public void setContent(String content) { 
		 this.content = content; 
		 }
	 
	 
	 
	 
	 
}