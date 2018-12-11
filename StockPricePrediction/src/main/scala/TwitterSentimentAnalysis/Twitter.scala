package TwitterStreaming


import TwitterStreaming.TwitterRestAPI.calculateSentiment

object Twitter {


  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:/Users/aswat/OneDrive/Documents/spark/")

    val aapleScore = calculateSentiment("Apple Inc stock")
    val valList = List(aapleScore)
    val structList = List("Apple")
    val res = (valList,structList).zipped.toArray.sortWith(_._1>_._1)
    for(i <- 0 until res.length){
      println(res(i)._2 + " " + res(i)._1)
    }



  }



}
