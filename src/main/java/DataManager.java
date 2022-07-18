import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingWithUserConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

public class DataManager {

    /**
     * Reads the specified data set in ARFF format
     * @param filename      the path to the ARFF file to read
     * @return Instances
     * @throws IOException
     */
    static public Instances readDataSet(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        Instances datasetInstances = new Instances(bufferedReader);
        bufferedReader.close();
        return datasetInstances;
    }

    /**
     * Reads the data set for every element in the Party enum
     * @return Instances    a unified dataset containing information for all parties
     * @throws Exception
     */
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

            // We need to add a new attribute to hold the party
            Add filter = new Add();
            filter.setAttributeIndex("first");
            filter.setAttributeName("party");
            filter.setNominalLabels(Party.toCommaSeparatedString());
            filter.setInputFormat(partyInstances);
            partyInstances = Filter.useFilter(partyInstances, filter);

            // Now set the appropriate value for this new attribute
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
                // merge the dataset for this party into our master dataset
                result.addAll(partyInstances);
            }
        }
        if (result != null) {
            // format the dataset and randomize its order before returning
            result.setRelationName("affiliation");
            // Ensure that all the weights are equivalent
            if (!result.allAttributeWeightsIdentical()) {
                Enumeration<Attribute> enumeration = result.enumerateAttributes();
                while (enumeration.hasMoreElements()) {
                    Attribute att = enumeration.nextElement();
                    att.setWeight(1.0);
                }
            }
            result.randomize(new Random());
        }
        return result;
    }

    /**
     * Split out the data applicable to a single particular party and write it to desk.
     * @param instances     a full dataset, containing data about all parties
     * @param party         the party for which we want to update the datafile on disk
     * @throws Exception
     */
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

    static public ArrayList<String> readQuestionsFromFile(String filepath) {
        ArrayList<String> questions = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null ) {
                questions.add(line);
                line = reader.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}