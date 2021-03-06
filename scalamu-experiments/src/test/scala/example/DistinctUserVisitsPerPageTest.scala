package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class DistinctUserVisitsPerPageTest extends FunSuite with SharedSparkContext with RDDComparisons {

  test("test 1") {

    val input = List("0.0.0.0,test,1978-10-17,1.0,test,test,test,test,1", "1.1.1.1,test,1978-10-17,1.0,test,test,test,test,1")

    val expected = List(("test", Set("0.0.0.0", "1.1.1.1")))

    val inputRDD = sc.parallelize(input)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = DistinctUserVisitsPerPage.distinctUserVisitsPerPage(inputRDD)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }

}