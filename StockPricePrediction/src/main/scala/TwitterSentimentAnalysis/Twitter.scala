package TwitterStreaming


import TwitterStreaming.TwitterRestAPI.calculateSentiment

object Twitter {


  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:/Users/aswat/OneDrive/Documents/spark/")

    val microsoftScore = calculateSentiment("Microsoft stock")
    val sentimentList = List(microsoftScore)
    val companyList = List("Microsoft")
    val result = (sentimentList,companyList).zipped.toArray.sortWith(_._1>_._1)
    for(i <- 0 until result.length){
      println(result(i)._2 + " " + result(i)._1)
    }



  }



}
