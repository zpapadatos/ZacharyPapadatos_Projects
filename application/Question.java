package application;

public class Question {
    private String userName; // to track the user who asked the question
    private int questionID; // to identify specific questions
    private String questionText; // the content of the question
    private String qstatus;

    // constructor
    public Question(String userName, String questionText) throws Exception {
        if (userName.length() >= 3 && userName.length() <= 30) { // checks that userID length is between 3 and 30 chars
            if (userName.matches("[a-zA-Z0-9]+")) { // checks for no special characters
                this.userName = userName;
            }
            else {
                throw new Exception("User ID must contain only letters and numbers.");
            }
        }
        else {
            throw new Exception("User ID must be between 3 and 30 characters.");
        }
        setQuestionText(questionText);
    }

    public Question (int questionID, String userName, String questionText, String qstatus) throws Exception {
        this.userName = userName;
        this.questionText = questionText;
        this.questionID = questionID;
        this.qstatus = qstatus;
    }

    public String getUsername() { // getter for userName
        return userName;
    }

    public int getQuestionID() { // getter for questionID
        return questionID;
    }

    public String getQuestionText() { // getter for questionText
        return questionText;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }
    
    public String getStatus() {
    	return qstatus;
    }

    public void setQuestionText(String userInput) throws Exception { // setter for questionText
        if (userInput.length() >= 2 && userInput.length() <= 500) { // checks that length is between 2 and 500 chars
            questionText = userInput;
        }
        else {
            throw new Exception("Question must be between 20 and 500 characters.");
        }
    }

	public void toggleStatus() {
		// TODO Auto-generated method stub
		
	}


}