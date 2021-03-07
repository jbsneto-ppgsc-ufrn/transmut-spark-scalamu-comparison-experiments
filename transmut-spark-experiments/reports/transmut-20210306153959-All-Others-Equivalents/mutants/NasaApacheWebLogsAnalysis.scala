package example
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object NasaApacheWebLogsAnalysis {
  def parseLogs(line: String) = line.split("\t")(0)
  def isNotHeader(line: String): Boolean = !(line.startsWith("host") && line.contains("bytes"))
  def isNotHeaderHost(host: String) = host != "host"
  def sameHostProblem(firstLogs: RDD[String], secondLogs: RDD[String]): RDD[String] = sys.props.get("CURRENT_MUTANT") match {
    case Some("146") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("147") =>
      val firstHosts: RDD[String] = firstLogs.filter(host => isNotHeaderHost(host))
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.map(parseLogs)
      cleanedHostIntersection
    case Some("148") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.filter(host => isNotHeaderHost(host))
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.map(parseLogs)
      cleanedHostIntersection
    case Some("149") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("150") =>
      val firstHosts: RDD[String] = firstLogs.filter(host => isNotHeaderHost(host))
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("151") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("152") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.filter(host => isNotHeaderHost(host))
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("153") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.map(parseLogs)
      cleanedHostIntersection
    case Some("154") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.map(parseLogs)
      cleanedHostIntersection
    case Some("155") =>
      val firstHosts: RDD[String] = firstLogs
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("156") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("157") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection
      cleanedHostIntersection
    case Some("158") =>
      val firstHosts: RDD[String] = firstLogs.map { (inputParameter: String) => {
        val originalFunction = parseLogs(_)
        val originalValue = originalFunction(inputParameter)
        ""
      } }
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("159") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map { (inputParameter: String) => {
        val originalFunction = parseLogs(_)
        val originalValue = originalFunction(inputParameter)
        ""
      } }
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("160") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection
      cleanedHostIntersection
    case Some("161") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.union(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("162") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.subtract(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("163") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("164") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = secondHosts
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("165") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs).distinct()
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("166") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs).distinct()
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("167") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts).distinct()
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
    case Some("168") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host)).distinct()
      cleanedHostIntersection
    case Some("169") =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter { (inputParameter: String) => {
        val originalFunction = (host => isNotHeaderHost(host))(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      cleanedHostIntersection
    case _ =>
      val firstHosts: RDD[String] = firstLogs.map(parseLogs)
      val secondHosts: RDD[String] = secondLogs.map(parseLogs)
      val intersection: RDD[String] = firstHosts.intersection(secondHosts)
      val cleanedHostIntersection: RDD[String] = intersection.filter(host => isNotHeaderHost(host))
      cleanedHostIntersection
  }
  def unionLogsProblem(firstLogs: RDD[String], secondLogs: RDD[String]): RDD[String] = sys.props.get("CURRENT_MUTANT") match {
    case Some("170") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.filter(line => isNotHeader(line))
      val cleanLogLines: RDD[String] = uniqueLogLines.distinct()
      cleanLogLines
    case Some("171") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.filter(line => isNotHeader(line))
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("172") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.distinct()
      cleanLogLines
    case Some("173") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("174") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines
      cleanLogLines
    case Some("175") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines
      cleanLogLines
    case Some("176") =>
      val aggregatedLogLines: RDD[String] = firstLogs.intersection(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("177") =>
      val aggregatedLogLines: RDD[String] = firstLogs.subtract(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("178") =>
      val aggregatedLogLines: RDD[String] = firstLogs
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("179") =>
      val aggregatedLogLines: RDD[String] = secondLogs
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("180") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("181") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs).distinct()
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
    case Some("182") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line)).distinct()
      cleanLogLines
    case Some("183") =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter { (inputParameter: String) => {
        val originalFunction = (line => isNotHeader(line))(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      cleanLogLines
    case _ =>
      val aggregatedLogLines: RDD[String] = firstLogs.union(secondLogs)
      val uniqueLogLines: RDD[String] = aggregatedLogLines.distinct()
      val cleanLogLines: RDD[String] = uniqueLogLines.filter(line => isNotHeader(line))
      cleanLogLines
  }
}