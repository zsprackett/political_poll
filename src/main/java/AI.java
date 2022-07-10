import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instances;

public class AI {
    private Instances all;
    private Instances training;
    private Instances test;
    private NaiveBayes naiveBayes;

    public AI(Instances instances) {
        this.all = instances;
    }

    public void prepareData(double trainingRatio) {
        int trainingSize = (int) Math.round(all.numInstances() * trainingRatio);
        int testSize = (int) all.numInstances() - trainingSize;
        this.training = new Instances(all,0,trainingSize);
        this.test = new Instances(all,trainingSize,testSize);
    }

    public void buildClassifier() throws Exception {
        this.naiveBayes = new NaiveBayes();
        this.naiveBayes.buildClassifier(this.training);
    }

    public void modelDetails() throws Exception {
        Evaluation eval = new Evaluation(this.training);
        eval.evaluateModel(this.naiveBayes,test);
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
    }

    public DenseInstance getEmptyInstance() {
        DenseInstance d = new DenseInstance(training.numAttributes());
        this.test.add(d);
        d.setDataset(test);
        return(d);
    }

    public String classify(DenseInstance d) throws Exception {
        double result = this.naiveBayes.classifyInstance(d);
        return(this.test.classAttribute().value((int)result));
    }
}
