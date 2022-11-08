import weka.core.Instances;

import java.util.Arrays;

public class Preprocessor {

    /**
     * @param newData unclassified data in an instances object
     * @return cleaned data
     * Same preprocessing step performed just like in the logbook
     */
    public Instances processor(Instances newData) {
        int RestingBPIndex = 2;
        int CholesterolIndex = 3;
        double medianBP = calculateMedian(newData, RestingBPIndex);
        double medianCholesterol = calculateMedian(newData, CholesterolIndex);
        for (int i = 0; i < newData.numInstances(); i++) {
            if (newData.instance(i).value(RestingBPIndex) <= 0) {
                newData.instance(i).setValue(RestingBPIndex, medianBP);
            }
            if (newData.instance(i).value(CholesterolIndex) <= 0) {
                newData.instance(i).setValue(CholesterolIndex, medianCholesterol);
            }
        }
        return newData;
    }

    /**
     * @param newData unclassified data in an instances object
     * @param columnIndex index number of the desired column for preprocessing
     * @return median of the preprocessed column
     */
    static double calculateMedian(Instances newData, int columnIndex) {
        double[] values = new double[newData.numInstances()];
        for (int i = 0; i < newData.numInstances(); i++) {
            if (newData.instance(i).value(columnIndex) > 0){
                values[i] = newData.instance(i).value(columnIndex);

            }
        }
        Arrays.sort(values);
        double median;
        // get count of scores
        int totalValues = values.length;
        // check if total number of scores is even
        if (totalValues % 2 == 0) {
            double sumOfMiddleElements = values[totalValues / 2] +
                    values[totalValues / 2 - 1];
            // calculate average of middle elements
            median = sumOfMiddleElements / 2;
        } else {
            // get the middle element
            median = values[values.length / 2];
        }
        return median;
    }
}
