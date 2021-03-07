package example
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
object MovieLensExploration {
  case class Movie(movieId: Int, title: String, year: Int, genres: List[String])
  case class Rating(userId: Int, movieId: Int, rating: Double, timestamp: Long)
  case class Tag(userId: Int, movieId: Int, tag: String, timestamp: Long)
  val genresSet = Set("Action", "Adventure", "Animation", "Children's", "Children", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western", "(no genres listed)")
  def extractMovieYear(movieTitle: String) = {
    val regex = "\\(([0-9]+)\\)".r
    val listNumbersTitle = regex.findAllIn(movieTitle).toList
    if (listNumbersTitle.nonEmpty) listNumbersTitle.last.replace("(", "").replace(")", "").trim.toInt else -1
  }
  def parseRatings(r: String) = {
    try {
      val data = r.split(",")
      val userId = data(0).toInt
      val movieId = data(1).toInt
      val rating = data(2).toDouble
      val timestamp = data(3).toLong
      Some(Rating(userId, movieId, rating, timestamp))
    } catch {
      case _: Throwable => None
    }
  }
  def parseMovies(m: String) = {
    try {
      val data = m.split(",")
      val movieId = data(0).toInt
      val title = data(1)
      val year = extractMovieYear(title)
      val genres = data(2).split('|').toList.map(_.trim()).filter(p => genresSet.contains(p))
      Some(Movie(movieId, title, year, genres))
    } catch {
      case _: Throwable => None
    }
  }
  def weighted_rating(R: Double, v: Double, m: Double, C: Double) = {
    v / (v + m) * R + m / (v + m) * C
  }
  def parseTags(m: String) = {
    try {
      val data = m.split(",")
      val userId = data(0).toInt
      val movieId = data(1).toInt
      val tag = data(2)
      val timestamp = data(3).toLong
      Some(Tag(userId, movieId, tag, timestamp))
    } catch {
      case _: Throwable => None
    }
  }
  def parseRDDs(ratingsCSV: RDD[String], moviesCSV: RDD[String], tagsCSV: RDD[String]): (RDD[Rating], RDD[Movie], RDD[Tag]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("1") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("2") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("3") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("4") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseRatings(_)
        val originalValue = originalFunction(inputParameter)
        List[Rating]()
      } }
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("5") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseMovies(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("6") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseMovies(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("7") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseMovies(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("8") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseMovies(_)
        val originalValue = originalFunction(inputParameter)
        List[Movie]()
      } }
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("9") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseTags(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      (ratings, movies, tags)
    case Some("10") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseTags(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      (ratings, movies, tags)
    case Some("11") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseTags(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      (ratings, movies, tags)
    case Some("12") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap { (inputParameter: String) => {
        val originalFunction = parseTags(_)
        val originalValue = originalFunction(inputParameter)
        List[Tag]()
      } }
      (ratings, movies, tags)
    case Some("13") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings).distinct()
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("14") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies).distinct()
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
    case Some("15") =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags).distinct()
      (ratings, movies, tags)
    case _ =>
      val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
      val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
      val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
      (ratings, movies, tags)
  }
  def moviesPerYearCount(movies: RDD[Movie]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("16") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("17") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.sortByKey(false)
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.reduceByKey { (a: Int, b: Int) => a + b }
      moviesPerYearInverseSorted
    case Some("18") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.sortByKey(false)
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.map { (t: (Int, Int)) => (t._2, t._1) }
      moviesPerYearInverseSorted
    case Some("19") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("20") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.sortByKey(false)
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("21") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("22") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.sortByKey(false)
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("23") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.reduceByKey { (a: Int, b: Int) => a + b }
      moviesPerYearInverseSorted
    case Some("24") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.map { (t: (Int, Int)) => (t._2, t._1) }
      moviesPerYearInverseSorted
    case Some("25") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("26") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("27") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse
      moviesPerYearInverseSorted
    case Some("28") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("29") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("30") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("31") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("32") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("33") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("34") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("35") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("36") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("37") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.year, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("38") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("39") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("40") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("41") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("42") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("43") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("44") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("45") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("46") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("47") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (inputParameter: (Int, Int)) => {
        val originalFunction = ((t: (Int, Int)) => (t._2, t._1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("48") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }.distinct()
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("49") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }.distinct()
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("50") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }.distinct()
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("51") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false).distinct()
      moviesPerYearInverseSorted
    case Some("52") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => firstParameter }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("53") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => secondParameter }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("54") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("55") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("56") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
    case Some("57") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse
      moviesPerYearInverseSorted
    case Some("58") =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(true)
      moviesPerYearInverseSorted
    case _ =>
      val yearTuple: RDD[(Int, Int)] = movies.map { (m: Movie) => (m.year, 1) }
      val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey { (a: Int, b: Int) => a + b }
      val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map { (t: (Int, Int)) => (t._2, t._1) }
      val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
      moviesPerYearInverseSorted
  }
  def mostPopularGenresByYear(movies: RDD[Movie]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("59") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("60") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple
      popularGenresByYear
    case Some("61") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => m.genres.map(g => ((m.year, g), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("62") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => m.genres.map(g => ((m.year, g), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("63") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => m.genres.map(g => ((m.year, g), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("64") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => m.genres.map(g => ((m.year, g), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        List[((Int, String), Int)]()
      } }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("65") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("66") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("67") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("68") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("69") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("70") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, ("", originalValue._2._2))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("71") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 0))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("72") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 1))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("73") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MaxValue))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("74") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MinValue))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("75") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (inputParameter: ((Int, String), Int)) => {
        val originalFunction = ((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, -originalValue._2._2))
      } }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("76") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }.distinct()
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("77") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }.distinct()
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("78") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }.distinct()
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("79") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }.distinct()
      popularGenresByYear
    case Some("80") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => firstParameter }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("81") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => secondParameter }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("82") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("83") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("84") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
    case Some("85") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (firstParameter: (String, Int), secondParameter: (String, Int)) => firstParameter }
      popularGenresByYear
    case Some("86") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (firstParameter: (String, Int), secondParameter: (String, Int)) => secondParameter }
      popularGenresByYear
    case Some("87") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (firstParameter: (String, Int), secondParameter: (String, Int)) => {
        val originalFunction = ((m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      popularGenresByYear
    case Some("88") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (firstParameter: (String, Int), secondParameter: (String, Int)) => {
        val originalFunction = ((m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      popularGenresByYear
    case Some("89") =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (firstParameter: (String, Int), secondParameter: (String, Int)) => {
        val originalFunction = ((m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      popularGenresByYear
    case _ =>
      val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap { (m: Movie) => m.genres.map(g => ((m.year, g), 1)) }
      val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey { (x: Int, y: Int) => x + y }
      val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map { (g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey { (m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2 }
      popularGenresByYear
  }
  def ratingsStatistics(movies: RDD[Movie], ratings: RDD[Rating]): RDD[((Int, String, Int), (Int, Double, Double, Double))] = sys.props.get("CURRENT_MUTANT") match {
    case Some("90") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("91") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("92") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("93") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("94") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("95") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, null.asInstanceOf[Movie])
      } }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("96") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("97") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("98") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("99") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("100") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("101") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (inputParameter: Rating) => {
        val originalFunction = ((r: Rating) => (r.movieId, r))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, null.asInstanceOf[Rating])
      } }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("102") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (inputParameter: (Int, (Movie, Rating))) => {
        val originalFunction = ((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0d)
      } }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("103") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (inputParameter: (Int, (Movie, Rating))) => {
        val originalFunction = ((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1d)
      } }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("104") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (inputParameter: (Int, (Movie, Rating))) => {
        val originalFunction = ((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Double.MaxValue)
      } }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("105") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (inputParameter: (Int, (Movie, Rating))) => {
        val originalFunction = ((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Double.MinValue)
      } }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("106") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (inputParameter: (Int, (Movie, Rating))) => {
        val originalFunction = ((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("107") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }.distinct()
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("108") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }.distinct()
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("109") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey).distinct()
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("110") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }.distinct()
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("111") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey().distinct()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("112") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }.distinct()
      ratingsStats
    case Some("113") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.leftOuterJoin(ratingsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1, tuple._2._2.getOrElse(null.asInstanceOf[Rating]))))
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("114") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.rightOuterJoin(ratingsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Movie]), tuple._2._2)))
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case Some("115") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.fullOuterJoin(ratingsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Movie]), tuple._2._2.getOrElse(null.asInstanceOf[Rating]))))
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
    case _ =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map { (r: Rating) => (r.movieId, r) }
      val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
      val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map { (tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating) }
      val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
      val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map { (x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)) }
      ratingsStats
  }
  def meanOfMeansOfRatingsOfAllMovies(ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("116") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (0d, originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("117") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (1d, originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("118") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (Double.MaxValue, originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("119") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (Double.MinValue, originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("120") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("121") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("122") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("123") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("124") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("125") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (inputParameter: (Tuple3[Int, String, Int], Tuple4[Int, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case Some("126") =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1) }.distinct()
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
    case _ =>
      val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map { (m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1) }
      val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce { (m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2) }
      sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
  }
  def bestMoviesDecade(ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))], minimumVotesRequired: Double, meanOfMeansRatingsOfAllMovies: Double) = sys.props.get("CURRENT_MUTANT") match {
    case Some("127") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade
      bestMoviesDecadeSorted
    case Some("128") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("129") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("130") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("131") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("132") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("133") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (inputParameter: (Tuple3[Int, String, Int], Tuple5[Int, Double, Double, Double, Double])) => {
        val originalFunction = ((m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, ("", originalValue._2._2))
      } }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("134") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("135") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("136") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("137") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("138") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("139") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (inputParameter: (Int, Iterable[(String, Tuple5[Int, Double, Double, Double, Double])])) => {
        val originalFunction = ((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, ("", originalValue._2._2))
      } }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("140") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }.distinct()
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("141") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }.distinct()
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("142") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey().distinct()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("143") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }.distinct()
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
    case Some("144") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true).distinct()
      bestMoviesDecadeSorted
    case Some("145") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade
      bestMoviesDecadeSorted
    case Some("146") =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(false)
      bestMoviesDecadeSorted
    case _ =>
      val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map { (s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))) }
      val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map { (m: ((Int, String, Int), (Int, Double, Double, Double, Double))) => (m._1._3 / 10 * 10, (m._1._2, m._2)) }
      val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
      val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map { (j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)) }
      val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
      bestMoviesDecadeSorted
  }
  def genresTagsCount(movies: RDD[Movie], tags: RDD[Tag]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("147") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("148") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped
      genresTagsCountSorted
    case Some("149") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("150") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("151") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("152") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("153") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("154") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (inputParameter: Movie) => {
        val originalFunction = ((m: Movie) => (m.movieId, m))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, null.asInstanceOf[Movie])
      } }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("155") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (0, originalValue._2)
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("156") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (1, originalValue._2)
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("157") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MaxValue, originalValue._2)
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("158") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (Int.MinValue, originalValue._2)
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("159") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (-originalValue._1, originalValue._2)
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("160") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (inputParameter: Tag) => {
        val originalFunction = ((t: Tag) => (t.movieId, t.tag))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, "")
      } }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("161") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (inputParameter: (Int, (Movie, String))) => {
        val originalFunction = ((mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("162") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (inputParameter: (Int, (Movie, String))) => {
        val originalFunction = ((mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("163") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (inputParameter: (Int, (Movie, String))) => {
        val originalFunction = ((mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("164") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (inputParameter: (Int, (Movie, String))) => {
        val originalFunction = ((mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)))(_)
        val originalValue = originalFunction(inputParameter)
        List[((String, String), Int)]()
      } }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("165") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("166") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, ("", originalValue._2._2))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("167") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 0))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("168") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, 1))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("169") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MaxValue))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("170") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, Int.MinValue))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("171") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (inputParameter: ((String, String), Int)) => {
        val originalFunction = ((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, (originalValue._2._1, -originalValue._2._2))
      } }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("172") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }.distinct()
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("173") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }.distinct()
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("174") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey).distinct()
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("175") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }.distinct()
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("176") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }.distinct()
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("177") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }.distinct()
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("178") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true).distinct()
      genresTagsCountSorted
    case Some("179") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (firstParameter: Int, secondParameter: Int) => firstParameter }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("180") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (firstParameter: Int, secondParameter: Int) => secondParameter }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("181") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((w: Int, z: Int) => w + z)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("182") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((w: Int, z: Int) => w + z)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("183") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((w: Int, z: Int) => w + z)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("184") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.leftOuterJoin(tagsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1, tuple._2._2.getOrElse(""))))
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("185") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.rightOuterJoin(tagsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Movie]), tuple._2._2)))
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("186") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.fullOuterJoin(tagsMovieIdKey).map(tuple => (tuple._1, (tuple._2._1.getOrElse(null.asInstanceOf[Movie]), tuple._2._2.getOrElse(""))))
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
    case Some("187") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped
      genresTagsCountSorted
    case Some("188") =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(false)
      genresTagsCountSorted
    case _ =>
      val moviesIdKey: RDD[(Int, Movie)] = movies.map { (m: Movie) => (m.movieId, m) }
      val tagsMovieIdKey: RDD[(Int, String)] = tags.map { (t: Tag) => (t.movieId, t.tag) }
      val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
      val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap { (mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)) }
      val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey { (w: Int, z: Int) => w + z }
      val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map { (g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)) }
      val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
      genresTagsCountSorted
  }
  def tagBestSummarizeGenre(genresTagsCountSorted: RDD[(String, (String, Int))]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("189") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (inputParameter: (String, Iterable[(String, Int)])) => {
        val originalFunction = ((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      genresTagsCountSortedTop5
    case Some("190") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (inputParameter: (String, Iterable[(String, Int)])) => {
        val originalFunction = ((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, List[(String, Int)](originalValue._2.head))
      } }
      genresTagsCountSortedTop5
    case Some("191") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (inputParameter: (String, Iterable[(String, Int)])) => {
        val originalFunction = ((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, originalValue._2.tail)
      } }
      genresTagsCountSortedTop5
    case Some("192") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (inputParameter: (String, Iterable[(String, Int)])) => {
        val originalFunction = ((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, originalValue._2.reverse)
      } }
      genresTagsCountSortedTop5
    case Some("193") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (inputParameter: (String, Iterable[(String, Int)])) => {
        val originalFunction = ((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, List[(String, Int)]())
      } }
      genresTagsCountSortedTop5
    case Some("194") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey().distinct()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)) }
      genresTagsCountSortedTop5
    case Some("195") =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)) }.distinct()
      genresTagsCountSortedTop5
    case _ =>
      val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
      val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map { (l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)) }
      genresTagsCountSortedTop5
  }
}