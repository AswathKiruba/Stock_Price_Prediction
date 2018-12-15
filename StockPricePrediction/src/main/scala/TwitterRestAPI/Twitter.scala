package TwitterRestAPI

import TwitterRestCall.calculateSentiment

object Twitter {


  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:/Users/aswat/OneDrive/Documents/spark/")

    val filterSentiment = calculateSentiment("Microsoft stock")
    val filterName = "Microsoft"
    println(filterName + " " + filterSentiment)

  }

}
