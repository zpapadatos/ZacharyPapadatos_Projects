package application;

/**
 * Performs automated testing for the {@link EmailRecognizer} class.
 * <p>
 * This class will perform some automated email validation tests and will report whether the input
 * was accepted or rejected by the program according to the expected behavior.
 *
 * <p>This class is part of HW3 and demonstrates how automated tests work and can complete 
 * tests and validation without requiring user input.
 *
 * @author Zachary Papadatos 
 * @version 1.0
 * @see EmailRecognizer
 */


public class EmailRecognizerAutomatedTests {
	
	/**
	 * Default constructor for EmailRecognizerAutomatedTests.
	 */

	
	 public EmailRecognizerAutomatedTests() {
		
	    }
	
	static int numPassed = 0; 
	static int numFailed = 0; 
	
	/**
     * Entry point for the tests.
     * <p>
     * Executes five test cases using {@link EmailRecognizer#checkForValidEmail(String)}
     * and compares the result to the expected outcome from the program.
     *
     * @param args Command-line arguments.
     */
	
	
	public static void main(String[] args) { 
		System.out.println("____________________________________________________________________________");
		System.out.println("\nTesting Automation");
		
		performEmailTest(1, "rightEmail@correct.com", true ); 
		performEmailTest(2, "", false ); 
		performEmailTest(3, "invalid-email-wrong", false); 
		performEmailTest(4, "s@.c", false ); 
		performEmailTest(5, "verrrrrrrrrrrrrrrrrrrrnnnnnnnnnnnnnnnnnnnnnnnnnnnnncccccccccccc@long.com", false ); 
		performEmailTest(6, "medlen@cox.net", true); 
		performEmailTest(7, "*nostartwith@special.com)", false); 
		

		System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
		

	}
	
	/**
     * Performs a single email validation test case.
     * <p>
     * Uses {@link EmailRecognizer#checkForValidEmail(String)} to validate
     * the provided input and checks if the result matches the expected outcome of the program.
     *
     * @param testCase      The test case number.
     * @param EmailText     The email input string to be validated.
     * @param expectedPass  {@code true} if the test is expected to pass; {@code false} otherwise.
     */
	 private static void performEmailTest(int testCase, String EmailText, boolean expectedPass) {
	        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
	        System.out.println("Email Input: \"" + EmailText + "\"");
	        System.out.println("____________________________________________________________________________");

	        boolean success;

	        try {
	            String result = EmailRecognizer.checkForValidEmail(EmailText);
	            success = result.isEmpty(); // success = true if no error message
	        } catch (Exception e) {
	            success = false; // exception = failure
	        }

	        evaluateTestResult(success, expectedPass, "Email");
	    }
	 
	 /**
	     * Evaluates and prints the result of a test case.
	     *
	     * @param success       {@code true} if the test succeeded; {@code false} otherwise.
	     * @param expectedPass  {@code true} if the test was expected to pass.
	     * @param testType      The type of test being performed .
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
