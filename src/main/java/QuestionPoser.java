import java.util.Scanner;

public class QuestionPoser {
    Scanner scan;

    /**
     * Constructor
     */
    public QuestionPoser() {
        this.scan = new Scanner(System.in);
    }

    /**
     * Ask the user the question and get the response
     * @param q the Question to ask
     * @param randomize boolean that determines whether to randomize the order of the answers
     * @return an Integer ID that matches the chosen option
     */
    public Integer poseQuestion(Question q, boolean randomize) {
        Integer response = -1;
        while(response < 0) {
            System.out.println(q.getPrintableQuestionAndAnswers());
            String input = null;
            try {
                System.out.print("Select an option: ");
                input = this.scan.next().toUpperCase();
                if (input.length() != 1) {
                    System.out.println("Responses must be exactly one character in length.");
                } else {
                    response = q.getResponseIndex(input.charAt(0));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid selection " + input.charAt(0));
            }
        }
        return(response);
    }
}
