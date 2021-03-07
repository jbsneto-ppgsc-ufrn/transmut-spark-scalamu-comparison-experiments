package example

import org.apache.spark.rdd.RDD
import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class NGramsCountTests extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1 - countNGrams") {

    val input = List("     A simple sentence.     ", "     A simple sentence.     ")

    val expected = List(
      (List("<start>", "<start>", "a"), 2),
      (List("<start>", "a", "simple"), 2),
      (List("a", "simple", "sentence"), 2),
      (List("simple", "sentence", "<end>"), 2))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD: RDD[(List[String], Int)] = NGramsCount.countNGrams(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
  test("test 2 - countNGrams") {

    val input = List("  . . .   ")

    val expected: List[(List[String], Int)] = List()

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD: RDD[(List[String], Int)] = NGramsCount.countNGrams(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
  test("test 1 - nGrams"){
    val input = ""
    val expected = List(List("<start>", "<start>", "<end>"))
    
    val result = NGramsCount.nGrams(input)
    
    assert(result == expected)
  }
  
  test("test 1 - filterEmpty"){
    val input = List("   ", "   ", "   ")
    val expected = false
    
    val result = NGramsCount.filterEmpty(input)
    
    assert(result == expected)
  }

}