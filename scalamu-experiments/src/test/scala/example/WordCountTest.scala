package example

import org.apache.spark.rdd.RDD
import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class WordCountTest extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1") {
    
    val input = List("A simple sentence", "A simple sentence")

    val expected: List[(String, Int)] = List(("A", 2), ("simple", 2), ("sentence", 2))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = WordCount.wordCount(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
}