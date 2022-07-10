import java.util.HashMap;
import java.util.ArrayList;

public class Question {
    private String question;
    private QuestionOptions options;
    private ArrayList<Character> selectors;
    public Question(String q, String[] o) {
        this.question = q;
        this.options = new QuestionOptions();

        if (o.length != 4) {
            throw new IllegalArgumentException("Expected an array of length 4 but got length " + o.length);
        }
        for (int i = 0; i < o.length; i++) {
            options.addOption(o[i]);
        }
    }

    public String getPrintableQuestionAndAnswers() {
        StringBuilder result = new StringBuilder();
        result.append(this.question);
        result.append("\n\n");
        result.append(this.options.getPrintableOptions());
        return result.toString();
    }

    public String getQuestion() {
        return this.question;
    }

    public Integer getResponseIndex(Character response) {
        return this.options.getResponseIndex(response);
    }
}
