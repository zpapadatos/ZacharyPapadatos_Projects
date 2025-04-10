package application;

public class nameRecognizer {

		/**
		 * <p> Title: FSM-translated UserNameRecognizer. </p>
		 * 
		 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
		 * diagram into an executable Java program using the UserName Recognizer. The code 
		 * detailed design is based on a while loop with a select list</p>
		 * 
		 * <p> Copyright: Lynn Robert Carter © 2024 </p>
		 * 
		 * @author Lynn Robert Carter
		 * 
		 * @version 1.00		2024-09-13	Initial baseline derived from the Even Recognizer
		 * @version 1.01		2024-09-17	Correction to address UNChar coding error, improper error
		 * 									message, and improve internal documentation
		 * 
		 */

		/**********************************************************************************************
		 * 
		 * Result attributes to be used for GUI applications where a detailed error message and a 
		 * pointer to the character of the error will enhance the user experience.
		 * 
		 */

		public static String NameRecognizerErrorMessage = "";	// The error message text
		public static String NameRecognizerInput = "";			// The input being processed
		public static int NameRecognizerIndexofError = -1;		// The index of error location
		private static int state = 0;						// The current state value
		private static int nextState = 0;					// The next state value
		private static boolean finalState = false;			// Is this state a final state?
		private static String inputLine = "";				// The input line
		private static char currentChar;					// The current character in the line
		private static int currentCharNdx;					// The index of the current character
		private static boolean running;						// The flag that specifies if the FSM is 
															// running
		private static int userNameSize = 0;			// A numeric value may not exceed 16 characters

		// Private method to display debugging data
		private static void displayDebuggingInfo() {
			// Display the current state of the FSM as part of an execution trace
			if (currentCharNdx >= inputLine.length())
				// display the line with the current state numbers aligned
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
						((finalState) ? "       F   " : "           ") + "None");
			else
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
					nextState + "     " + userNameSize);
		}
		
		// Private method to move to the next character within the limits of the input line
		private static void moveToNextCharacter() {
			currentCharNdx++;
			if (currentCharNdx < inputLine.length())
				currentChar = inputLine.charAt(currentCharNdx);
			else {
				currentChar = ' ';
				running = false;
			}
		}

		/**********
		 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
		 * method.
		 * 
		 * @param input		The input string for the Finite State Machine
		 * @return			An output string that is empty if every things is okay or it is a String
		 * 						with a helpful description of the error
		 */
		public static String EvaluateName(String input) {
			// Check to ensure that there is input to process
			if(input.length() <= 0) {
				NameRecognizerIndexofError = 0;	// Error at first character;
				return "\n*** ERROR *** The input is empty";
			}
			
			// The local variables used to perform the Finite State Machine simulation
			state = 0;							// This is the FSM state number
			inputLine = input;					// Save the reference to the input line as a global
			currentCharNdx = 0;					// The index of the current character
			currentChar = input.charAt(0);		// The current character from above indexed position

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state

			NameRecognizerInput = input;	// Save a copy of the input
			running = true;						// Start the loop
			nextState = -1;						// There is no next state
			System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
			
			// This is the place where semantic actions for a transition to the initial state occur
			
			userNameSize = 0;					// Initialize the UserName size

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state
			while (running) {
				// The switch statement takes the execution to the code for the current state, where
				// that code sees whether or not the current character is valid to transition to a
				// next state
				switch (state) {
				case 0: 
					// State 0 has 1 valid transition that is addressed by an if statement.		
					// The current character is checked against A-Z, a-z, 0-9. If any are matched
					// the FSM goes to state 1			
					if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
							(currentChar >= 'a' && currentChar <= 'z' )) {	 	// Check for a-z	
						nextState = 1;
						userNameSize++;
					}
					// If it is none of those characters, the FSM halts
					else 
						running = false;
					// The execution of this state is finished
					break;
				
				case 1: 
					// State 1 has two valid transitions, 
					//	1: a A-Z, a-z that transitions back to state 1
					//  2: a space that transitions to state 2 	
					if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
							(currentChar >= 'a' && currentChar <= 'z' )) {
						nextState = 1; //keeps program in state one 
						
						// Count the character
						userNameSize++;
					}
					// State 2
					else if ((currentChar == ' ')) { 			 
						nextState = 2;
						
						// Count the .
						userNameSize++;
					}				
					// If it is none of those characters, the FSM halts
					else
						running = false;
					
					// The execution of this state is finished
					// If the size is larger than 16, the loop must stop
					if (userNameSize > 36)
						running = false;
					break;			
					
				case 2: 
					// A-Z, a-z State 1
					if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )) {	
						nextState = 1; 				
						// Count the odd digit
						userNameSize++;
						
					}
					// If it is none of those characters, the FSM halts
					else 
						running = false;

					// The execution of this state is finished
					// If the size is larger than 16, the loop must stop
					if (userNameSize > 36)
						running = false;
					break;			
				}
				
				if (running) {
					displayDebuggingInfo();
					// When the processing of a state has finished, the FSM proceeds to the next
					// character in the input and if there is one, it fetches that character and
					// updates the currentChar.  If there is no next character the currentChar is
					// set to a blank.
					moveToNextCharacter();

					// Move to the next state
					state = nextState;
					
					// Is the new state a final state?  If so, signal this fact.
					if (state == 1) finalState = true;

					// Ensure that one of the cases sets this to a valid value
					nextState = -1;
				}
				// Should the FSM get here, the loop starts again
		
			}
			displayDebuggingInfo();
			
			System.out.println("The loop has ended.");
			
			// When the FSM halts, we must determine if the situation is an error or not.  That depends
			// of the current state of the FSM and whether or not the whole string has been consumed.
			// This switch directs the execution to separate code for each of the FSM states and that
			// makes it possible for this code to display a very specific error message to improve the
			// user experience.
			NameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
			NameRecognizerErrorMessage = "\n*** ERROR *** ";
			
			// The following code is a slight variation to support just console output.
			switch (state) {
			case 0:
				// State 0 is not a final state, so we can return a very specific error message
				NameRecognizerErrorMessage += "A Name must start with A-Z, or a-z. Please try again\n";
				return NameRecognizerErrorMessage;

			case 1:
				// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
				// we must ensure the whole string has been consumed.

				if (userNameSize < 2) {
					// Name is too small
					NameRecognizerErrorMessage += "A Name must have at least 2 characters.\n";
					return NameRecognizerErrorMessage;
				}
				else if (userNameSize > 36) {
					// Name is too long
					NameRecognizerErrorMessage += 
						"A Name must have no more than 36 character.\n";
					return NameRecognizerErrorMessage;
				}
				else if (currentCharNdx < input.length()) {
					// There are characters remaining in the input, so the input is not valid
					NameRecognizerErrorMessage += 
						"A Name character may only contain the characters A-Z";
					return NameRecognizerErrorMessage;
				}
				else {
						// Name is valid
						NameRecognizerIndexofError = -1;
						NameRecognizerErrorMessage = "";
						return NameRecognizerErrorMessage;
				}

			case 2:
				// State 2 is not a final state, so we can return a very specific error message
				NameRecognizerErrorMessage +=
					"A Space must seperate first and last";
				return NameRecognizerErrorMessage;
				
			default:
				// This is for the case where we have a state that is outside of the valid range.
				// This should not happen
				return "";
			}
		}
	}


