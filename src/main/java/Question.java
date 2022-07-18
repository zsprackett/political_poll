import java.util.ArrayList;

public class Question {
    private boolean randomize;
    private String question;
    private QuestionOptions options;

    /**
     * Represents a question and the possible answers
     * @param q a string version of the question to ask
     * @param o a string array containing the possible answers
     * @param randomize a boolean that indicates whether to randomize the order of the answers
     */
    public Question(String q, String[] o, boolean randomize) {
        this.question = q;
        this.randomize = randomize;
        this.options = new QuestionOptions();

        if (o.length < 2) {
            throw new IllegalArgumentException("Expected an array with more than one entry but got " + o.length);
        }
        for (String s : o) {
            options.addOption(s, randomize);
        }
    }

    /**
     * Retrieve a string containing the questions and answers
     * @return the text
     */
    public String getPrintableQuestionAndAnswers() {
        return this.question +
                "\n\n" +
                this.options.getPrintableOptions(this.randomize);
    }

    /**
     * Look up the response associated with the provided character
     * @param response the character the user entered
     * @return the integer associated with the response
     * @throws IllegalArgumentException thrown for an invalid reponse
     */
    public Integer getResponseIndex(Character response) throws IllegalArgumentException {
        return this.options.getResponseIndex(response);
    }
}
