package com.sensors

import java.io.File
import scala.io.BufferedSource

// TODO not sure if source is closed once last record is returned
// TODO not sure how to explicitly force source to be closed (for example because of an exception) by the class consumer
// TODO not resilient, ideally should be capable of retries
class CsvDataProvider(val dirPath: String, val fileNamesPattern: String, val dropLines: Int) extends DataProvider {
  override def provide(): List[Iterator[String]] = getListOfFilesSources.map(s => s.getLines.drop(dropLines))

  override def sourcesCount(): Int = getListOfFilesSources.length

  private val getListOfFilesSources: List[BufferedSource] = {
    val regex = fileNamesPattern.r
    val dir = new File(dirPath)
    if (dir.exists() && dir.isDirectory) {
      dir.listFiles.filter(_.isFile)
        .filter(n => regex.findFirstIn(n.getName).isDefined)
        .map(_.getPath).toList
        .map(io.Source.fromFile)
    } else {
      List[BufferedSource]() //TODO add log warning
    }
  }
}
