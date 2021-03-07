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

  // All genres available
  val genresSet = Set("Action", "Adventure", "Animation", "Children's", "Children", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western", "(no genres listed)")

  // this functions extracts the year of the movie that is attached with the movie title. For example: "Toy Story (1995)"
  // -1 represents an unspecified year
  def extractMovieYear(movieTitle: String) = {
    val regex = "\\(([0-9]+)\\)".r
    val listNumbersTitle = regex.findAllIn(movieTitle).toList
    if (listNumbersTitle.nonEmpty)
      listNumbersTitle.last.replace("(", "").replace(")", "").trim.toInt
    else
      -1
  }

  // ratings.csv has the following data: userId,movieId,rating,timestamp
  // we want (userId, movieId, rating, timestamp)
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

  // movies.csv has the following data: movieId,title,genres
  // we want (movieId,title, year, genres)
  def parseMovies(m: String) = {
    try {
      val data = m.split(",")
      val movieId = data(0).toInt
      val title = data(1)
      val year = extractMovieYear(title)
      val genres = data(2).split('|').toList.map(_.trim()).filter(p => genresSet.contains(p)) // removing unexpected genres
      Some(Movie(movieId, title, year, genres))
    } catch {
      case _: Throwable => None
    }
  }

  // R = average for the movie (mean) = (Rating)
  // v = number of votes for the movie = (votes)
  // m = minimum votes required to be listed in the Top 250
  // C = the mean vote across the whole report
  def weighted_rating(R: Double, v: Double, m: Double, C: Double) = {
    (v / (v + m)) * R + (m / (v + m)) * C
  }

  // tags.csv has the following data: userId,movieId,tag,timestamp
  // we want (userId,movieId,tag,timestamp)
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

  def parseRDDs(ratingsCSV: RDD[String], moviesCSV: RDD[String], tagsCSV: RDD[String]): (RDD[Rating], RDD[Movie], RDD[Tag]) = {
    val ratings: RDD[Rating] = ratingsCSV.flatMap(parseRatings)
    val movies: RDD[Movie] = moviesCSV.flatMap(parseMovies)
    val tags: RDD[Tag] = tagsCSV.flatMap(parseTags)
    (ratings, movies, tags)
  }

  def moviesPerYearCount(movies: RDD[Movie]) = {
    val yearTuple: RDD[(Int, Int)] = movies.map((m: Movie) => (m.year, 1))
    val moviesPerYear: RDD[(Int, Int)] = yearTuple.reduceByKey((a: Int, b: Int) => a + b)
    val moviesPerYearInverse: RDD[(Int, Int)] = moviesPerYear.map((t: (Int, Int)) => (t._2, t._1))
    val moviesPerYearInverseSorted: RDD[(Int, Int)] = moviesPerYearInverse.sortByKey(false)
    moviesPerYearInverseSorted
  }

  def mostPopularGenresByYear(movies: RDD[Movie]) = {
    val yearGenreTuple: RDD[((Int, String), Int)] = movies.flatMap((m: Movie) => m.genres.map(g => ((m.year, g), 1)))
    val yearGenreCount: RDD[((Int, String), Int)] = yearGenreTuple.reduceByKey((x: Int, y: Int) => x + y)
    val yearGenreCountTuple: RDD[(Int, (String, Int))] = yearGenreCount.map((g: ((Int, String), Int)) => (g._1._1, (g._1._2, g._2)))
    val popularGenresByYear: RDD[(Int, (String, Int))] = yearGenreCountTuple.reduceByKey((m1: (String, Int), m2: (String, Int)) => if (m1._2 > m2._2) m1 else m2)
    popularGenresByYear
  }

  def ratingsStatistics(movies: RDD[Movie], ratings: RDD[Rating]): RDD[((Int, String, Int), (Int, Double, Double, Double))] = {
    val moviesIdKey: RDD[(Int, Movie)] = movies.map((m: Movie) => (m.movieId, m))
    val ratingsMovieIdKey: RDD[(Int, Rating)] = ratings.map((r: Rating) => (r.movieId, r))
    val joinMoviesRatings: RDD[(Int, (Movie, Rating))] = moviesIdKey.join(ratingsMovieIdKey)
    val moviesInfoCounts: RDD[((Int, String, Int), Double)] = joinMoviesRatings.map((tuple: (Int, (Movie, Rating))) => ((tuple._1, tuple._2._1.title, tuple._2._1.year), tuple._2._2.rating))
    val groupByMoviesInfo: RDD[((Int, String, Int), Iterable[Double])] = moviesInfoCounts.groupByKey()
    val ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))] = groupByMoviesInfo.map((x: ((Int, String, Int), Iterable[Double])) => (x._1, (x._2.size, x._2.sum / x._2.size, x._2.min, x._2.max)))
    ratingsStats
  }

  def meanOfMeansOfRatingsOfAllMovies(ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))]) = {
    val meanOfRatingsAllMovies: RDD[(Double, Int)] = ratingsStats.map((m: ((Int, String, Int), (Int, Double, Double, Double))) => (m._2._2, 1))
    val sumMeanOfRatingsAllMovies: (Double, Int) = meanOfRatingsAllMovies.reduce((m1: (Double, Int), m2: (Double, Int)) => (m1._1 + m2._1, m1._2 + m2._2))
    sumMeanOfRatingsAllMovies._1 / sumMeanOfRatingsAllMovies._2
  }

  def bestMoviesDecade(ratingsStats: RDD[((Int, String, Int), (Int, Double, Double, Double))], minimumVotesRequired: Double, meanOfMeansRatingsOfAllMovies: Double) = {
    val ratingsStatisticsWithWR: RDD[((Int, String, Int), (Int, Double, Double, Double, Double))] = ratingsStats.map((s: ((Int, String, Int), (Int, Double, Double, Double))) => (s._1, (s._2._1, s._2._2, s._2._3, s._2._4, weighted_rating(s._2._2, s._2._1.toDouble, minimumVotesRequired, meanOfMeansRatingsOfAllMovies))))
    val ratingsStatisticsWithWRDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = ratingsStatisticsWithWR.map((m: (((Int, String, Int), (Int, Double, Double, Double, Double)))) => (m._1._3 / 10 * 10, (m._1._2, m._2)))
    val groupByDecade: RDD[(Int, Iterable[(String, (Int, Double, Double, Double, Double))])] = ratingsStatisticsWithWRDecade.groupByKey()
    val bestMoviesDecade: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = groupByDecade.map((j: (Int, Iterable[(String, (Int, Double, Double, Double, Double))])) => (j._1, j._2.reduce((j1, j2) => if (j1._2._5 > j2._2._5) j1 else j2)))
    val bestMoviesDecadeSorted: RDD[(Int, (String, (Int, Double, Double, Double, Double)))] = bestMoviesDecade.sortByKey(true)
    bestMoviesDecadeSorted
  }

  def genresTagsCount(movies: RDD[Movie], tags: RDD[Tag]) = {
    val moviesIdKey: RDD[(Int, Movie)] = movies.map((m: Movie) => (m.movieId, m))
    val tagsMovieIdKey: RDD[(Int, String)] = tags.map((t: Tag) => (t.movieId, t.tag))
    val joinMoviesTags: RDD[(Int, (Movie, String))] = moviesIdKey.join(tagsMovieIdKey)
    val genresTags: RDD[((String, String), Int)] = joinMoviesTags.flatMap((mg: (Int, (Movie, String))) => mg._2._1.genres.map(g => ((g, mg._2._2), 1)))
    val genresTagsCounts: RDD[((String, String), Int)] = genresTags.reduceByKey((w: Int, z: Int) => w + z)
    val genresTagsCountMapped: RDD[(String, (String, Int))] = genresTagsCounts.map((g: ((String, String), Int)) => (g._1._1, (g._1._2, g._2)))
    val genresTagsCountSorted: RDD[(String, (String, Int))] = genresTagsCountMapped.sortByKey(true)
    genresTagsCountSorted
  }

  def tagBestSummarizeGenre(genresTagsCountSorted: RDD[(String, (String, Int))]) = {
    val genresTagsCountSortedGrouped: RDD[(String, Iterable[(String, Int)])] = genresTagsCountSorted.groupByKey()
    val genresTagsCountSortedTop5: RDD[(String, List[(String, Int)])] = genresTagsCountSortedGrouped.map((l: (String, Iterable[(String, Int)])) => (l._1, l._2.toList.sortBy(_._2).reverse.take(5)))
    genresTagsCountSortedTop5
  }

}