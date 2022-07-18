import java.util.ArrayList;
import org.apache.commons.cli.*;
import weka.core.Instances;
import weka.core.DenseInstance;

public class Main {
    /**
     * Entrypoint
     * @param args - string array of CLI arguments
     * @throws Exception thrown on error
     */
    public static void main(String[] args) throws Exception {
        Instances allResults = DataManager.readAllDataSets();
        // scroll the log messages off the screen
        System.out.println(new String(new char[50]).replace("\0", "\r\n"));
        System.out.println("Dataset consists of " + allResults.size() + " results.");

        // parse out our CLI options
        Options options = new Options();
        options.addOption("d", "detail", false, "print model details");
        options.addOption("b", "boostq1", false, "boost the weight of question 1");
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            System.err.println("Parsing CLI options failed.  Reason: " + exp.getMessage());
        }

        if (line != null && line.hasOption("b")) {
            System.out.println("Boosting the weight of question 1.");
            allResults.setAttributeWeight(allResults.attribute("q1"), 2.0);
        }

        QuestionPoser questionPoser = new QuestionPoser();

        allResults.setClass(allResults.attribute("party"));
        AI ai = new AI(allResults);
        // Don't bother building a test dataset if we don't have many records
        if (allResults.size() < 10) {
            ai.prepareData(1.0);
        } else {
            ai.prepareData(0.7);
        }
        ai.buildClassifier();
        if (line != null && line.hasOption("d")) {
            ai.modelDetails();
        }

        // Loop through questions and make predictions
        DenseInstance d = ai.getEmptyInstance();
        Integer r;
        ArrayList<String> questions = DataManager.readQuestionsFromFile("data/questions.txt");
        for (int i = 0; i < questions.size(); i++) {
            Question the_question = new YesNoQuestion(questions.get(i));
            r = questionPoser.poseQuestion(the_question, false);
            d.setValue(allResults.attribute("q" + (i+1)), r);

            System.out.println("You seem like a " + Party.capitalizeFirst(ai.classify(d)));
        }


        // Ask the user, add their entry to the dataset for the party they specify
        Question q = new Question(
                "What political affiliation do you feel you most closely align with?",
                Party.getNames(),
                true
        );
        r = questionPoser.poseQuestion(q, true);
        Party specifiedParty = Party.getById(r);
        if (specifiedParty != null) {
            System.out.println("You claim to be a " + Party.capitalizeFirst(specifiedParty) + ". Recording results.");
            d.setValue(allResults.attribute("party"), specifiedParty.toString());
            allResults.add(d);
            DataManager.writeDataSet(allResults, Party.getById(r));
        }
    }
}
