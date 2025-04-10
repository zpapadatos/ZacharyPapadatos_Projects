package application;

import application.StaffHomePage;
import databasePart1.DatabaseHelper;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class StaffHomePageButtonTest {

    static class FakeDatabaseHelper extends DatabaseHelper {
        public java.util.List<Review> getReviews() { return java.util.List.of(); }
       // public java.util.List<Question> getAllQuestions() { return java.util.List.of(); }
        //public java.util.List<Answer> getAllAnswers() { return java.util.List.of(); }
        public java.util.List<String> getAllUsernames() { return java.util.List.of(); }
        public void flagReview(int id) {}
        public void flagUserAccount(String username) {}
    }

    @BeforeAll
    public static void init() {
        new JFXPanel(); // Initializes JavaFX environment
    }

    private void testButtonPress(String buttonText) {
        Platform.runLater(() -> {
            try {
                StaffHomePage page = new StaffHomePage(new FakeDatabaseHelper());
                Stage stage = new Stage();
                page.show(stage);

                Scene scene = stage.getScene();
                VBox root = (VBox) scene.getRoot();

                Button target = null;
                for (Node node : root.getChildren()) {
                    if (node instanceof Button b && b.getText().equals(buttonText)) {
                        target = b;
                        break;
                    }
                }

                assertNotNull(target, "Button '" + buttonText + "' not found");
                target.fire(); //Simulates pressing the button
            } catch (Exception e) {
                fail("Exception during button press test: " + e.getMessage());
            }
        });

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test public void testViewAllReviewsButtonFires() { testButtonPress("View All Reviews"); }
    //@Test public void testViewAllQuestionsButtonFires() { testButtonPress("View All Questions"); }
   // @Test public void testViewAllAnswersButtonFires() { testButtonPress("View All Answers"); }
    @Test public void testFlagaReviewButtonFires() { testButtonPress("Flag a Review"); }
    @Test public void testFlagUserAccountButtonFires() { testButtonPress("Flag User Account"); }
    @Test public void testLogoutButtonFires() { testButtonPress("Logout"); }
}
