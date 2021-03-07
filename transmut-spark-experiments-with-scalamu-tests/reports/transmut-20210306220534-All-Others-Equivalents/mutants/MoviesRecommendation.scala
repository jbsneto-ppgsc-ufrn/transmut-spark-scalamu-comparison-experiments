package example
import scala.math.sqrt
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object MoviesRecommendation {
  def parseRatings(r: String) = {
    try {
      val data = r.split(",")
      val userId = data(0).toInt
      val movieId = data(1).toInt
      val rating = data(2).toDouble
      Some((userId, (movieId, rating)))
    } catch {
      case _: Throwable => None
    }
  }
  def removeDuplicates(u: (Int, ((Int, Double), (Int, Double)))): Boolean = {
    val movieId1 = u._2._1._1
    val movieId2 = u._2._2._1
    return movieId1 < movieId2
  }
  def makeCorrelatedMoviesPairs(u: (Int, ((Int, Double), (Int, Double)))) = {
    val movieId1 = u._2._1._1
    val rating1 = u._2._1._2
    val movieId2 = u._2._2._1
    val rating2 = u._2._2._2
    ((movieId1, movieId2), (rating1, rating2))
  }
  def similarity(pairs: Iterable[(Double, Double)]): (Double, Int) = {
    var sumAA: Double = 0.0d
    var sumBB: Double = 0.0d
    var sumAB: Double = 0.0d
    for (pair <- pairs) {
      val A = pair._1
      val B = pair._2
      sumAA += A * A
      sumBB += B * B
      sumAB += A * B
    }
    val num: Double = sumAB
    val den = sqrt(sumAA) * sqrt(sumBB)
    var sim: Double = 0.0d
    if (den != 0) {
      sim = num / den
    }
    (sim, pairs.size)
  }
  def relevantSimilarities(sim: ((Int, Int), (Double, Int)), minimumSimilarity: Double, minimumPairs: Int) = {
    sim._2._1 > minimumSimilarity && sim._2._2 > minimumPairs
  }
  def makeMoviesSimilaritiesPairs(sim: ((Int, Int), (Double, Int))) = {
    val movieId = sim._1._1
    val similarMovieId = sim._1._2
    val similarityScore = sim._2._1
    Array((movieId, (similarMovieId, similarityScore)), (similarMovieId, (movieId, similarityScore)))
  }
  def makeTopNRecommendedMoviesCSV(similarMovies: (Int, Iterable[(Int, Double)]), n: Int) = {
    val movieId = similarMovies._1
    val sortedSimilarMovies = similarMovies._2.toList.sortBy(_._2).reverse
    var recommendedMovies = movieId.toString
    var i = 0
    while (i < n && i < sortedSimilarMovies.size) {
      recommendedMovies += "," + sortedSimilarMovies(i)._1.toString
      i += 1
    }
    recommendedMovies
  }
  def moviesSimilaritiesTable(inputRDD: RDD[String]): RDD[((Int, Int), (Double, Int))] = sys.props.get("CURRENT_MUTANT") match {
    case Some("90") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("91") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities
      sortedMoviesSimilarities
    case Some("92") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("93") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("94") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("95") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        List[(Int, (Int, Double))]()
      } }
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("96") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((0, originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("97") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((1, originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("98") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((Int.MaxValue, originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("99") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((Int.MinValue, originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("100") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((-originalValue._1._1, originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("101") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((originalValue._1._1, 0), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("102") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((originalValue._1._1, 1), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("103") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((originalValue._1._1, Int.MaxValue), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("104") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((originalValue._1._1, Int.MinValue), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("105") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        ((originalValue._1._1, -originalValue._1._2), originalValue._2)
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("106") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (0d, originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("107") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (1d, originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("108") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (Double.MaxValue, originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("109") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (Double.MinValue, originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("110") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (-originalValue._2._1, originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("111") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 0d))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("112") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 1d))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("113") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Double.MaxValue))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("114") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Double.MinValue))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("115") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = makeCorrelatedMoviesPairs(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, -originalValue._2._2))
      } }
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("116") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("117") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings).distinct()
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("118") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings).distinct()
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("119") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates).distinct()
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("120") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs).distinct()
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("121") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey().distinct()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("122") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity).distinct()
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("123") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey().distinct()
      sortedMoviesSimilarities
    case Some("124") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.leftOuterJoin(ratings).map(tuple => (tuple._1, (tuple._2._1, tuple._2._2.getOrElse((0, 0d)))))
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("125") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.rightOuterJoin(ratings).map(tuple => (tuple._1, (tuple._2._1.getOrElse((0, 0d)), tuple._2._2)))
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("126") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.fullOuterJoin(ratings).map(tuple => (tuple._1, (tuple._2._1.getOrElse((0, 0d)), tuple._2._2.getOrElse((0, 0d)))))
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("127") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities
      sortedMoviesSimilarities
    case Some("128") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter { (inputParameter: (Int, ((Int, Double), (Int, Double)))) => {
        val originalFunction = removeDuplicates(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
    case Some("129") =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey(false)
      sortedMoviesSimilarities
    case _ =>
      val ratings: RDD[(Int, (Int, Double))] = inputRDD.flatMap(parseRatings)
      val selfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = ratings.join(ratings)
      val filteredSelfJoinRatings: RDD[(Int, ((Int, Double), (Int, Double)))] = selfJoinRatings.filter(removeDuplicates)
      val correlatedMovies: RDD[((Int, Int), (Double, Double))] = filteredSelfJoinRatings.map(makeCorrelatedMoviesPairs)
      val correlatedMoviesGroupedRatings: RDD[((Int, Int), Iterable[(Double, Double)])] = correlatedMovies.groupByKey()
      val moviesSimilarities: RDD[((Int, Int), (Double, Int))] = correlatedMoviesGroupedRatings.mapValues(similarity)
      val sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = moviesSimilarities.sortByKey()
      sortedMoviesSimilarities
  }
  def topNMoviesRecommendation(sortedMoviesSimilarities: RDD[((Int, Int), (Double, Int))], n: Int, minimumSimilarity: Double, minimumPairs: Int): RDD[String] = sys.props.get("CURRENT_MUTANT") match {
    case Some("130") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("131") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("132") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap { (inputParameter: ((Int, Int), (Double, Int))) => {
        val originalFunction = makeMoviesSimilaritiesPairs(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("133") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap { (inputParameter: ((Int, Int), (Double, Int))) => {
        val originalFunction = makeMoviesSimilaritiesPairs(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("134") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap { (inputParameter: ((Int, Int), (Double, Int))) => {
        val originalFunction = makeMoviesSimilaritiesPairs(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("135") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap { (inputParameter: ((Int, Int), (Double, Int))) => {
        val originalFunction = makeMoviesSimilaritiesPairs(_)
        val originalValue = originalFunction(inputParameter)
        List[(Int, (Int, Double))]()
      } }
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("136") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (inputParameter: (Int, Iterable[(Int, Double)])) => {
        val originalFunction = ((x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n))(_)
        val originalValue = originalFunction(inputParameter)
        ""
      } }
      topNRecommendedMoviesByMovie
    case Some("137") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("138") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }.distinct()
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("139") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs).distinct()
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("140") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey().distinct()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("141") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey().distinct()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("142") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }.distinct()
      topNRecommendedMoviesByMovie
    case Some("143") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("144") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (inputParameter: ((Int, Int), (Double, Int))) => {
        val originalFunction = ((x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs))(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case Some("145") =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey(false)
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
    case _ =>
      val relevantMoviesSimilarities: RDD[((Int, Int), (Double, Int))] = sortedMoviesSimilarities.filter { (x: ((Int, Int), (Double, Int))) => relevantSimilarities(x, minimumSimilarity, minimumPairs) }
      val recommendedMoviesPairs: RDD[(Int, (Int, Double))] = relevantMoviesSimilarities.flatMap(makeMoviesSimilaritiesPairs)
      val recommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesPairs.groupByKey()
      val sortedRecommendedMoviesList: RDD[(Int, Iterable[(Int, Double)])] = recommendedMoviesList.sortByKey()
      val topNRecommendedMoviesByMovie: RDD[String] = sortedRecommendedMoviesList.map { (x: (Int, Iterable[(Int, Double)])) => makeTopNRecommendedMoviesCSV(x, n) }
      topNRecommendedMoviesByMovie
  }
}