package example
import java.sql.Date
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.Partitioner
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object JoinQuery {
  case class Ranking(pageURL: String, pageRank: Int, avgDuration: Int)
  def parseRankings(line: String): Ranking = {
    val fields = line.split(',')
    val ranking: Ranking = Ranking(fields(0), fields(1).toInt, fields(2).toInt)
    return ranking
  }
  case class UserVisit(sourceIP: String, destURL: String, visitDate: Date, adRevenue: Float, userAgent: String, countryCode: String, languageCode: String, searchWord: String, duration: Int)
  def parseUserVisits(line: String): UserVisit = {
    val fields = line.split(',')
    val userVisit: UserVisit = UserVisit(fields(0), fields(1), Date.valueOf(fields(2)), fields(3).toFloat, fields(4), fields(5), fields(6), fields(7), fields(8).toInt)
    return userVisit
  }
  def filterUserVisitsDateRange(u: UserVisit) = {
    val date1 = Date.valueOf("1980-01-01")
    val date2 = Date.valueOf("1980-04-01")
    u.visitDate.after(date1) && u.visitDate.before(date2)
  }
  def mapUserVisitToTuple(u: UserVisit) = (u.destURL, u)
  def mapRankingToTuple(r: Ranking) = (r.pageURL, r)
  def mapRankingAndUserVisitJoinToTuple(v: (Ranking, UserVisit)) = (v._2.sourceIP, (v._2.adRevenue, v._1.pageRank))
  def mapAggregation(v: (String, Iterable[(Float, Int)])) = (v._1, v._2.map(_._1).sum, v._2.map(_._2).sum / v._2.map(_._2).size)
  def join(rankingsLines: RDD[String], userVisitsLines: RDD[String]): RDD[(String, Float, Int)] = sys.props.get("CURRENT_MUTANT") match {
    case Some("34") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("35") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation
      results
    case Some("36") =>
      val rankings: RDD[Ranking] = rankingsLines.map { (inputParameter: String) => {
        val originalFunction = parseRankings(_)
        val originalValue = originalFunction(inputParameter)
        null.asInstanceOf[Ranking]
      } }
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("37") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map { (inputParameter: String) => {
        val originalFunction = parseUserVisits(_)
        val originalValue = originalFunction(inputParameter)
        null.asInstanceOf[UserVisit]
      } }
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("38") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("39") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, null.asInstanceOf[UserVisit])
      } }
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("40") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("41") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map { (inputParameter: Ranking) => {
        val originalFunction = mapRankingToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, null.asInstanceOf[Ranking])
      } }
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("42") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("43") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (0f, originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("44") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (1f, originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("45") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (Float.MaxValue, originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("46") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (Float.MinValue, originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("47") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (-originalValue._2._1, originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("48") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 0))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("49") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 1))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("50") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MaxValue))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("51") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MinValue))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("52") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map { (inputParameter: (Ranking, UserVisit)) => {
        val originalFunction = mapRankingAndUserVisitJoinToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, -originalValue._2._2))
      } }
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("53") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("54") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings).distinct()
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("55") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits).distinct()
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("56") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange).distinct()
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("57") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple).distinct()
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("58") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple).distinct()
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("59") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV).distinct()
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("60") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values.distinct()
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("61") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple).distinct()
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("62") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey().distinct()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("63") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation).distinct()
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("64") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false).distinct()
      results
    case Some("65") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.leftOuterJoin(subqueryUV).map(tuple => (tuple._1, (tuple._2._1, tuple._2._2.getOrElse(null.asInstanceOf[UserVisit]))))
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("66") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.rightOuterJoin(subqueryUV).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Ranking]), tuple._2._2)))
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("67") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.fullOuterJoin(subqueryUV).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Ranking]), tuple._2._2.getOrElse(null.asInstanceOf[UserVisit]))))
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("68") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation
      results
    case Some("69") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter { (inputParameter: UserVisit) => {
        val originalFunction = filterUserVisitsDateRange(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
    case Some("70") =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, true)
      results
    case _ =>
      val rankings: RDD[Ranking] = rankingsLines.map(parseRankings)
      val userVisits: RDD[UserVisit] = userVisitsLines.map(parseUserVisits)
      val filteredUV: RDD[UserVisit] = userVisits.filter(filterUserVisitsDateRange)
      val subqueryUV: RDD[(String, UserVisit)] = filteredUV.map(mapUserVisitToTuple)
      val subqueryR: RDD[(String, Ranking)] = rankings.map(mapRankingToTuple)
      val subqueryJoin: RDD[(String, (Ranking, UserVisit))] = subqueryR.join(subqueryUV)
      val subqueryJoinValues: RDD[(Ranking, UserVisit)] = subqueryJoin.values
      val subquerySelect: RDD[(String, (Float, Int))] = subqueryJoinValues.map(mapRankingAndUserVisitJoinToTuple)
      val subqueryGroup: RDD[(String, Iterable[(Float, Int)])] = subquerySelect.groupByKey()
      val subqueryAggregation: RDD[(String, Float, Int)] = subqueryGroup.map(mapAggregation)
      val results: RDD[(String, Float, Int)] = subqueryAggregation.sortBy(_._2, false)
      results
  }
}