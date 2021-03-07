package example

import org.apache.spark._
import org.apache.log4j._
import org.apache.spark.rdd.RDD

object NGramsCount {
  // Start and end marks of a sentence
  val start = "<start>"
  val end = "<end>"
  
  var n: Int = 3

  /*
   * Calculate the n-grams of a given sentence.
   */
  def nGrams(sentence: String): List[List[String]] = {
    val sentenceLowerCase = sentence.trim.toLowerCase
    val tokens = sentenceLowerCase.split(' ').map(t => t.replaceAll("""\W""", "")).filter(_.length() > 0).toList
    val tokensStartEnd = List.fill(n - 1)(start) ++ tokens :+ end
    val ngrams = tokensStartEnd.sliding(n)
    ngrams.toList
  }

  def tokenize(x: String) = x.split("(?<=[a-z])\\.\\s+")

  def filterEmpty(l: List[String]) = l.filter(w => !w.trim.isEmpty).size == n && l != List.fill(n - 1)(start) :+ end

  def countNGrams(input: RDD[String]) = {
    
    val sentences = input.flatMap(tokenize)

    val ngrams = sentences.flatMap((s: String) => nGrams(s))

    val ngramsFiltered = ngrams.filter((ngram: List[String]) => filterEmpty(ngram))

    val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map((ngram: List[String]) => (ngram, 1))

    val ngramsCount = ngramsPairs.reduceByKey((x: Int, y: Int) => x + y)

    ngramsCount
  }
  
}