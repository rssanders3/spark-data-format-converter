package com.github.rssanders3.spark.data_format_converter.common

import java.io.File

import org.apache.commons.io.FileUtils

/**
 * Created by robertsanders on 12/7/16.
 */
object TestUtilFunctions {

  def deleteTestOutputDir(testOutputDir: String): Unit = {
    FileUtils.deleteDirectory(new File(testOutputDir))
  }

  def deleteTestOutputDirContents(testOutputDir: String): Unit = {
    val deleteTestOutputDir = new File(testOutputDir)
    if (deleteTestOutputDir.exists()) {
      deleteTestOutputDir.listFiles().foreach(file => {
        FileUtils.deleteDirectory(file)
      })
    }
  }

}
