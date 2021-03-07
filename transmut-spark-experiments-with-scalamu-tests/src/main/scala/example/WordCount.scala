package example

import org.apache.spark._
import org.apache.spark.rdd.RDD

object WordCount {

	def wordCount(input: RDD[String]) = {
		val words = input.flatMap( (line: String) => line.split(" ") )
		val pairs = words.map( (word: String) => (word, 1) )
		val counts = pairs.reduceByKey( (a: Int, b: Int) => a + b )
		counts
	}
  
}