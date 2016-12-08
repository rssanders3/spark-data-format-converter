package com.github.rssanders3.spark.data_format_converter.utils

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, GivenWhenThen, Matchers}

/**
 * Created by robertsanders on 12/1/16.
 */
class ReaderTest extends FlatSpec with GivenWhenThen with Matchers with BeforeAndAfterAll {

  private val MASTER = "local[2]"
  private val APP_NAME = this.getClass.getSimpleName
  private val TEST_OUTPUT_DIR = "src/test/resources/test_output"

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
  }

  override def afterAll(): Unit = {
    if (_sc != null) {
      _sc.stop()
      _sc = null
    }

    super.afterAll()
  }

  "Importing as text " should "work" in {
    val inputDF = Reader.read(sqlContext, "src/test/resources/text/test1.txt", null, "text")
    assert(inputDF.collect().length > 0)
  }

}
