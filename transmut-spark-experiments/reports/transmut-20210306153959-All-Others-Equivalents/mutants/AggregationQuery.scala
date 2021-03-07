package example
import java.sql.Date
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object AggregationQuery {
  case class UserVisit(sourceIP: String, destURL: String, visitDate: Date, adRevenue: Float, userAgent: String, countryCode: String, languageCode: String, searchWord: String, duration: Int)
  def parseUserVisits(line: String): UserVisit = {
    val fields = line.split(',')
    val userVisit: UserVisit = UserVisit(fields(0), fields(1), Date.valueOf(fields(2)), fields(3).toFloat, fields(4), fields(5), fields(6), fields(7), fields(8).toInt)
    return userVisit
  }
  def mapUserVisitToTuple(u: UserVisit) = (u.sourceIP.substring(0, 7), u.adRevenue)
  def aggregation(input: RDD[String]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("1") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples
      results
    case Some("2") =>
      val userVisits: RDD[UserVisit] = input.map { (inputParameter: String) => {
        val originalFunction = parseUserVisits(_)
        val originalValue = originalFunction(inputParameter)
        null.asInstanceOf[UserVisit]
      } }
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("3") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("4") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0f)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("5") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1f)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("6") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Float.MaxValue)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("7") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Float.MinValue)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("8") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("9") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits).distinct()
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("10") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple).distinct()
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
    case Some("11") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }.distinct()
      results
    case Some("12") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (firstParameter: Float, secondParameter: Float) => firstParameter }
      results
    case Some("13") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (firstParameter: Float, secondParameter: Float) => secondParameter }
      results
    case Some("14") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (firstParameter: Float, secondParameter: Float) => {
        val originalFunction = ((x: Float, y: Float) => x + y)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      results
    case Some("15") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (firstParameter: Float, secondParameter: Float) => {
        val originalFunction = ((x: Float, y: Float) => x + y)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      results
    case Some("16") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (firstParameter: Float, secondParameter: Float) => {
        val originalFunction = ((x: Float, y: Float) => x + y)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      results
    case _ =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userVisitsTuples: RDD[(String, Float)] = userVisits.map(mapUserVisitToTuple)
      val results: RDD[(String, Float)] = userVisitsTuples.reduceByKey { (x: Float, y: Float) => x + y }
      results
  }
}