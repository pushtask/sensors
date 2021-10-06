package com.sensors

trait StatsCollector {
  def collect(): Map[String, StatsRecord]

  val emptyM = Map[String, StatsRecord]().withDefaultValue(StatsRecord())
}
