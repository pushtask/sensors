package com.sensors

class ToTerminalReportWriter(report: List[String]) extends ReportWriter {
  override def write(): Unit = {
    report.foreach(println)
  }

}
