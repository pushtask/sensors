package com.sensors

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class HumidityStatsReporterSpec extends AnyFlatSpec {

  // setup input
  private val sourcesCount = 2

  private val s1 = StatsRecord(
    count = 2,
    failCount = 1,
    maxV = 98,
    minV = 10,
    sumV = 108,
    avgV = 54)

  private val s2 = StatsRecord(
    count = 3,
    failCount = 0,
    maxV = 88,
    minV = 78,
    sumV = 246,
    avgV = 82)

  private val s3 = StatsRecord(
    count = 0,
    failCount = 1,
    maxV = -1,
    minV = 101,
    sumV = 0,
    avgV = 0)

  private val stats = Map("s1" -> s1, "s2" -> s2, "s3" -> s3)

  // setup reporter
  private val reporter = new HumidityStatsReporter(stats, sourcesCount)

  // expected output
  private val reportExpected =
    "Num of processed files: 2" ::
      "Num of processed measurements: 5" ::
      "Num of failed measurements: 2" ::
      "" ::
      "Sensors with highest avg humidity:" ::
      "" ::
      "sensor-id,min,avg,max" ::
      "s2,78,82,88" ::
      "s1,10,54,98" ::
      "s3,NaN,NaN,NaN" :: Nil


  // do the test
  behavior of "HumidityStatsReporter"
  it should "return expected report" in {
    reporter.report() shouldBe reportExpected
  }

}
