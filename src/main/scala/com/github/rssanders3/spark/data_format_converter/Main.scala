package com.github.rssanders3.spark.data_format_converter

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
 * Created by robertsanders on 12/1/16.
 */
object Main {

  val APP_NAME: String = "SparkDataFormatConverter"

  def main(args: Array[String]): Unit = {

    if (args.contains("-help") || args.contains("--help")) {
      println(MainArgs.argsUsage)
      System.exit(-1)
    }

    val jobArgs = MainArgs.parseJobArgs(args.toList)
    if (jobArgs == null) {
      println(MainArgs.argsUsage)
      System.exit(-1)
    }

    jobArgs.validate()
    println(jobArgs)

    val conf = new SparkConf().setAppName(APP_NAME)
    val sc = new SparkContext(conf)

    val sqlContext = if(jobArgs.useHiveContext()) new HiveContext(sc) else new SQLContext(sc)

    val inputDFReader = sqlContext.read.format(jobArgs.inputDataType)

    var inputDF: DataFrame = null
    if(jobArgs.inputFilePath != null) {
      inputDF = inputDFReader.load(jobArgs.inputFilePath)
    } else if (jobArgs.inputTableName != null) {
      inputDF = inputDFReader.table(jobArgs.inputTableName)
    } else {
      throw new IllegalArgumentException("Input information has not been provided.")
    }

    val outputDFWriter = inputDF.write.format(jobArgs.outputDataType).mode(jobArgs.getSaveMode())
    if(jobArgs.outputFilePath != null) {
      outputDFWriter.save(jobArgs.outputFilePath)
    } else if(jobArgs.outputTableName != null) {
      outputDFWriter.saveAsTable(jobArgs.outputTableName)
    } else {
      throw new IllegalArgumentException("Output information has not been provided")
    }

  }

}
