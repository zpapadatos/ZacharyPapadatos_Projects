package application;
import java.util.ArrayList;

import databasePart1.DatabaseHelper;

public class QuestionList {
private ArrayList<Question> questionList;
private DatabaseHelper databaseHelper;

//constructor
public QuestionList(DatabaseHelper databaseHelper){
    this.databaseHelper = databaseHelper;
    try {
		this.questionList = databaseHelper.getAllQuestions();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


public void addQuestion(Question q) throws Exception{
    int generatedQuestionID = databaseHelper.insertQuestion(q);
    q.setQuestionID(generatedQuestionID);
    questionList.add(q);
}

// returns the full list
public ArrayList<Question> getQuestionList() {
	return questionList;
}

//Searches question based on ID
public Question searchQuestionById(int id) {
       for (Question q : questionList) {
           if (q.getQuestionID() == id) {
               return q;
           }
       }
       return null;
   }
//Searches for a question based on certain keyword
public ArrayList<Question> searchQuestionsByKeyword(String keyword) {
       ArrayList<Question> results = new ArrayList<>();
       String lowerKeyword = keyword.toLowerCase();
       for (Question q : questionList) {
           if (q.getQuestionText().toLowerCase().contains(lowerKeyword)) {
               results.add(q);
           }
       }
       return results;
   }

//update an existing question
public void updateQuestion(int id, String updated, String username) throws Exception {
for (Question question : questionList) {
    if (question.getQuestionID() == id) {
        if (!databaseHelper.isQuestionOwner(username, id)) {
            return;
        }
        question.setQuestionText(updated);

        databaseHelper.updateQuestion(id, updated);
        return;
    }
}
}

public void deleteQuestion(int questionID, String userID) throws Exception {
    for (int i = 0; i < questionList.size(); i++) { // iterate through questionList to find the right question to delete
        if (questionList.get(i).getQuestionID() == questionID) {
            if (!questionList.get(i).getUsername().equals(userID)) { // once the right question is found, check if the user trying to delete the question is also the author.
                throw new Exception("You can't delete this question because you didn't post it."); // if not, send an error
            }
            databaseHelper.deleteQuestion(questionID);
            databaseHelper.deleteAnswersForQuestion(questionID);
            questionList.remove(i); // if the question is found and user matches author, then remove the question
            return;
        }
    }
    throw new Exception("Question doesn't exist"); // send an error if no match was found
    }

}