package application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEvaluatorJUnitTest {
	@Test
    public void PasswordTest1() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("Abcdef1!");
            success = result.isEmpty(); 
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Expected valid password to succeed");
    }

    @Test
    public void PasswordTest2() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("abcdef1!");
            success = result.isEmpty(); 
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected missing uppercase to fail");
    }

    @Test
    public void PasswordTest3() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("ABCDEF1!");
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected missing lowercase to fail");
    }

    @Test
    public void PasswordTest4() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("Abcdefg!");
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected missing digit to fail");
    }

    @Test
    public void PasswordTest5() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("Abcdefg1");
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected missing special char to fail");
    }

    @Test
    public void PasswordTest6() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("Ab1!");
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected short password to fail");
    }

    @Test
    public void PasswordTest7() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("");
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected empty password to fail");
    }
    @Test
    public void PasswordTest8() {
        boolean success;
        try {
            String result = PasswordEvaluator.evaluatePassword("a".repeat(100));
            success = result.isEmpty();
        } catch (Exception e) {
            success = false;
        }
        assertFalse(success, "Expected excessive password to fail");
    }
}
