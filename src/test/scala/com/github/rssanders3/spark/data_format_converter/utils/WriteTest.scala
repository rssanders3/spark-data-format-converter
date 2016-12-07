package com.github.rssanders3.spark.data_format_converter.utils

import java.io.File

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, GivenWhenThen, Matchers}

/**
 * Created by robertsanders on 12/1/16.
 */
class WriteTest extends FlatSpec with GivenWhenThen with Matchers with BeforeAndAfterAll {

  private val MASTER = "local[2]"
  private val APP_NAME = this.getClass.getSimpleName
  private val TEST_OUTPUT_DIR = "src/test/resources/WriteTest_output"

  private var _sc: SparkContext = _
  private var _sqlContext: SQLContext = _

  def sc = _sc
  def sqlContext = _sqlContext

  val conf: SparkConf = new SparkConf()
    .setMaster(MASTER)
    .setAppName(APP_NAME)

  override def beforeAll(): Unit = {
    super.beforeAll()
    _sc = new SparkContext(conf)
    _sqlContext = new SQLContext(_sc)
    deleteTestOutputDirContents()
  }

  def deleteTestOutputDirContents(): Unit = {
    val deleteTestOutputDir = new File(TEST_OUTPUT_DIR)
    if (deleteTestOutputDir.exists()) {
      deleteTestOutputDir.listFiles().foreach(file => {
        file.delete()
      })
    }
  }

  override def afterAll(): Unit = {
    if (_sc != null) {
      _sc.stop()
      _sc = null
    }

    super.afterAll()
  }

  "Importing as text and exporting as parquet" should "work" in {
    val inputDF = Reader.read(sqlContext, "src/test/resources/text/test1.txt", null, "text")
    assert(inputDF.collect().length > 0)
  }

}
