import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WekaRunner {

    /**
     * @param unknownData Instances who will be classified
     * @return results of classification
     */
    private Instances runModel(Instances unknownData) {
        try {
            CostSensitiveClassifier model = loadClassifier();
            return classifyNewInstance(model, unknownData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param dataFile load in Arff file and convert to instances
     * @return instances
     */
    private Instances loadArff(String dataFile) throws IOException {
        try {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(dataFile);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            throw new IOException("could not read from file");
        }
    }

    /**
     * read Weka model from file path
     * @return finalized Weka model Random Forest
     */
    private CostSensitiveClassifier loadClassifier() throws Exception {
        String modelPath = "src/main/resources/RandomForest.model";
        return (CostSensitiveClassifier) weka.core.SerializationHelper.read(modelPath);
    }

    /**
     * @param randomForest used algorithm for unseen data
     * @param newData unclassified data in an instances object
     * @return classified instances in arff format
     */
    private Instances classifyNewInstance(CostSensitiveClassifier randomForest, Instances newData) throws Exception {
        // create copy
        Instances labeled = new Instances(newData);
        // label instances
        for (int i = 0; i < newData.numInstances(); i++) {
            double clsLabel = randomForest.classifyInstance(newData.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        return labeled;
    }


    /**
     * @param fileName full path from output file
     * @param res Instances object of the results in arff format
     * @param extension the desired output extension from the user input
     * prints result to commandline if the user didn't give an output file
     * otherwise checks output extension and write the result in the desired
     * output file
     */
    private void writeOutput(String fileName, Instances res, String extension) throws IOException {
        if (extension == null){
            for (int i = 0; i < res.numInstances(); i++){
                boolean heartDisease = res.instance(i).classValue() != 0;
                System.out.println("Patient: " + i + " has heart disease based on the model: " + heartDisease);
            }

        }

        else if (extension.equals("csv")) {
            CSVSaver saver = new CSVSaver();
            saver.setInstances(res);
            saver.setFile(new File(fileName));
            saver.writeBatch();
        }

        else if (extension.equals("arff")){
            FileWriter file = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(String.valueOf(res));
            writer.newLine();
            writer.close();

        }

    }


    public static void main(String[] args) {
        CLHandler handler = new CLHandler(args);
        Preprocessor processor = new Preprocessor();
        WekaRunner runner = new WekaRunner();
        if (args.length == 0){
            System.err.println("No arguments given, please select the desired arff file.");
            handler.formatter.printHelp("WekaRunner",handler.options, true);
        }
        if (handler.cmd.getOptionValue("infile") != null) {
            try {
                Instances newData = runner.loadArff(handler.cmd.getOptionValue("infile"));
                Instances cleanedData = processor.processor(newData);
                Instances result = runner.runModel(cleanedData);
                runner.writeOutput(handler.cmd.getOptionValue("output"), result, handler.extension);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
