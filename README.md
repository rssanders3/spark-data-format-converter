# spark-data-format-converter
Uses Apache Spark to convert data from one format to another

### Sample Help Command
spark-submit data_format_converter-jar-with-dependencies.jar --help

### Sample Run Command
spark-submit data_format_converter-jar-with-dependencies.jar --inputDataType json --inputFilePath /home/cloudera/input.txt --outputDataType parquet --outputFilePath /home/cloudera/output-dfc-parquet/
