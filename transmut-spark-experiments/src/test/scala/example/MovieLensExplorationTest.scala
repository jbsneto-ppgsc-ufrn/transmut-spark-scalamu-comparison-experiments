package example

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite

import MovieLensExploration._

class MovieLensExplorationTest extends FunSuite with SharedSparkContext with RDDComparisons {

  test("Test - parseRDDs - 1") {

    val ratingsCSVList = List(
      "336,1,4.0,1122227329",
      "62,2,4.0,1528843890")

    val moviesCSVList = List(
      "1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy",
      "2,Jumanji (1995),Adventure|Children|Fantasy")

    val tagsCSVList = List(
      "336,1,pixar,1139045764",
      "62,2,fantasy,1528843929")

    val ratingsCSV = sc.parallelize(ratingsCSVList)
    val moviesCSV = sc.parallelize(moviesCSVList)
    val tagsCSV = sc.parallelize(tagsCSVList)

    val expectedRatingsList = List(
      Rating(336, 1, 4.0, 1122227329),
      Rating(62, 2, 4.0, 1528843890))

    val expectedMoviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val expectedTagsList = List(
      Tag(336, 1, "pixar", 1139045764),
      Tag(62, 2, "fantasy", 1528843929))

    val expectedRatings = sc.parallelize(expectedRatingsList)
    val expectedMovies = sc.parallelize(expectedMoviesList)
    val expectedTags = sc.parallelize(expectedTagsList)

    val (ratings, movies, tags) = parseRDDs(ratingsCSV, moviesCSV, tagsCSV)

    assert(None === compareRDD(ratings, expectedRatings))
    assert(None === compareRDD(movies, expectedMovies))
    assert(None === compareRDD(tags, expectedTags))
  }

  test("Test - parseRDDs - 2") {

    val ratingsCSVList = List(
      "374,153,4.0,849089070",
      "374,153,4.0,849089070",
      "374,153,4.0,849089070")

    val moviesCSVList = List(
      "153,Batman Forever (1995),Action|Adventure|Comedy|Crime",
      "153,Batman Forever (1995),Action|Adventure|Comedy|Crime",
      "153,Batman Forever (1995),Action|Adventure|Comedy|Crime")

    val tagsCSVList = List(
      "336,153,superhero,1139045840",
      "336,153,superhero,1139045840",
      "336,153,superhero,1139045840")

    val ratingsCSV = sc.parallelize(ratingsCSVList)
    val moviesCSV = sc.parallelize(moviesCSVList)
    val tagsCSV = sc.parallelize(tagsCSVList)

    val expectedRatingsList = List(
      Rating(374, 153, 4.0, 849089070),
      Rating(374, 153, 4.0, 849089070),
      Rating(374, 153, 4.0, 849089070))

    val expectedMoviesList = List(
      Movie(153, "Batman Forever (1995)", 1995, List("Action", "Adventure", "Comedy", "Crime")),
      Movie(153, "Batman Forever (1995)", 1995, List("Action", "Adventure", "Comedy", "Crime")),
      Movie(153, "Batman Forever (1995)", 1995, List("Action", "Adventure", "Comedy", "Crime")))

    val expectedTagsList = List(
      Tag(336, 153, "superhero", 1139045840),
      Tag(336, 153, "superhero", 1139045840),
      Tag(336, 153, "superhero", 1139045840))

    val expectedRatings = sc.parallelize(expectedRatingsList)
    val expectedMovies = sc.parallelize(expectedMoviesList)
    val expectedTags = sc.parallelize(expectedTagsList)

    val (ratings, movies, tags) = parseRDDs(ratingsCSV, moviesCSV, tagsCSV)

    assert(None === compareRDD(ratings, expectedRatings))
    assert(None === compareRDD(movies, expectedMovies))
    assert(None === compareRDD(tags, expectedTags))
  }

