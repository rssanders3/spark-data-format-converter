package com.github.rssanders3.spark.data_format_converter.utils

import com.github.rssanders3.spark.data_format_converter.MainArgs.JobArgs
import org.apache.spark.sql.{DataFrame, SQLContext}

/**
 * Created by robertsanders on 12/6/16.
 */
object Reader {

  def read(sqlContext: SQLContext, jobArgs: JobArgs): DataFrame = {
    read(sqlContext = sqlContext,
      inputFilePath = jobArgs.inputFilePath,
      inputTableName = jobArgs.inputTableName,
      inputDataType = jobArgs.inputDataType
    )
  }

  def read(sqlContext: SQLContext, inputFilePath: String, inputTableName: String, inputDataType: String): DataFrame = {
    val inputDFReader = sqlContext.read.format(inputDataType)

    var inputDF: DataFrame = null
    if(inputFilePath != null) {
      inputDF = inputDFReader.load(inputFilePath)
    } else if (inputTableName != null) {
      inputDF = inputDFReader.table(inputTableName)
    } else {
      throw new IllegalArgumentException("Input information has not been provided.")
    }

    inputDF
  }

}
