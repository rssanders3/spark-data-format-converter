package com.github.rssanders3.spark.data_format_converter

import com.github.rssanders3.spark.data_format_converter.utils.{Reader, Writer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.{LoggerFactory, Logger}


/**
 * Created by robertsanders on 12/1/16.
 */
object Main {

  val LOGGER: Logger = LoggerFactory.getLogger(MainArgs.getClass.getName)

  val APP_NAME: String = "SparkDataFormatConverter"

  def main(args: Array[String]): Unit = {

    if (args.contains("-help") || args.contains("--help")) {
      LOGGER.info(MainArgs.argsUsage)
      System.exit(0)
    }

    val jobArgs = MainArgs.parseJobArgs(args.toList)
    if (jobArgs == null) {
      LOGGER.info(MainArgs.argsUsage)
      System.exit(-1)
    }

    jobArgs.validate()
    LOGGER.info(jobArgs.toString())

    val conf = new SparkConf().setAppName(APP_NAME)
    val sc = new SparkContext(conf)
    val sqlContext = if(jobArgs.useHiveContext()) new HiveContext(sc) else new SQLContext(sc)

    val inputDF = Reader.read(sqlContext, jobArgs)

    Writer.write(sqlContext, inputDF, jobArgs)

  }

}
