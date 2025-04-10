package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.Map;

public class TrustedReviewer {
    private final DatabaseHelper databaseHelper;

    //saves to the field
    public TrustedReviewer(DatabaseHelper db) {
        this.databaseHelper = db;
    }

    //adds a a reviewer with a score 
    public void addReviewer(String student, String reviewer, int score) throws SQLException {
        databaseHelper.insertTrustedReviewer(student, reviewer, score);
    }

    //delete reviewer with the score 
    public void deleteReviewer(String student, String reviewer) throws SQLException {
        databaseHelper.deleteTrustedReviewer(student, reviewer);
    }

    //gets all trusted reviewers 
    public Map<String, Integer> getReviewers(String student) throws SQLException {
        return databaseHelper.getTrustedReviewers(student);
    }
}
  


