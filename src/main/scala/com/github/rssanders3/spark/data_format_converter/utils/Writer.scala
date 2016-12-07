package com.github.rssanders3.spark.data_format_converter.utils

import com.github.rssanders3.spark.data_format_converter.MainArgs.JobArgs
import org.apache.spark.sql.{SaveMode, DataFrame, SQLContext}

/**
 * Created by robertsanders on 12/6/16.
 */
object Writer {

  def write(sqlContext: SQLContext, inputDF: DataFrame, jobArgs: JobArgs): Unit = {
    write(sqlContext = sqlContext,
      inputDF = inputDF,
      outputDataType = jobArgs.outputDataType,
      outputFilePath = jobArgs.outputFilePath,
      outputTableName = jobArgs.outputTableName,
      saveMode = jobArgs.getSaveMode()
    )
  }

  def write(sqlContext: SQLContext, inputDF: DataFrame, outputDataType: String, outputFilePath: String, outputTableName: String, saveMode: SaveMode = SaveMode.Overwrite): Unit = {
    var outputDFWriter = inputDF.write.format(outputDataType)
    if(saveMode != null) {
      outputDFWriter = outputDFWriter.mode(saveMode)
    }

    if(outputFilePath != null) {
      outputDFWriter.save(outputFilePath)
    } else if(outputTableName != null) {
      outputDFWriter.saveAsTable(outputTableName)
    } else {
      throw new IllegalArgumentException("Output information has not been provided")
    }

  }


}
