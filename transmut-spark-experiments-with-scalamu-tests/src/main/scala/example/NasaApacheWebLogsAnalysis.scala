package example

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

// Reference: https://github.com/jleetutorial/scala-spark-tutorial/blob/master/src/main/scala/com/sparkTutorial/rdd/nasaApacheWebLogs/SameHostsSolution.scala
// Reference: https://github.com/jleetutorial/scala-spark-tutorial/blob/master/src/main/scala/com/sparkTutorial/rdd/nasaApacheWebLogs/UnionLogsSolution.scala
object NasaApacheWebLogsAnalysis {

  def parseLogs(line: String) = line.split("\t")(0)

  def isNotHeader(line: String): Boolean = !(line.startsWith("host") && line.contains("bytes"))

  def isNotHeaderHost(host: String) = host != "host"

  def sameHostProblem(firstLogs: RDD[String], secondLogs: RDD[String]): RDD[String] = {
    val firstHosts: RDD[String] = firstLogs.map(parseLogs)
    val secondHosts: RDD[String] = secondLogs.map(parseLogs)
    val intersection: RDD[String] = firstHosts.intersection(secondHosts)
    val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
    cleanedHostIntersection
  }

  def unionLogsProblem(firstLogs: RDD[String], secondLogs: RDD[String]): RDD[String] = {
    val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
    val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
    val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
    cleanLogLines
  }

}
