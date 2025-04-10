package application;
import java.sql.SQLException;
import java.util.ArrayList;

import databasePart1.DatabaseHelper;

public class AnswerList {
private ArrayList<Answer> answerList;
private DatabaseHelper databaseHelper;

//constructor
public AnswerList(DatabaseHelper databaseHelper){
	this.databaseHelper = databaseHelper;
	try {
		this.answerList = databaseHelper.getAllAnswers();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

// adds a new answer to the ArrayList given its valid
public void addAnswer(Answer a) throws Exception {
	int generatedAnswerID = databaseHelper.insertAnswer(a);
	a.setAnswerID(generatedAnswerID);
	answerList.add(a);
}

// returns the answerList
public ArrayList<Answer> getAnswerList() {
	return answerList;
}

// iterates through the ArrayList to find the answer to delete
public void deleteAnswer(int answerID, String userID) throws Exception {
    for (int i = 0; i < answerList.size(); i++) { // iterate through answerList to find the right answer to delete
        if (answerList.get(i).getAnswerID() == answerID) {
            if (!answerList.get(i).getUserName().equals(userID)) { // once the right answer is found, check if the user trying to delete the answer is also the author.
                throw new Exception("You can't delete this answer because you didn't post it."); // if not, send an error
            }
            databaseHelper.deleteAnswer(answerID);
            answerList.remove(i); // if the question is found and user matches author, then remove the question
            return;
        }
    }
    throw new Exception("answer doesn't exist"); // send an error if no match was found
}

// removes all answers associated with one question, this is called when a question is deleted
public void deleteAnswersForQuestion(int questionID) {
	for (int i = answerList.size() - 1 ;i >= 0; i--) { // iterate backwards through answerList so deletions don't mess up the loop index
		if (answerList.get(i).getQuestionID() == questionID) { // if the answer belongs to the question with questionID, delete it
			answerList.remove(i);
		}
	}
}

//update an existing answer
public void updateAnswer(int id, String updated, String username) throws Exception {
for (Answer answer : answerList) {
 if (answer.getAnswerID() == id) {
     if (!databaseHelper.isAnswerOwner(username, id)) {
         return;
     }
     answer.setAnswerText(updated);
     databaseHelper.updateAnswer(id, updated);
     return;
 }
}
}

// returns a list containing all the answers for a specific question
public ArrayList<Answer> getAnswersForQuestion(int questionID) throws Exception {
	ArrayList<Answer> matching = new ArrayList<>();
// if there are no answers, send an error
	if (answerList.size() <= 0) {
		throw new Exception("No answers found");
	}
//iterate through the list and add all matching answers
	for (int i = 0; i < answerList.size(); i++) {
		if (answerList.get(i).getQuestionID() == questionID) { // if an answer in answerList belongs to the question, add it to the ArrayList to be returned
			matching.add(answerList.get(i));
		}
	}
	return matching;
}
}