package com.sensors

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalamock.scalatest.MockFactory

class HumidityStatsCollectorSpec extends AnyFlatSpec with MockFactory {

  // setup input
  private val l1 = ("s1,10" :: "s2,88" :: "s1,NaN" :: Nil).iterator
  private val l2 = ("s2,80" :: "s3,NaN" :: "s2,78" :: "s1,98" :: Nil).iterator
  private val inputSources = l1 :: l2 :: Nil

  // setup expected output
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

  private val statsExpected = Map("s1" -> s1, "s2" -> s2, "s3" -> s3)

  // mock data provider
  private val dataProvider = mock[DataProvider]
  (dataProvider.provide _).expects().returns(inputSources)

  // create statsCollector object
  private val statsCollector = new HumidityStatsCollector(dataProvider)


  // do the test
  behavior of "statsCollector"
  it should "return expected output" in {
    statsCollector.collect() shouldBe statsExpected
  }
}
