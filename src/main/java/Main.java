import weka.core.Instances;
import weka.core.DenseInstance;

public class Main {
    public static void main(String[] args) throws Exception {
        Question q  = new Question(
            "The question?",
            new String[] {
                    "answer a",
                    "answer b",
                    "answer c",
                    "answer d"
            }
        );

        System.out.println(q.getPrintableQuestionAndAnswers());
        System.out.println(q.getResponseIndex('b'));

        Instances i = DataManager.readAllDataSets();
        i.setClass(i.attribute("party"));

        AI ai = new AI(i);
        ai.prepareData(0.7);
        ai.buildClassifier();
        ai.modelDetails();

        DenseInstance d = ai.getEmptyInstance();

        d.setValue(i.attribute("q1"), "1");
        /*
        d.setValue(test.attribute("q2"), "1");
        d.setValue(test.attribute("q3"), "1");
        d.setValue(test.attribute("q4"), "2");
        d.setValue(test.attribute("q5"), "2");
        d.setValue(test.attribute("q6"), "2");
        d.setValue(test.attribute("q7"), "2");
        d.setValue(test.attribute("q8"), "2");
        d.setValue(test.attribute("q9"), "2");
        d.setValue(test.attribute("q10"), "1");
        */
        System.out.println("Naive Bayes Prediction: " + ai.classify(d));
    }
}
