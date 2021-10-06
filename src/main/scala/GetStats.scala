package com.sensors

/*TODO
   move configuration to config file
   add logger
   exceptions:
        1. file not found/can't access
        2. was not able to split line
        3. OOM?
*/

object GetStats extends App {
  // configuration
  val csvDropLines = 1
  val csvFilenamePattern = """^g.*\.csv$""" // "g1.csv, g2.csv ..."
  val reportFilesLocation = "./resources/"

  // setup source
  val dataProvider = new CsvDataProvider(reportFilesLocation, csvFilenamePattern, csvDropLines)
  val statsCollector = new HumidityStatsCollector(dataProvider)

  // Load stats from the source
  val stats = statsCollector.collect()

  // setup reporter
  val reporter = new HumidityStatsReporter(stats, dataProvider.sourcesCount())

  // prepare report
  val report = reporter.report()

  // setup output
  val reportWriter = new ToTerminalReportWriter(report)

  // output
  reportWriter.write()
}
