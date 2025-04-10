package application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionAndAnswerJUnitTesting {
	
	@Test 
	public void QuestionTest1() { 
		boolean success;
        try {
            new Question("TestUser", "Is java a programming language?");
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Expected valid question threw an exception ");
    }
        
	
	@Test 
	public void QuestionTest2() {
		boolean success; 
		try { 
			new Question("testUser", ""); 
			success = true; 
		} catch(Exception e) { 
			success = false; 
			
		}
		assertFalse(success, "Expected invalid question to succeed");

	}
	@Test 
	public void QuestionTest3() {
		boolean success; 
		try { 
			new Question("TestUser", "A".repeat(5000));
			success = true; 
		} catch(Exception e) { 
			success = false; 
		}
		assertFalse(success, "Expected invalid question to succeed");

	}
	@Test 
	public void AnswerTest1() { 
		boolean success;
        try {
            new Answer("TestUser", "Yes java is considered a programming language", 1);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Expected valid answer to succeed");
    }
	@Test 
	public void AnswerTest2() { 
		boolean success;
        try {
            new Answer("TestUser", "", 2);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected was true unexpectedly");
    }
	
	@Test 
	public void AnswerTest3() { 
		boolean success; 
		try { 
			new Answer("TestUser","A".repeat(5000),3);
            success = true;
		}catch (Exception e) {
            success = false;	
	    }
        assertFalse(success, "Expected was true unexpectedly");

	}
	
}
