import scala.io.Source

object Main {
  def main(args: Array[String]) {
    time ( parseThem )
  }

  def parseThem {
    val format = new java.text.SimpleDateFormat("yyyy.MM.dd hh:mm:ss")
    val path = "/home/attila.forgacs/fix/data/EURUSD_5 Secs_Bid_2013.01.01_2013.11.06.csv"
    val src = Source.fromFile(path)
    val iter = src.getLines().drop(1)
    while (iter.hasNext) {
      val row = iter.next().split(",")
      val date = format.parse(row(0))
      val o = row(1).toFloat
      val h = row(2).toFloat
      val l = row(3).toFloat
      val c = row(4).toFloat
      val v = row(5).toFloat
    }
  }

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / Math.pow(10,9) + "ns")
    result
  }
}
