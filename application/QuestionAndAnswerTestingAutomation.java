package application;

/**
 * Performs automated testing for the {@link Question} and {@link Answer} classes.
 * <p>
 * This class will perform some automated Question and answer validation tests and will report whether the input
 * was accepted or rejected by the program according to the expected behavior.
 *
 * <p>This class is part of HW3 and demonstrates how automated tests work and can complete 
 * tests and validation without requiring user input.
 *
 * @author Zachary Papadatos 
 * @version 1.0
 * @see Question 
 * @see Answer
 */

public class QuestionAndAnswerTestingAutomation {
	
	static int numPassed = 0; 
	static int numFailed = 0; 
	
	/**
     * Entry point for the tests.
     * <p>
     * Executes test cases that create {@link Question} and {@link Answer} objects
     *
     * @param args Command-line arguments.
     */

	public static void main(String[] args) { 
	System.out.println("____________________________________________________________________________");
	System.out.println("\nTesting Automation");

	performQuestionTest(1,"Is java a programming language?", true); 
	performQuestionTest(2, "", false); 
	performQuestionTest(3, "A".repeat(5000), false); 
	performAnswerTest(1, "Yes java is considered a programming language", true); 
	performAnswerTest(2, "", false); 
	performAnswerTest(3, "A".repeat(5000), false); 

	System.out.println("____________________________________________________________________________");
	        System.out.println();
	        System.out.println("Number of tests passed: " + numPassed);
	        System.out.println("Number of tests failed: " + numFailed);

	}
	 /**
     * Performs a single question validation test.
     * <p>
     * Creates a {@link Question} object using the input and
     * checks if the result matches the expected outcome of the program.
     *
     * @param testCase      The test case number.
     * @param questionText  The question text to be validated.
     * @param expectedPass  {@code true} if the test is expected to pass; {@code false} otherwise.
     */

	private static void performQuestionTest(int testCase, String questionText, boolean expectedPass) {
	    System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
	    System.out.println("Question Input: \"" + questionText + "\"");
	    System.out.println("____________________________________________________________________________");

	    boolean success;

	    try {
	        String dummyUser = "TestUser";

	        new Question(dummyUser, questionText);
	        success = true;
	    } catch (Exception e) {
	        success = false;
	    }

	    evaluateTestResult(success, expectedPass, "Question");
	}

	/**
     * Performs a single answer validation test.
     * <p>
     * Creates an {@link Answer} object using the input and
     * checks if the result matches the expected outcome of the program.
     *
     * @param testCase     The test case number.
     * @param answerText   The answer text to be validated.
     * @param expectedPass {@code true} if the test is expected to pass; {@code false} otherwise.
     */

	private static void performAnswerTest(int testCase, String answerText, boolean expectedPass) {
	    System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
	    System.out.println("Answer Input: \"" + answerText + "\"");
	    System.out.println("____________________________________________________________________________");

	    boolean success;

	    try {
	        String dummyUser = "TestUser";
	        int dummyQuestionID = 1;

	        new Answer(dummyUser, answerText, dummyQuestionID);
	        success = true;
	    } catch (Exception e) {
	        success = false;
	    }

	    evaluateTestResult(success, expectedPass, "Answer");
	}
	
	 /**
     * Evaluates and prints the result of all the test cases from the program.
     *
     * @param success      {@code true} if the test succeeded; {@code false} otherwise.
     * @param expectedPass {@code true} if the test was expected to succeed.
     * @param testType     The type of test being performed .
     */

	    private static void evaluateTestResult(boolean success, boolean expectedPass, String testType) {
	        if (success && expectedPass) {
	            System.out.println("***Success*** The " + testType + " test passed as expected!");
	            numPassed++;
	        } else if (!success && !expectedPass) {
	            System.out.println("***Success*** The " + testType + " test correctly failed as expected!");
	            numPassed++;
	        } else if (!success) {
	            System.out.println("***Failure*** The " + testType + " test failed, but it was expected to pass!");
	            numFailed++;
	        } else {
	            System.out.println("***Failure*** The " + testType + " test passed, but it was expected to fail!");
	            numFailed++;
	        }
	    }
	    

	}


