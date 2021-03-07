package example
import java.sql.Date
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object DistinctUserVisitsPerPage {
  case class UserVisit(sourceIP: String, destURL: String, visitDate: Date, adRevenue: Float, userAgent: String, countryCode: String, languageCode: String, searchWord: String, duration: Int)
  def parseUserVisits(line: String): UserVisit = {
    val fields = line.split(',')
    val userVisit: UserVisit = UserVisit(fields(0), fields(1), Date.valueOf(fields(2)), fields(3).toFloat, fields(4), fields(5), fields(6), fields(7), fields(8).toInt)
    return userVisit
  }
  def mapUserVisitToTuple(u: UserVisit) = (u.destURL, u.sourceIP)
  def mapSourceIPToSet(u: (String, String)) = (u._1, Set(u._2))
  def setUnion(a: Set[String], b: Set[String]) = a ++ b
  def distinctUserVisitsPerPage(input: RDD[String]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("17") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess
      distinctSites
    case Some("18") =>
      val userVisits: RDD[UserVisit] = input.map { (inputParameter: String) => {
        val originalFunction = parseUserVisits(_)
        val originalValue = originalFunction(inputParameter)
        null.asInstanceOf[UserVisit]
      } }
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("19") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("20") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map { (inputParameter: UserVisit) => {
        val originalFunction = mapUserVisitToTuple(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, "")
      } }
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("21") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map { (inputParameter: (String, String)) => {
        val originalFunction = mapSourceIPToSet(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("22") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map { (inputParameter: (String, String)) => {
        val originalFunction = mapSourceIPToSet(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Set[String](originalValue._2.head))
      } }
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("23") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map { (inputParameter: (String, String)) => {
        val originalFunction = mapSourceIPToSet(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, originalValue._2.tail)
      } }
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("24") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map { (inputParameter: (String, String)) => {
        val originalFunction = mapSourceIPToSet(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Set[String]())
      } }
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("25") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits).distinct()
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("26") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple).distinct()
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("27") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet).distinct()
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
    case Some("28") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion).distinct()
      distinctSites
    case Some("29") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey { (firstParameter: Set[String], secondParameter: Set[String]) => firstParameter }
      distinctSites
    case Some("30") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey { (firstParameter: Set[String], secondParameter: Set[String]) => secondParameter }
      distinctSites
    case Some("31") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey { (firstParameter: Set[String], secondParameter: Set[String]) => {
        val originalFunction = setUnion(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      distinctSites
    case Some("32") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey { (firstParameter: Set[String], secondParameter: Set[String]) => {
        val originalFunction = setUnion(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      distinctSites
    case Some("33") =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey { (firstParameter: Set[String], secondParameter: Set[String]) => {
        val originalFunction = setUnion(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      distinctSites
    case _ =>
      val userVisits: RDD[UserVisit] = input.map(parseUserVisits)
      val userAccesses: RDD[(String, String)] = userVisits.map(mapUserVisitToTuple)
      val mapedUserAccess: RDD[(String, Set[String])] = userAccesses.map(mapSourceIPToSet)
      val distinctSites: RDD[(String, Set[String])] = mapedUserAccess.reduceByKey(setUnion)
      distinctSites
  }
}