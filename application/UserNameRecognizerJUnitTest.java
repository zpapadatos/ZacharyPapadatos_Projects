package application;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserNameRecognizerJUnitTest {
//checkForValidUserName
	@Test
	public void UserNameTest1() {
		boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("Zacha_2024"); 
			success = result.isEmpty(); 
		} catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Expected valid username to succeed");
		
	}
	@Test
	public void UserNameTest2() { 
		boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("AAA"); 
			success = result.isEmpty(); 
			
		} catch (Exception e) { 
			success = false; 
		}
        assertFalse(success, "Expected not long enough to fail");

	}
	@Test
    public void UserNameTest3() { 
    	boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("zpapadatos"); 
			success = result.isEmpty(); 
			
		} catch (Exception e) { 
			success = false; 
		}
        assertTrue(success, "Expected pass valid username ");
	}
	@Test 
    public void UserNameTest4() { 
    	boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("AAA".repeat(30)); 
			success = result.isEmpty(); 
			
		} catch (Exception e) { 
			success = false; 
		}
        assertFalse(success, "Expected to fail too long");
		
	}
	@Test
    public void UserNameTest5() { 
    	boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("@zacharypapadatps"); 
			success = result.isEmpty(); 
			
		} catch (Exception e) { 
			success = false; 
		}
        assertFalse(success, "Expected to fail does not start with a letter");
	}
	@Test
    public void UserNameTest6() { 
    	boolean success; 
		try { 
			String result = UserNameRecognizer.checkForValidUserName("1zacharypapadatps"); 
			success = result.isEmpty(); 
			
		} catch (Exception e) { 
			success = false; 
		}
        assertFalse(success, "Expected to fail does not start with a letter");
	}

}
