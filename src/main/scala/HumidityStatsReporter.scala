package com.sensors

class HumidityStatsReporter(dataMap: Map[String, StatsRecord], sourcesProcessed: Int) extends StatsReporter {

  private def getReportLineFromStats(k:String, v: StatsRecord):String = {
    if (v.count == 0) {
      s"${k},NaN,NaN,NaN"
    }else {
      s"${k},${v.minV},${v.avgV},${v.maxV}"
    }
  }

  override def report(): List[String] = {
    // TODO split on functions to make it testable

    val measurementsCount = dataMap.foldLeft(0L)((total, v) => v._2.count + total)
    val failedMeasurementsCount = dataMap.foldLeft(0L)(_ + _._2.failCount)
    val mSorted = dataMap.toSeq.sortWith((v1, v2) => v1._2.avgV > v2._2.avgV)


    s"Num of processed files: $sourcesProcessed" ::
      s"Num of processed measurements: $measurementsCount" ::
      s"Num of failed measurements: $failedMeasurementsCount" ::
      "" ::
      "Sensors with highest avg humidity:" ::
      "" ::
      "sensor-id,min,avg,max" ::
      mSorted.map(v => getReportLineFromStats(v._1,v._2)).toList
  }
}
