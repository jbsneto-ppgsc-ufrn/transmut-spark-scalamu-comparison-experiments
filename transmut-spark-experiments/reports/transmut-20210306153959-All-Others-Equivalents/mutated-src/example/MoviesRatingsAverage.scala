package example
import java.nio.charset.CodingErrorAction
import scala.io.Codec
import scala.io.Source
import scala.util.Try
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
object MoviesRatingsAverage {
  def parseRatings(r: String) = {
    if (r.split(",").length > 2 && Try(r.split(",")(1).toInt).isSuccess && Try(r.split(",")(2).toDouble).isSuccess) {
      val rating = r.split(",")
      Some((rating(1).toInt, (rating(2).toDouble, 1.0d)))
    } else None
  }
  def loadMovieNames(lines: Iterator[String]): Map[Int, String] = {
    var movieNames: Map[Int, String] = Map()
    for (line <- lines) {
      var movie = line.split(",")
      if (movie.length > 1 && Try(movie(0).toInt).isSuccess) {
        movieNames += movie(0).toInt -> movie(1)
      }
    }
    return movieNames
  }
  def loadMovieNames(): Map[Int, String] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val lines = Source.fromFile("./resources/movies.csv").getLines()
    loadMovieNames(lines)
  }
  def aggregateDoubleTuple(x: (Double, Double), y: (Double, Double)): (Double, Double) = (x._1 + y._1, x._2 + y._2)
  def mapDoubleTupleDivide(x: (Double, Double)) = x._1 / x._2
  def mapRatingsAverageToCSV(x: (Int, Double), movieNames: Broadcast[Map[Int, String]]) = x._1.toString + "," + movieNames.value(x._1).toString + "," + x._2.toString
  def moviesRatingsAverage(inputRDD: RDD[String], movieNames: Broadcast[Map[Int, String]]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("71") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("72") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("73") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("74") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("75") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("76") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        List[(Int, (Double, Double))]()
      } }
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("77") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (inputParameter: (Int, Double)) => {
        val originalFunction = ((x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames))(_)
        val originalValue = originalFunction(inputParameter)
        ""
      } }
      resultsCVS
    case Some("78") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings).distinct()
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("79") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple).distinct()
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("80") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide).distinct()
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("81") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false).distinct()
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("82") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }.distinct()
      resultsCVS
    case Some("83") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey { (firstParameter: (Double, Double), secondParameter: (Double, Double)) => firstParameter }
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("84") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey { (firstParameter: (Double, Double), secondParameter: (Double, Double)) => secondParameter }
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("85") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey { (firstParameter: (Double, Double), secondParameter: (Double, Double)) => {
        val originalFunction = aggregateDoubleTuple(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("86") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey { (firstParameter: (Double, Double), secondParameter: (Double, Double)) => {
        val originalFunction = aggregateDoubleTuple(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("87") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey { (firstParameter: (Double, Double), secondParameter: (Double, Double)) => {
        val originalFunction = aggregateDoubleTuple(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("88") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case Some("89") =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(true)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
    case _ =>
      val ratings: RDD[(Int, (Double, Double))] = inputRDD.flatMap(parseRatings)
      val ratingsSum: RDD[(Int, (Double, Double))] = ratings.reduceByKey(aggregateDoubleTuple)
      val ratingsAverage: RDD[(Int, Double)] = ratingsSum.mapValues(mapDoubleTupleDivide)
      val ratingsAverageSorted: RDD[(Int, Double)] = ratingsAverage.sortByKey(false)
      val resultsCVS: RDD[String] = ratingsAverageSorted.map { (x: (Int, Double)) => mapRatingsAverageToCSV(x, movieNames) }
      resultsCVS
  }
}