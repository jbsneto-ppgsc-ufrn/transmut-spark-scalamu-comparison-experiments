package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class MoviesRatingsAverageTest extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1 - moviesRatingsAverage") {

    val movieNames = sc.broadcast(MoviesRatingsAverage.loadMovieNames())

    val input = List("1,1")

    val expected: List[String] = List()

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRatingsAverage.moviesRatingsAverage(inputRDD, movieNames)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))

  }
  
  test("test 2 - moviesRatingsAverage") {

    val movieNames = sc.broadcast(MoviesRatingsAverage.loadMovieNames())

    val input = List("1,A,1.0,1991-05-23")

    val expected: List[String] = List()

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRatingsAverage.moviesRatingsAverage(inputRDD, movieNames)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))

  }
  
  test("test 3 - moviesRatingsAverage") {

    val movieNames = sc.broadcast(MoviesRatingsAverage.loadMovieNames())

    val input = List("1,1,A,1991-05-23")

    val expected: List[String] = List()

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRatingsAverage.moviesRatingsAverage(inputRDD, movieNames)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))

  }
  
  test("test 4 - moviesRatingsAverage") {

    val movieNames = sc.broadcast(MoviesRatingsAverage.loadMovieNames())

    val input = List("1,1,1.0,1991-05-23")

    val expected = List("1,Toy Story (1995),1.0")

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRatingsAverage.moviesRatingsAverage(inputRDD, movieNames)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))

  }
  
  test("test 5 - moviesRatingsAverage") {

    val movieNames = sc.broadcast(MoviesRatingsAverage.loadMovieNames())

    val input = List("1,1,1.0,1991-05-23", "1,1,3.0,1991-05-23")

    val expected = List("1,Toy Story (1995),2.0")

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = MoviesRatingsAverage.moviesRatingsAverage(inputRDD, movieNames)

    assert(None === compareRDDWithOrder(resultRDD, expectedRDD))

  }
  
  test("test 1 - loadMovieNames") {
    val input = List("1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy", "0").iterator
    
    val expected = Map(1 -> "Toy Story (1995)")
    
    val result = MoviesRatingsAverage.loadMovieNames(input)
    
    assert(result == expected)
  }
  
}