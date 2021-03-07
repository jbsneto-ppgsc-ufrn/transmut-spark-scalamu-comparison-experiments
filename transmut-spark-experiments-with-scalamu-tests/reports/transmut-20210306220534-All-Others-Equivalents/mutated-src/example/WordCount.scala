package example
import org.apache.spark._
import org.apache.spark.rdd.RDD
object WordCount {
  def wordCount(input: RDD[String]) = sys.props.get("CURRENT_MUTANT") match {
    case Some("229") =>
      val words = input
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("230") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs
      counts
    case Some("231") =>
      val words = input.flatMap { (inputParameter: String) => {
        val originalFunction = ((line: String) => line.split(" "))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.headOption
      } }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("232") =>
      val words = input.flatMap { (inputParameter: String) => {
        val originalFunction = ((line: String) => line.split(" "))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.tail
      } }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("233") =>
      val words = input.flatMap { (inputParameter: String) => {
        val originalFunction = ((line: String) => line.split(" "))(_)
        val originalValue = originalFunction(inputParameter)
        originalValue.toList.reverse
      } }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("234") =>
      val words = input.flatMap { (inputParameter: String) => {
        val originalFunction = ((line: String) => line.split(" "))(_)
        val originalValue = originalFunction(inputParameter)
        List[String]()
      } }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("235") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        ("", originalValue._2)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("236") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 0)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("237") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, 1)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("238") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MaxValue)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("239") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, Int.MinValue)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("240") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (inputParameter: String) => {
        val originalFunction = ((word: String) => (word, 1))(_)
        val originalValue = originalFunction(inputParameter)
        (originalValue._1, -originalValue._2)
      } }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("241") =>
      val words = input.flatMap { (line: String) => line.split(" ") }.distinct()
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("242") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }.distinct()
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
    case Some("243") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }.distinct()
      counts
    case Some("244") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => firstParameter }
      counts
    case Some("245") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => secondParameter }
      counts
    case Some("246") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(firstParameter, firstParameter)
      } }
      counts
    case Some("247") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(secondParameter, secondParameter)
      } }
      counts
    case Some("248") =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (firstParameter: Int, secondParameter: Int) => {
        val originalFunction = ((a: Int, b: Int) => a + b)(_, _)
        originalFunction(secondParameter, firstParameter)
      } }
      counts
    case _ =>
      val words = input.flatMap { (line: String) => line.split(" ") }
      val pairs = words.map { (word: String) => (word, 1) }
      val counts = pairs.reduceByKey { (a: Int, b: Int) => a + b }
      counts
  }
}