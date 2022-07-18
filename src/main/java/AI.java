import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class AI {
    private Instances all;
    private Instances training;
    private Instances test;
    private NaiveBayes naiveBayes;

    public AI(Instances instances) {
        this.all = instances;
    }

    /**
     * Prepare the data to build our model
     * @param trainingRatio     the percentage of data to use for training the model
     */
    public void prepareData(double trainingRatio) {
        int trainingSize = (int) Math.round(all.numInstances() * trainingRatio);
        int testSize = (int) all.numInstances() - trainingSize;
        this.training = new Instances(all,0,trainingSize);
        this.test = new Instances(all,trainingSize,testSize);
    }

    /**
     * Builds our naive bayes classifier
     * @throws Exception
     */
    public void buildClassifier() throws Exception {
        this.naiveBayes = new NaiveBayes();
        this.naiveBayes.buildClassifier(this.training);
    }

    /**
     * Displays information about our model
     * @throws Exception
     */
    public void modelDetails() throws Exception {
        if (this.test.size() > 0) {
            Evaluation eval = new Evaluation(this.training);
            eval.evaluateModel(this.naiveBayes, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toMatrixString());
        } else {
            System.out.println("We don't have a test dataset to evaluate our model.");
        }
    }

    /**
     * Constructs an empty Instance with the correct number of attributes
     * @return DensInstance
     */
    public DenseInstance getEmptyInstance() {
        DenseInstance d = new DenseInstance(training.numAttributes());
        this.test.add(d);
        d.setDataset(test);
        return(d);
    }

    /**
     * Classify the provided instances
     * @param d         the instance to classify
     * @return Party    the political affiliation predicted from the data instance
     * @throws Exception
     */
    public Party classify(Instance d) throws Exception {
        double result = this.naiveBayes.classifyInstance(d);
        return(Party.valueOf(this.test.classAttribute().value((int)result)));
    }
}
