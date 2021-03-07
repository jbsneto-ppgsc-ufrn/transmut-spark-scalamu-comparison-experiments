package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class MoviesRecommendationTest extends FunSuite with SharedSparkContext with RDDComparisons {

  test("test 1 - moviesSimilaritiesTable") {
    val input = List("1,1,1.0,1111111111", "1,2,1.0,1111111111")

    val expected: List[((Int, Int), (Double, Int))] = List(((1, 2), (1.0, 1)))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRecommendation.moviesSimilaritiesTable(inputRDD)

    val result = resultRDD.collect()

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))
  }
  
  test("test 2 - moviesSimilaritiesTable") {
    val input = List("1,1,5.0,1111111111", "1,2,5.0,1111111111", "2,1,5.0,1111111111", "2,2,5.0,1111111111")

    val expected: List[((Int, Int), (Double, Int))] = List(((1,2),(0.9999999999999999,2)))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRecommendation.moviesSimilaritiesTable(inputRDD)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))
  }
  
  test("test 1 - topNMoviesRecommendation") {
    val input = List("1,1,1.0,1111111111", "1,2,1.0,1111111111")

    val expected = List("1,2", "2,1")

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val moviesSimilaritiesTable = MoviesRecommendation.moviesSimilaritiesTable(inputRDD)

    val resultRDD = MoviesRecommendation.topNMoviesRecommendation(moviesSimilaritiesTable, 1, 0.9, 0)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))
  }

  test("test 2 - topNMoviesRecommendation") {
    val input = List("1,1,5.0,1111111111", "1,2,5.0,1111111111", "1,3,5.0,1111111111", "2,1,5.0,1111111111", "2,2,5.0,1111111111", "2,3,5.0,1111111111", "3,1,5.0,1111111111", "3,2,5.0,1111111111", "3,3,5.0,1111111111")

    val expected = List("1,3", "2,3", "3,2")

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val moviesSimilaritiesTable = MoviesRecommendation.moviesSimilaritiesTable(inputRDD)

    val resultRDD = MoviesRecommendation.topNMoviesRecommendation(moviesSimilaritiesTable, 1, 0.9, 0)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))
  }
  
  test("test 3 - topNMoviesRecommendation") {
    val moviesSimilaritiesTableList: List[((Int, Int), (Double, Int))] = List(((1, 2), (0.5, 1)))

    val moviesSimilaritiesTableRDD = sc.parallelize(moviesSimilaritiesTableList)
    
    val expected: List[String] = List()
    
    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRecommendation.topNMoviesRecommendation(moviesSimilaritiesTableRDD, 1, 0.9, 0)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))
  }
  
  test("test 1 - similarity"){
    val input = List((0.0, 0.0), (0.0, 0.0))
    
    val expected = (0.0, 2)
    
    val result = MoviesRecommendation.similarity(input)

    assert(result == expected)
  }
  
  test("test 1 - relevantSimilarities"){
    
    val sim: ((Int, Int), (Double, Int)) = ((1,2), (1.0, 1))
    val minimumSimilarity: Double = 0.9
    val minimumPairs: Int = 1
    
    val expected = false
    
    val result = MoviesRecommendation.relevantSimilarities(sim, minimumSimilarity, minimumPairs)

    assert(result == expected)
  }
  
  test("test 2 - relevantSimilarities"){
    
    val sim: ((Int, Int), (Double, Int)) = ((1,2), (0.9, 2))
    val minimumSimilarity: Double = 0.9
    val minimumPairs: Int = 1
    
    val expected = false
    
    val result = MoviesRecommendation.relevantSimilarities(sim, minimumSimilarity, minimumPairs)

    assert(result == expected)
  }
  
  test("test 1 - makeTopNRecommendedMoviesCSV"){
    
    val similarMovies: (Int, Iterable[(Int, Double)]) = (1, List((2, 1.0), (3, 0.9)))
    
    val n: Int = 3
    
    val expected = "1,2,3"
    
    val result = MoviesRecommendation.makeTopNRecommendedMoviesCSV(similarMovies, n)

    assert(result == expected)
    
  }

}