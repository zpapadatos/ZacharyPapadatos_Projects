package application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailRecognizerJUnitAutmation {
	
	   @Test
	    public void EmailTest1() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("rightEmail@correct.com");
	            assertTrue(result.isEmpty());
	        } catch (Exception e) {
	            fail("Test 1 threw an exception unexpectedly: " + e.getMessage());
	        }
	    }

	    @Test
	    public void EmailTest2() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("");
	            assertFalse(result.isEmpty());
	        } catch (Exception e) {
	        	 fail("Test 2 was true unexpectedly: " + e.getMessage());
	            
	        }
	    }

	    @Test
	    public void EmailTest3() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("invalid-email-wrong");
	            assertFalse(result.isEmpty());
	        } catch (Exception e) {
	        	fail("Test 3 was true unexpectedly: " + e.getMessage());
	        }
	    }

	    @Test
	    public void EmailTest4() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("s@.c");
	            assertFalse(result.isEmpty());
	        } catch (Exception e) {
	        	fail("Test 4 was true unexpectedly: " + e.getMessage());
	            
	        }
	    }

	    @Test
	    public void EmailTest5() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("verrrrrrrrrrrrrrrrrrrrnnnnnnnnnnnnnnnnnnnnnnnnnnnnncccccccccccc@long.com");
	            assertFalse(result.isEmpty());
	        } catch (Exception e) {
	        	fail("Test 5 was true unexpectedly: " + e.getMessage());
	            
	        }
	    }

	    @Test
	    public void EmailTest6() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("medlen@cox.net");
	            assertTrue(result.isEmpty());
	        } catch (Exception e) {
	            fail("Test 6 threw an exception unexpectedly: " + e.getMessage());
	        }
	    }

	    @Test
	    public void EmailTest7() {
	        try {
	            String result = EmailRecognizer.checkForValidEmail("*nostartwith@special.com)");
	            assertFalse(result.isEmpty());
	        } catch (Exception e) {
	        	fail("Test 7 was true unexpectedly: " + e.getMessage());
	            
	        }
	    }
}
