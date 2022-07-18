public class YesNoQuestion extends Question {
    /**
     * Override the Question for convenience to make asking Yes/No questions easier
     * @param q a string containing the question
     */
    public YesNoQuestion(String q) {
        super(q, new String[]{"Yes", "No"}, false);
    }
}