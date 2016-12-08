package com.github.rssanders3.spark.data_format_converter.utils

import java.io.File

import com.github.rssanders3.spark.data_format_converter.common.TestUtilFunctions
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, GivenWhenThen, Matchers}

/**
 * Created by robertsanders on 12/1/16.
 */
class WriteTest extends FlatSpec with GivenWhenThen with Matchers with BeforeAndAfterAll {

  private val MASTER = "local[2]"
  private val APP_NAME = this.getClass.getSimpleName
  private val TEST_OUTPUT_DIR = "src/test/resources/test_output/WriteTest"

  private var _sc: SparkContext = _
  private var _sqlContext: SQLContext = _

  def sc = _sc
  def sqlContext = _sqlContext

  val conf: SparkConf = new SparkConf()
    .setMaster(MASTER)
    .setAppName(APP_NAME)

  TestUtilFunctions.deleteTestOutputDirContents(TEST_OUTPUT_DIR)

  override def beforeAll(): Unit = {
    super.beforeAll()
    _sc = new SparkContext(conf)
    _sqlContext = new SQLContext(_sc)
  }

  override def afterAll(): Unit = {
    if (_sc != null) {
      _sc.stop()
      _sc = null
    }
    super.afterAll()
  }

  "Importing as text and exporting as parquet" should "work" in {
//    val inputList: java.util.List[WriteTestObject] = new java.util.ArrayList[WriteTestObject]()
//    inputList.add(new WriteTestObject("key1", "value1"))
//    val inputDF = sqlContext.createDataFrame(inputList, WriteTestObject.getClass)
    val inputDF = sqlContext.read.text("src/test/resources/text/test1.txt")
    val outputDir = TEST_OUTPUT_DIR + "/text_to_parquet"
    Writer.write(sqlContext, inputDF, "parquet", outputDir, null, SaveMode.ErrorIfExists)
    assert(new File(outputDir).exists())
  }

}
