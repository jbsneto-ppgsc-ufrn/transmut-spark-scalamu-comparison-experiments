package example
import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.rdd.RDD
object ScanQuery {
  case class Ranking(pageURL: String, pageRank: Int, avgDuration: Int)
  def parseRankings(line: String): Ranking = {
    val fields = line.split(',')
    val ranking: Ranking = Ranking(fields(0), fields(1).toInt, fields(2).toInt)
    return ranking
  }
  def filterRankings(r: Ranking) = r.pageRank > 300
  def mapRankingToTuple(r: Ranking) = (r.pageURL, r.pageRank)
  def scan(input: RDD[String]): RDD[(String, Int)] = sys.props.get("CURRENT_MUTANT") match {
    case Some("216") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case Some("217") =>
      val rankings = input.map { (inputParameter: String) => {
        val originalFunction = parseRankings(_)
        val originalValue = originalFunction(inputParameter)
        null.asInstanceOf[Ranking]
      } }
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case Some("218") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      results
    case Some("219") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      results
    case Some("220") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      results
    case Some("221") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      results
    case Some("222") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      results
    case Some("223") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      results
    case Some("224") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case Some("225") =>
      val rankings = input.map(parseRankings).distinct()
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case Some("226") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings).distinct()
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case Some("227") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple).distinct()
      results
    case Some("228") =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter { (inputParameter: Ranking) => {
        val originalFunction = filterRankings(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
    case _ =>
      val rankings = input.map(parseRankings)
      val filteredRankings: RDD[Ranking] = rankings.filter(filterRankings)
      val results: RDD[(String, Int)] = filteredRankings.map(mapRankingToTuple)
      results
  }
}