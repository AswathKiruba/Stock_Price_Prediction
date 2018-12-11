package TwitterStreaming


import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark._
import TwitterStreaming.TwitterRestAPI.{accessSecret, accessToken, consumerKey, consumerSecret}
import SentimentAnalysisUtil.detectSentiment


object SparkTwitterStreaming {


    // set twitter oAuth keys
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessSecret)

    // search Keyword
    val filters = Array("Microsoft")

    val conf = new SparkConf().setAppName("TwitterSentimentAnalysis").setMaster("local[4]")

    // Create a DStream for every 5 seconds
    val ssc = new StreamingContext(conf, Seconds(5))

    // Get json object from twitter stream
    val tweets = TwitterUtils.createStream(ssc, None, filters)

    tweets.print()


    tweets.foreachRDD{(rdd, time) =>
      rdd.map(t => {
        Map(
          "author"-> t.getUser.getScreenName,
          "date" -> t.getCreatedAt.getTime.toString,
          "message" -> t.getText,
          "sentiment" -> detectSentiment(t.getText).toString
        )
      }).saveToEs("SparkTwitter/tweets")
    }


    ssc.start()
    ssc.awaitTermination()



}
