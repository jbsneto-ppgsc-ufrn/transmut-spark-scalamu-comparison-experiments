package example

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite

import MovieLensExploration._

class MovieLensExplorationTest extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1 - parseRDDs") {

    val ratingsCSVList = List("1,44,1.0,1111111111")

    val moviesCSVList = List("44,Mortal Kombat (1995),Action|Adventure|Fantasy")

    val tagsCSVList = List("1,44,fight,1111111111")

    val ratingsCSV = sc.parallelize(ratingsCSVList)
    val moviesCSV = sc.parallelize(moviesCSVList)
    val tagsCSV = sc.parallelize(tagsCSVList)

    val expectedRatingsList = List(Rating(1,44,1.0,1111111111))

    val expectedMoviesList = List(Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")))

    val expectedTagsList = List(Tag(1, 44, "fight", 1111111111))

    val expectedRatings = sc.parallelize(expectedRatingsList)
    val expectedMovies = sc.parallelize(expectedMoviesList)
    val expectedTags = sc.parallelize(expectedTagsList)

    val (ratings, movies, tags) = parseRDDs(ratingsCSV, moviesCSV, tagsCSV)

    assert(None === compareRDD(ratings, expectedRatings))
    assert(None === compareRDD(movies, expectedMovies))
    assert(None === compareRDD(tags, expectedTags))
  }

  test("test 2 - parseRDDs") {

    val ratingsCSVList = List("1,1,1.0,1111111111")

    val moviesCSVList = List("1,Any ( ),Any | Drama")

    val tagsCSVList = List("1,1,any,1111111111")

    val ratingsCSV = sc.parallelize(ratingsCSVList)
    val moviesCSV = sc.parallelize(moviesCSVList)
    val tagsCSV = sc.parallelize(tagsCSVList)

    val expectedRatingsList = List(Rating(1,1,1.0,1111111111))

    val expectedMoviesList = List(Movie(1, "Any ( )", -1, List("Drama")))

    val expectedTagsList = List(Tag(1, 1, "any", 1111111111))

    val expectedRatings = sc.parallelize(expectedRatingsList)
    val expectedMovies = sc.parallelize(expectedMoviesList)
    val expectedTags = sc.parallelize(expectedTagsList)

    val (ratings, movies, tags) = parseRDDs(ratingsCSV, moviesCSV, tagsCSV)

    assert(None === compareRDD(ratings, expectedRatings))
    assert(None === compareRDD(movies, expectedMovies))
    assert(None === compareRDD(tags, expectedTags))
  }
  
  test("test 1 - moviesPerYearCount") {

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((2, 1995))
    val expectedResult = sc.parallelize(expectedResultList)

    val moviesPerYear = moviesPerYearCount(movies)

    assert(None === compareRDDWithOrder(moviesPerYear, expectedResult))
  }
  
  test("test 1 - mostPopularGenresByYear") {

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1995, ("Action", 2)))
    val expectedResult = sc.parallelize(expectedResultList)

    val mostPopularGenresYear = mostPopularGenresByYear(movies)

    assert(None === compareRDD(mostPopularGenresYear, expectedResult))
  }
  
  test("test 2 - mostPopularGenresByYear") {

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")),
      Movie(10, "GoldenEye (1995)", 1995, List("Action", "Adventure", "Thriller")),
      Movie(155, "Beyond Rangoon (1995)", 1995, List("Adventure", "Drama", "War")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1995, ("Action", 3)))
    val expectedResult = sc.parallelize(expectedResultList)

    val mostPopularGenresYear = mostPopularGenresByYear(movies)

    assert(None === compareRDD(mostPopularGenresYear, expectedResult))
  }
  
  test("test 1 - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade") {
    val ratingsList = List(
      Rating(1, 44, 5.0, 1111111111),
      Rating(2, 116469, 4.0, 1111111111))

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990, ("Mortal Kombat (1995)", (1, 5.0, 5.0, 5.0, 4.666666666666666))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)

    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }
  
  test("test 2 - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade") {
    val ratingsList = List(
      Rating(1, 44, 5.0, 1111111111),
      Rating(2, 44, 4.0, 1111111111),
      Rating(1, 116469, 4.0, 1111111111),
      Rating(2, 116469, 3.0, 1111111111))

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990,("Mortal Kombat (1995)", (2, 4.5, 4.0, 5.0, 4.25))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)
    
    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }
  
  test("test 3 - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade") {
    val ratingsList = List(
      Rating(1, 44, 5.0, 1111111111),
      Rating(2, 1682, 5.0, 1111111111),
      Rating(2, 44, 4.0, 1111111111),
      Rating(1, 116469, 4.0, 1111111111),
      Rating(2, 116469, 3.0, 1111111111),
      Rating(1, 1682, 5.0, 1111111111),
      Rating(3, 1682, 5.0, 1111111111))

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")),
      Movie(1682,"Truman Show, The (1998)", 1998, List("Comedy", "Drama", "Sci-Fi")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990,("Truman Show, The (1998)",(3,5.0,5.0,5.0,4.733333333333333))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)

    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }
  
  test("test 1 - genresTagsCount") {

    val moviesList = List(
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(116469, "Kickboxer 5 (1995)", 1995, List("Action", "Thriller")))


    val tagsList = List(
      Tag(1, 44, "fight", 1111111111),
      Tag(2, 44, "fight", 1111111111),
      Tag(1, 116469, "adventure", 1111111111),
      Tag(2, 116469, "adventure", 1111111111))

    val movies = sc.parallelize(moviesList)
    val tags = sc.parallelize(tagsList)

    val expectedResultList = List(
      ("Action", ("fight", 2)),
      ("Action", ("adventure", 2)),
      ("Adventure", ("fight", 2)),
      ("Fantasy", ("fight", 2)),
      ("Thriller", ("adventure", 2)))

    val expectedResult = sc.parallelize(expectedResultList)

    val genresTagsC = genresTagsCount(movies, tags)

    assert(None === compareRDDWithOrder(genresTagsC, expectedResult))
  }
  
  test("test 1 - tagBestSummarizeGenre") {

    val genresTagsCountList = List(
      ("Action", ("fight", 3)),
      ("Action", ("adventure", 2)))

    val genresTagsCount = sc.parallelize(genresTagsCountList)

    val expectedResultList = List(("Action", List(("fight", 3), ("adventure", 2))))
    val expectedResult = sc.parallelize(expectedResultList)

    val tagBestSummarizeGen = tagBestSummarizeGenre(genresTagsCount)

    assert(None === compareRDD(tagBestSummarizeGen, expectedResult))
  }

}