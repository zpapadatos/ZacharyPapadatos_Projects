package application;

public class TrustedReviewerEntry {
    private final String name;
    private final int score;

    //object field storage of the name and score 
    public TrustedReviewerEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    //allows to read the name and score 
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
