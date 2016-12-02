package com.github.rssanders3.spark.data_format_converter

import java.security.InvalidParameterException
import java.util

import org.apache.spark.sql.SaveMode


/**
 * Created by robertsanders on 12/1/16.
 */
object MainArgs {

  /**
   * spark-submit data_format_converter-jar-with-dependencies.jar --inputDataType json --inputFilePath /home/cloudera/input.txt --outputDataType parquet --outputFilePath /home/cloudera/output-dfc-parquet/
   */

  val POSSIBLE_DATA_TYPES = List("text", "json", "orc", "csv", "parquet")

  val DEFAULT_INPUT_DATA_TYPE = "text"
  val DEFAULT_SAVE_MODE = SaveMode.Overwrite.toString
  val DEFAULT_OUTPUT_DATA_TYPE = "text"

  val argsUsage = s"MainJobArgs Usage: \n" +
    s"\t[--inputDataType <string> (required=false, default=$DEFAULT_INPUT_DATA_TYPE, description=Input Data Type (Possible Values: $POSSIBLE_DATA_TYPES))]\n" +
    s"\t[--inputTableName <string> (required=true (or --inputFilePath), description=Input Table Name in Hive)]\n" +
    s"\t[--inputFilePath <string> (required=true (or --inputTableName), description=Input File Path in HDFS)]\n" +
    s"\t[--saveMode <string> (required=false, default=$DEFAULT_SAVE_MODE, description=Mode to use when Saving (Possible Values: ${SaveMode.values().toList}))]\n" +
    s"\t[--outputDataType <string> (required=false, default=$DEFAULT_OUTPUT_DATA_TYPE, description=Output Data Type (Possible Values: $POSSIBLE_DATA_TYPES))]\n" +
    s"\t[--outputTableName <string> (required=true (or --outputFilePath), description=Output Table Name in Hive]\n" +
    s"\t[--outputFilePath <string> (required=true (or --outputTableName), description=Output File Path in HDFS)]\n" +
    s"\n"

  case class JobArgs(inputDataType: String = DEFAULT_INPUT_DATA_TYPE,
                     inputTableName: String = null,
                     inputFilePath: String = null,
                     saveMode: String = DEFAULT_SAVE_MODE,
                     outputDataType: String = DEFAULT_OUTPUT_DATA_TYPE,
                     outputTableName: String = null,
                     outputFilePath: String = null) {

    override def toString(): String = {
      s"MainJobArgs(\n" +
        s"inputDataType=$inputDataType, \n" +
        s"inputTableName=$inputTableName, \n" +
        s"inputFilePath=$inputFilePath, \n" +
        s"saveMode=$saveMode, \n" +
        s"outputDataType=$outputDataType, \n" +
        s"outputTableName=$outputTableName, \n" +
        s"outputFilePath=$outputFilePath, \n" +
        s")"
    }

    def validate(): Unit = {
      val invalidMessageList = new util.ArrayList[String]()

      if(inputDataType == null) {
        invalidMessageList.add("--inputDataType cannot be null")
      }

      if (inputTableName == null && inputFilePath == null) {
        invalidMessageList.add("--inputTableName or --inputFilePath arguments need to be provided. Select one.")
      } else if(inputTableName != null && inputFilePath != null) {
        invalidMessageList.add("--inputTableName and --inputFilePath arguments cannot both be provided. Select one.")
      }

      if(getSaveMode() == null) {
        invalidMessageList.add(s"--saveMode argument value '$saveMode' is not a valid value. Possible Values: ${SaveMode.values().toList}.")
      }

      if(outputDataType == null) {
        invalidMessageList.add("--outputDataType cannot be null")
      }

      if (outputTableName == null && outputFilePath == null) {
        invalidMessageList.add("--outputTableName or --outputFilePath arguments need to be provided. Select one.")
      } else if(outputTableName != null && outputFilePath != null) {
        invalidMessageList.add("--outputTableName and --outputFilePath arguments cannot both be provided. Select one.")
      }

      if (invalidMessageList.size() > 0) {
        throw new InvalidParameterException("Invalid Arguments: " + invalidMessageList)
      }
    }

    def getSaveMode(): SaveMode = {
      try {
        return SaveMode.valueOf(saveMode)
      } catch {
        case e: Exception => return null
      }
    }

    def useHiveContext(): Boolean = {
      inputTableName != null || outputTableName != null
    }

  }

  /**
   * Parses the input arguments to
   *
   * @param args
   * @param jobArgs
   * @return JobArgs
   */
  def parseJobArgs(args: List[String], jobArgs: JobArgs = JobArgs()): JobArgs = {
    args.toList match {
      case Nil => jobArgs
      case "--inputDataType" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(inputDataType = value))
      case "--inputTableName" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(inputTableName = value))
      case "--inputFilePath" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(inputFilePath = value))
      case "--saveMode" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(saveMode = value))
      case "--outputDataType" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(outputDataType = value))
      case "--outputTableName" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(outputTableName = value))
      case "--outputFilePath" :: value :: otherArgs => parseJobArgs(otherArgs, jobArgs.copy(outputFilePath = value))
      case option :: tail => println("Unknown option " + option); return null;
    }
  }


}
