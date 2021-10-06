package com.sensors

import scala.util.{Failure, Success, Try}

class HumidityStatsCollector(dataProvider: DataProvider) extends StatsCollector {
  private def addMeasurementToStats(m: Map[String, StatsRecord], line: String) = {
    val Array(k, v) = line.split(',')
    val vi = Try(v.toInt) // TODO value must be in range 0to100, add validation

    vi match {
      case Success(i) => m + (k -> StatsRecord(
        count = m(k).count + 1,
        failCount = m(k).failCount,
        maxV = m(k).maxV.max(i),
        minV = m(k).minV.min(i),
        sumV = m(k).sumV + i)
        )
      case Failure(s) => m + (k -> StatsRecord(
        count = m(k).count,
        failCount = m(k).failCount + 1,
        maxV = m(k).maxV,
        minV = m(k).minV,
        sumV = m(k).sumV)
        )
    }
  }

  private def getStatsFromSource(lines: Iterator[String]): Map[String, StatsRecord] = {
    lines
      .foldLeft(emptyM)((m, line) => addMeasurementToStats(m, line))
  }

  private def combineReports(m1: Map[String, StatsRecord], m2: Map[String, StatsRecord]) = {
    m2.foldLeft(m1)((total, v) => {
      total + (v._1 -> StatsRecord(
        total(v._1).count + v._2.count,
        total(v._1).failCount + v._2.failCount,
        total(v._1).maxV.max(v._2.maxV),
        total(v._1).minV.min(v._2.minV),
        total(v._1).sumV + v._2.sumV)
        )
    })
  }

  // must be run as final transformation, after data collected from all sources
  private def calcAvg(k: String, v: StatsRecord)  = {
      k -> StatsRecord(v.count, v.failCount, v.maxV, v.minV, v.sumV, if (v.count != 0) Math.round(v.sumV / v.count) else 0)
  }

  override def collect(): Map[String, StatsRecord] = {
    dataProvider
      .provide()
      .map(getStatsFromSource)
      .fold(emptyM)((total, m) => combineReports(total, m))
      .map(v => calcAvg(v._1, v._2))
  }
}
