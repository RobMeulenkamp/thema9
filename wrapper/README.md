# Weka Wrapper

The tool is designed to predict the risk of heart disease for patients. It accepts an attribute-relation file format (arff) with unclassified instances and uses the exported Weka model RandomForest. 

------------------------------------------------------------------------



## Arguments

Short name | Long name | Description |  Example
--- | --- | --- | --- |
-f | -infile | Parse instances from arff file and classify them | -f./data/test_heart.arff
-o | -output | Writes the result to csv, arff file or prints to command line depending on the wishes of the user | -o./data/output.arff


## Example usage
Required version java  16 SDK.
```
java -jar src\main\java\WekaRunner.java -f./data/test_heart.arff -o./data/output.csv
```

If you need help, run the program without arguments and help menu will be printed to the command line



## Extra information
data contains 3 example files:

**test_heart.arff** Test data which can be used for classifying 

**output.arff** Example arff output file

**output.csv** Example csv output file

## Contact
- l.r.meulenkamp@st.hanze.nl
- L.R. Meulenkamp
