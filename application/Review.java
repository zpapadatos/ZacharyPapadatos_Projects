package application;

public class Review {
    private int reviewID;
    private int questionID;
    private int answerID;
    private String reviewContent;
    private String username;
    private String reviewType;
    

    // Constructor
    public Review(int reviewID, int questionID,int answerID, String reviewContent, String username, String reviewType) {
        this.reviewID = reviewID;
        this.questionID = questionID;
        this.answerID = answerID;
        this.reviewContent = reviewContent;
        this.username = username;
        this.reviewType = reviewType;
    }

    // Getters
    public int getReviewID() {
        return reviewID;
    }
    
    public int getanswerID() {
    	 return answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getUsername() {
        return username;
    }
    public String getReviewType() {
        return reviewType;
    }

    // Setters
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }
}