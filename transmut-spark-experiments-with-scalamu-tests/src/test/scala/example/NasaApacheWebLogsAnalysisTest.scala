package example

import org.scalatest.FunSuite

import com.holdenkarau.spark.testing.RDDComparisons
import com.holdenkarau.spark.testing.SharedSparkContext

class NasaApacheWebLogsAnalysisTest extends FunSuite with SharedSparkContext with RDDComparisons {

  test("test 1 - sameHostProblem") {

    val input1 = List("host	logname	time	method	url	response	bytes", "0.0.0.0	-	0000	GET	/url	000	0000	")
    val input2 = List("host	logname	time	method	url	response	bytes", "1.1.1.1	-	1111	GET	/url	111	1111	")

    val expected: List[String] = List()

    val inputRDD1 = sc.parallelize(input1)
    val inputRDD2 = sc.parallelize(input2)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = NasaApacheWebLogsAnalysis.sameHostProblem(inputRDD1, inputRDD2)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }

  test("test 1 - unionLogsProblem") {

    val input1 = List("host	logname	time	method	url	response	bytes", "0.0.0.0	-	0000	GET	/url	000	0000	")
    val input2 = List("host	logname	time	method	url	response	bytes", "1.1.1.1	-	1111	GET	/url	111	1111")

    val expected = List("0.0.0.0	-	0000	GET	/url	000	0000	", "1.1.1.1	-	1111	GET	/url	111	1111")

    val inputRDD1 = sc.parallelize(input1)
    val inputRDD2 = sc.parallelize(input2)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = NasaApacheWebLogsAnalysis.unionLogsProblem(inputRDD1, inputRDD2)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }
  
  test("test 2 - unionLogsProblem") {

    val input1 = List("host	logname	time	method	url	response	bytes", "0.0.0.0	-	0000	GET	/url	000	0000	")
    val input2 = List("host	logname	time	method	url	response	bits", "0.0.0.0	-	0000	GET	/url	000	0000	")

    val expected = List("0.0.0.0	-	0000	GET	/url	000	0000	", "host	logname	time	method	url	response	bits")

    val inputRDD1 = sc.parallelize(input1)
    val inputRDD2 = sc.parallelize(input2)

    val expectedRDD = sc.parallelize(expected)

    val resultRDD = NasaApacheWebLogsAnalysis.unionLogsProblem(inputRDD1, inputRDD2)

    assert(None === compareRDD(resultRDD, expectedRDD))

  }

}