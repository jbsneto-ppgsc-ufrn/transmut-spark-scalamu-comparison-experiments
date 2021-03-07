package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class ScanQueryTest extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1") {

    val input = List("url1,300,1")

    val expected: List[(String, Int)] = List()

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = ScanQuery.scan(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
}