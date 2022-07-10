import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingWithUserConstant;
import weka.filters.unsupervised.instance.RemoveWithValues;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class DataManager {
    private static Instance current;

    static public Instances readDataSet(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        Instances datasetInstances = new Instances(bufferedReader);
        bufferedReader.close();
        return datasetInstances;
    }

    static public Instances readAllDataSets() throws Exception {
        Instances result = null;
        for (Party p : Party.values()) {
            Instances partyInstances = null;
            String filename = "Data/" + p.toString().toLowerCase() + ".arff";
            try {
                partyInstances = readDataSet(filename);
            } catch (IOException e) {
                System.out.println("Failed to read " + filename + ": " + e);
            }

            Add filter = new Add();
            filter.setAttributeIndex("first");
            filter.setAttributeName("party");
            filter.setNominalLabels(Party.toCommaSeparatedString());
            filter.setInputFormat(partyInstances);
            partyInstances = Filter.useFilter(partyInstances, filter);

            ReplaceMissingWithUserConstant filter2 = new ReplaceMissingWithUserConstant();
            filter2.setAttributes("party");
            filter2.setNominalStringReplacementValue(p.toString());
            filter2.setInputFormat(partyInstances);
            partyInstances = Filter.useFilter(partyInstances, filter2);

            if (result == null) {
                result = partyInstances;
            } else {
                if (!result.equalHeaders(partyInstances)) {
                    throw new Exception("Header mismatch:" + result.equalHeadersMsg(partyInstances));
                }
                result.addAll(partyInstances);
            }
        }
        result.setRelationName("affiliation");
        result.randomize(new Random());
        return result;
    }

    static public void writeDataSet(Instances instances, Party party) throws Exception {
        Instances partyInstances = new Instances(instances);

        // iterate backwards, when we remove an instance, the positions shift.
        for (int i = partyInstances.numInstances() - 1; i >= 0; i--) {
            Instance current = partyInstances.instance(i);
            if (!current.stringValue(instances.attribute("party")).equals(party.toString())) {
                partyInstances.delete(i);
            }
        }

        // remove column which contains the party since it adds no value
        Remove remove = new Remove();
        remove.setAttributeIndicesArray(new int[] {instances.attribute("party").index()});
        remove.setInputFormat(partyInstances);
        partyInstances = Filter.useFilter(partyInstances, remove);
        ArffSaver saver = new ArffSaver();

        // write out our datafile
        partyInstances.setRelationName(party.toString().toLowerCase());
        saver.setInstances(partyInstances);
        saver.setFile(new File("Data/" + party.toString().toLowerCase() + ".arff"));
        saver.writeBatch();
    }
}
