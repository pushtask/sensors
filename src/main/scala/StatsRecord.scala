package com.sensors

// TODO not ideal to have actual values by default instead of nulls
case class StatsRecord(
                        count: Long = 0L,
                        failCount: Long = 0L,
                        maxV: Int = -1,
                        minV: Int = 101,
                        sumV: Long = 0,
                        avgV: Int = 0
                      )
