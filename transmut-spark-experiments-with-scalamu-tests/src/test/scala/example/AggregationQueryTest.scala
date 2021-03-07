package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class AggregationQueryTest extends FunSuite with SharedSparkContext with RDDComparisons {
  
  test("test 1") {

    val input = List("192.168.10.1,test,1978-10-17,1.0,test,test,test,test,1")

    val expected = List(("192.168", 1.0f))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = AggregationQuery.aggregation(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
  test("test 2") {

    val input = List("192.168.10.1,test,1978-10-17,1.0,test,test,test,test,1", "192.168.10.1,test,1978-10-17,1.0,test,test,test,test,1")

    val expected = List(("192.168", 2.0f))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = AggregationQuery.aggregation(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
}