  test("Test - moviesPerYearCount - 1") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((2, 1995))
    val expectedResult = sc.parallelize(expectedResultList)

    val moviesPerYear = moviesPerYearCount(movies)

    assert(None === compareRDDWithOrder(moviesPerYear, expectedResult))
  }

  test("Test - moviesPerYearCount - 2") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(364, "Lion King, The (1994)", 1994, List("Adventure", "Animation", "Children", "Drama", "Musical", "IMAX")),
      Movie(367, "Mask, The (1994)", 1994, List("Action", "Comedy", "Crime", "Fantasy")),
      Movie(256, "Junior (1994)", 1994, List("Comedy", "Sci-Fi")),
      Movie(296, "Pulp Fiction (1994)", 1994, List("Comedy", "Crime", "Drama", "Thriller")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((4, 1994), (3, 1995))
    val expectedResult = sc.parallelize(expectedResultList)

    val moviesPerYear = moviesPerYearCount(movies)

    assert(None === compareRDDWithOrder(moviesPerYear, expectedResult))
  }

  test("Test - moviesPerYearCount - 3") {

    val moviesList = List(
      Movie(7451, "Mean Girls (2004)", 2004, List("Comedy")),
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(364, "Lion King, The (1994)", 1994, List("Adventure", "Animation", "Children", "Drama", "Musical", "IMAX")),
      Movie(367, "Mask, The (1994)", 1994, List("Action", "Comedy", "Crime", "Fantasy")),
      Movie(256, "Junior (1994)", 1994, List("Comedy", "Sci-Fi")),
      Movie(296, "Pulp Fiction (1994)", 1994, List("Comedy", "Crime", "Drama", "Thriller")),
      Movie(593, "Silence of the Lambs, The (1991)", 1991, List("Crime", "Horror", "Thriller")),
      Movie(595, "Beauty and the Beast (1991)", 1991, List("Animation", "Children", "Fantasy", "Musical", "Romance", "IMAX")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((4, 1994), (3, 1995), (2, 1991), (1, 2004))
    val expectedResult = sc.parallelize(expectedResultList)

    val moviesPerYear = moviesPerYearCount(movies)

    assert(None === compareRDDWithOrder(moviesPerYear, expectedResult))
  }

  test("Test - mostPopularGenresByYear - 1") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1995, ("Fantasy", 2)))
    val expectedResult = sc.parallelize(expectedResultList)

    val mostPopularGenresYear = mostPopularGenresByYear(movies)

    assert(None === compareRDD(mostPopularGenresYear, expectedResult))
  }

  test("Test - mostPopularGenresByYear - 2") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(44, "Mortal Kombat (1995)", 1995, List("Action", "Adventure", "Fantasy")),
      Movie(364, "Lion King, The (1994)", 1994, List("Adventure", "Animation", "Children", "Drama", "Musical", "IMAX")),
      Movie(367, "Mask, The (1994)", 1994, List("Action", "Comedy", "Crime", "Fantasy")),
      Movie(256, "Junior (1994)", 1994, List("Comedy", "Sci-Fi")),
      Movie(296, "Pulp Fiction (1994)", 1994, List("Comedy", "Crime", "Drama", "Thriller")),
      Movie(305, "Ready to Wear (Pret-A-Porter) (1994)", 1994, List("Comedy")))

    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1994, ("Comedy", 4)), (1995, ("Fantasy", 3)))
    val expectedResult = sc.parallelize(expectedResultList)

    val mostPopularGenresYear = mostPopularGenresByYear(movies)

    assert(None === compareRDD(mostPopularGenresYear, expectedResult))
  }

  test("Test - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade - 1") {

    val ratingsList = List(
      Rating(40, 1, 5.0, 832058959),
      Rating(62, 2, 4.0, 1528843890))

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990, ("Toy Story (1995)", (1, 5.0, 5.0, 5.0, 4.666666666666666))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)

    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }

  test("Test - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade - 2") {

    val ratingsList = List(
      Rating(40, 1, 5.0, 832058959),
      Rating(62, 2, 4.0, 1528843890),
      Rating(280, 7451, 5.0, 1347605314),
      Rating(68, 6934, 3.5, 1158531776))

    val moviesList = List(
      Movie(6934, "Matrix Revolutions, The (2003)", 2003, List("Action", "Adventure", "Sci-Fi", "Thriller", "IMAX")),
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(7451, "Mean Girls (2004)", 2004, List("Comedy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List(
      (1990, ("Toy Story (1995)", (1, 5.0, 5.0, 5.0, 4.583333333333333))),
      (2000, ("Mean Girls (2004)", (1, 5.0, 5.0, 5.0, 4.583333333333333))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)

    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }

  test("Test - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade - 3") {

    val ratingsList = List(
      Rating(110, 2959, 4.5, 1175329962),
      Rating(18, 33794, 4.5, 1455050618),
      Rating(62, 2, 4.0, 1528843890))

    val moviesList = List(
      Movie(2959, "Fight Club (1999)", 1999, List("Action", "Crime", "Drama", "Thriller")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(592, "Batman (1989)", 1989, List("Action", "Crime", "Thriller")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990, ("Fight Club (1999)", (1, 4.5, 4.5, 4.5, 4.333333333333333))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)
    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }

  test("Test - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade - 4") {

    val ratingsList = List(
      Rating(40, 1, 5.0, 832058959),
      Rating(62, 2, 4.0, 1528843890),
      Rating(62, 2, 4.0, 1528843890),
      Rating(62, 2, 4.0, 1528843890))

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990, ("Toy Story (1995)", (2, 5.0, 5.0, 5.0, 4.75))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)

    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }

  test("Test - ratingsStatistics, meanOfMeansOfRatingsOfAllMovies, bestMoviesDecade - 5") {

    val ratingsList = List(
      Rating(62, 2, 4.0, 1528843890),
      Rating(62, 2, 4.0, 1528843890),
      Rating(62, 2, 4.0, 1528843890))

    val moviesList = List(Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val ratings = sc.parallelize(ratingsList)
    val movies = sc.parallelize(moviesList)

    val expectedResultList = List((1990,("Jumanji (1995)",(3,4.0,4.0,4.0,4.0))))
    val expectedResult = sc.parallelize(expectedResultList)

    val ratingsStats = ratingsStatistics(movies, ratings)
    val mean = meanOfMeansOfRatingsOfAllMovies(ratingsStats)
    val bestMoviesDec = bestMoviesDecade(ratingsStats, 2.0, mean)
    
    assert(None === compareRDDWithOrder(bestMoviesDec, expectedResult))
  }

  test("Test - genresTagsCount - 1") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val tagsList = List(
      Tag(336, 1, "pixar", 1139045764),
      Tag(62, 2, "fantasy", 1528843929))

    val movies = sc.parallelize(moviesList)
    val tags = sc.parallelize(tagsList)

    val expectedResultList = List(
      ("Adventure", ("fantasy", 1)),
      ("Adventure", ("pixar", 1)),
      ("Animation", ("pixar", 1)),
      ("Children", ("pixar", 1)),
      ("Children", ("fantasy", 1)),
      ("Comedy", ("pixar", 1)),
      ("Fantasy", ("pixar", 1)),
      ("Fantasy", ("fantasy", 1)))

    val expectedResult = sc.parallelize(expectedResultList)

    val genresTagsC = genresTagsCount(movies, tags)

    assert(None === compareRDDWithOrder(genresTagsC, expectedResult))
  }

  test("Test - genresTagsCount - 2") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")),
      Movie(2355, "Bug's Life, A (1998)", 1998, List("Adventure", "Animation", "Children", "Comedy")))

    val tagsList = List(
      Tag(336, 1, "pixar", 1139045764),
      Tag(474, 1, "pixar", 1137206825),
      Tag(62, 2, "fantasy", 1528843929),
      Tag(474, 2355, "pixar", 1137191711))

    val movies = sc.parallelize(moviesList)
    val tags = sc.parallelize(tagsList)

    val expectedResultList = List(
      ("Adventure", ("fantasy", 1)),
      ("Adventure", ("pixar", 3)),
      ("Animation", ("pixar", 3)),
      ("Children", ("pixar", 3)),
      ("Children", ("fantasy", 1)),
      ("Comedy", ("pixar", 3)),
      ("Fantasy", ("pixar", 2)),
      ("Fantasy", ("fantasy", 1)))

    val expectedResult = sc.parallelize(expectedResultList)

    val genresTagsC = genresTagsCount(movies, tags)

    assert(None === compareRDDWithOrder(genresTagsC, expectedResult))
  }

  test("Test - genresTagsCount - 3") {

    val moviesList = List(
      Movie(1, "Toy Story (1995)", 1995, List("Adventure", "Animation", "Children", "Comedy", "Fantasy")),
      Movie(2, "Jumanji (1995)", 1995, List("Adventure", "Children", "Fantasy")))

    val tagsList = List(
      Tag(62, 2, "fantasy", 1528843929),
      Tag(474, 2355, "pixar", 1137191711))

    val movies = sc.parallelize(moviesList)
    val tags = sc.parallelize(tagsList)

    val expectedResultList = List(
      ("Adventure", ("fantasy", 1)),
      ("Children", ("fantasy", 1)),
      ("Fantasy", ("fantasy", 1)))

    val expectedResult = sc.parallelize(expectedResultList)

    val genresTagsC = genresTagsCount(movies, tags)

    assert(None === compareRDDWithOrder(genresTagsC, expectedResult))
  }

  test("Test - genresTagsCount - 4") {

    val moviesList = List(
      Movie(2355, "Bug's Life, A (1998)", 1998, List("Adventure", "Animation", "Children", "Comedy")),
      Movie(2355, "Bug's Life, A (1998)", 1998, List("Adventure", "Animation", "Children", "Comedy")),
      Movie(2355, "Bug's Life, A (1998)", 1998, List("Adventure", "Animation", "Children", "Comedy")))

    val tagsList = List(
      Tag(474, 2355, "pixar", 1137191711),
      Tag(474, 2355, "pixar", 1137191711),
      Tag(474, 2355, "pixar", 1137191711),
      Tag(474, 2355, "pixar", 1137191711))

    val movies = sc.parallelize(moviesList)
    val tags = sc.parallelize(tagsList)

    val expectedResultList = List(
      ("Adventure", ("pixar", 12)),
      ("Animation", ("pixar", 12)),
      ("Children", ("pixar", 12)),
      ("Comedy", ("pixar", 12)))

    val expectedResult = sc.parallelize(expectedResultList)

    val genresTagsC = genresTagsCount(movies, tags)

    assert(None === compareRDDWithOrder(genresTagsC, expectedResult))
  }

  test("Test - tagBestSummarizeGenre - 1") {

    val genresTagsCountList = List(
      ("Adventure", ("fantasy", 3)),
      ("Adventure", ("pixar", 1)),
      ("Adventure", ("classic", 5)),
      ("Adventure", ("iconic", 4)),
      ("Adventure", ("original", 2)),
      ("Adventure", ("Disney", 6)))

    val genresTagsCount = sc.parallelize(genresTagsCountList)

    val expectedResultList = List(("Adventure", List(("Disney", 6), ("classic", 5), ("iconic", 4), ("fantasy", 3), ("original", 2))))
    val expectedResult = sc.parallelize(expectedResultList)

    val tagBestSummarizeGen = tagBestSummarizeGenre(genresTagsCount)

    assert(None === compareRDD(tagBestSummarizeGen, expectedResult))
  }

}