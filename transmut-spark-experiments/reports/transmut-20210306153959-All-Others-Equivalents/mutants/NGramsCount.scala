package example
import org.apache.spark._
import org.apache.log4j._
import org.apache.spark.rdd.RDD
object NGramsCount {
  val start = "<start>"
  val end = "<end>"
  var n: Int = 3
  def nGrams(sentence: String): List[List[String]] = {
    val sentenceLowerCase = sentence.trim.toLowerCase
    val tokens = sentenceLowerCase.split(' ').map(t => t.replaceAll("\\W", "")).filter(_.length() > 0).toList
    val tokensStartEnd = List.fill(n - 1)(start) ++ tokens :+ end
    val ngrams = tokensStartEnd.sliding(n)
    ngrams.toList
  }
  def tokenize(x: String) = x.split("(?<=[a-z])\\.\\s+")
  def filterEmpty(l: List[String]) = l.filter(w => !w.trim.isEmpty).size == n && l != List.fill(n - 1)(start) :+ end
  def countNGrams(input: RDD[String]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("184") =>
      val sentences = input
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("185") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("186") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs
      ngramsCount
    case Some("187") =>
      val sentences = input.flatMap { (inputParameter: String) => {
        val originalFunction = tokenize(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("188") =>
      val sentences = input.flatMap { (inputParameter: String) => {
        val originalFunction = tokenize(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("189") =>
      val sentences = input.flatMap { (inputParameter: String) => {
        val originalFunction = tokenize(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("190") =>
      val sentences = input.flatMap { (inputParameter: String) => {
        val originalFunction = tokenize(_)
        val originalValue = originalFunction(inputParameter)
        List[String]()
      } }
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("191") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (inputParameter: String) => {
        val originalFunction = ((s: String) => nGrams(s))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("192") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (inputParameter: String) => {
        val originalFunction = ((s: String) => nGrams(s))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("193") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (inputParameter: String) => {
        val originalFunction = ((s: String) => nGrams(s))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("194") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (inputParameter: String) => {
        val originalFunction = ((s: String) => nGrams(s))(_)
        val originalValue = originalFunction(inputParameter)
        List[List[String]]()
      } }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("195") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (List[String](originalValue._1.head), originalValue._2)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("196") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1.tail, originalValue._2)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("197") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1.reverse, originalValue._2)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("198") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (List[String](), originalValue._2)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("199") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("200") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("201") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("202") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("203") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => (ngram, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("204") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("205") =>
      val sentences = input.flatMap(tokenize).distinct()
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("206") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }.distinct()
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("207") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }.distinct()
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("208") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }.distinct()
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case Some("209") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }.distinct()
      ngramsCount
    case Some("210") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => firstParameter }
      ngramsCount
    case Some("211") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => secondParameter }
      ngramsCount
    case Some("212") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      ngramsCount
    case Some("213") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      ngramsCount
    case Some("214") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((x: Int, y: Int) => x + y)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      ngramsCount
    case Some("215") =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (inputParameter: List[String]) => {
        val originalFunction = ((ngram: List[String]) => filterEmpty(ngram))(_)
        val originalValue = originalFunction(inputParameter)
        !originalValue
      } }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
    case _ =>
      val sentences = input.flatMap(tokenize)
      val ngrams = sentences.flatMap { (s: String) => nGrams(s) }
      val ngramsFiltered = ngrams.filter { (ngram: List[String]) => filterEmpty(ngram) }
      val ngramsPairs: RDD[(List[String], Int)] = ngramsFiltered.map { (ngram: List[String]) => (ngram, 1) }
      val ngramsCount = ngramsPairs.reduceByKey { (x: Int, y: Int) => x + y }
      ngramsCount
  }
}