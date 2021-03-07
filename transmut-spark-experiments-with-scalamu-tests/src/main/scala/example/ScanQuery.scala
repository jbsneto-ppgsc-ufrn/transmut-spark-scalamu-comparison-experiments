package example

import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.rdd.RDD

/*
 * 1. Scan Query
 * 
 * SELECT pageURL, pageRank FROM rankings WHERE pageRank > X
 * 
 * This query scans and filters the dataset and stores the results.
 */
object ScanQuery {

  /*
   * Rankings
   * 
   * Lists websites and their page rank	
   * 
   * pageURL VARCHAR(300)
   * pageRank INT
   * avgDuration INT
   */
  case class Ranking(pageURL: String, pageRank: Int, avgDuration: Int)

  def parseRankings(line: String): Ranking = {
    val fields = line.split(',')
    val ranking: Ranking = Ranking(fields(0), fields(1).toInt, fields(2).toInt)
    return ranking
  }

  def filterRankings(r: Ranking) = r.pageRank > 300
  
  def mapRankingToTuple(r: Ranking) = (r.pageURL, r.pageRank)

  def scan(input: RDD[String]): RDD[(String, Int)] = {
    val rankings = input.map(parseRankings)
    val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
    val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
    results
  }

}