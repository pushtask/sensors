package com.sensors

trait DataProvider {
  def provide(): List[Iterator[String]]

  def sourcesCount(): Int

}
