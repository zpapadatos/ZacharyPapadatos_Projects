package application;

public class Answer {
private String userName; // to track the user who posted the answer
private int answerID; // to identify specific answers
private String answerText; // the content of the answer
private int questionID; // keeps track of which question the answer belongs to
private String astatus;

// constructor
public Answer(String userName, String answerText, int questionID) throws Exception {
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
	this.questionID = questionID;
	setAnswerText(answerText);
}

public Answer(int id, String userName, int questionID, String answerText, String astatus) {
    this.answerID = id;
    this.userName = userName;
    this.questionID = questionID;
    this.answerText = answerText;
    this.astatus = astatus;
}

public String getUserName() { // getter for userID
	return userName;
}

public int getAnswerID() { // getter for answerID
	return answerID;
}

public String getAnswerText() { // getter for answerText
	return answerText;
}

public int getQuestionID() { // getter for questionID
	return questionID;
}

public void setAnswerID(int answerID) {
    this.answerID = answerID;
}

public String getStatus() {
	return astatus;
}

public void setAnswerText(String userInput) throws Exception { // setter for questionText
	if (userInput.length() >= 2 && userInput.length() <= 500) { // checks that length is between 20 and 500 chars
		answerText = userInput;
	}
	else {
		throw new Exception("Answer must be between 2 and 500 characters.");
	}
}

}