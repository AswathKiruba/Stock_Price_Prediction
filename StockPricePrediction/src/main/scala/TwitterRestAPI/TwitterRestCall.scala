package TwitterRestAPI

import TwitterStreaming.SentimentAnalysisUtil
import com.github.nscala_time.time.Imports.DateTime
import com.github.nscala_time.time.StaticDateTimeFormat
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import com.github.nscala_time.time.Imports._

import scala.io.{Codec, Source}

/**
     *reference: https://dzone.com/articles/access-twitter-rest-api-v11
     */


object TwitterRestCall {
  val consumerKey = "zckox0ybm1UheRy7DdirLYB18"
  val consumerSecret = "Nx0oDdxfZrRFepfbuMLFqMRvjAhYduaHPwlRCW5r0oHFypwmVn"
  val accessToken = "988311065896374272-ECPkJhP3GYbJ38RTUDMDNQJuhmeiAWg"
  val accessSecret = "LuL3oMyVNDpMpVTEmjokScshuoR5ZoozHB42fkcJsS81m"

  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessSecret)


  def restCall(dateTime: DateTime,filter: String, tweetCount: Int): String = {

    val consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret)
    consumer.setTokenWithSecret(accessToken, accessSecret)
    val url = "https://api.twitter.com/1.1/search/tweets.json?q=" + filter + "&count=" + tweetCount + "&until=" + dateTime.toString(StaticDateTimeFormat.forPattern("yyyy-MM-dd"))
    val request = new HttpGet(url)
    consumer.sign(request)
    val client = HttpClientBuilder.create().build()
    val response = client.execute(request)
    IOUtils.toString(response.getEntity().getContent())
  }


  def getTweets(filter: String, tweetCount: Int = 30): String = {
    val currentDate= DateTime.now
    val ss = for (i <- 1 to 5) yield restCall(currentDate-i.days,filter,tweetCount)
    ss.mkString("\n")
  }

  def calculateSentiment(filter: String = "", tweetCount: Int = 30, catchlog: Boolean = true): String = {
    val ingest = new Ingest[Response]()
    implicit val codec = Codec.UTF8
    val source = Source.fromString(getTweets(filter.replaceAll(" ","%20"),tweetCount))
    val realTweet = for (tweet <- ingest(source).toSeq) yield tweet
    val processedTweet = realTweet.flatMap(_.toOption)
    val sentiment = processedTweet.map(r => r.statuses).flatten
    val totalSentiment = sentiment.par.map(s => SentimentAnalysisUtil.detectSentimentScore(s.text,catchlog))
    val finalScore = totalSentiment.sum/totalSentiment.size
    finalScore match {
      case sentiment if sentiment <= 0.0 => "NOT_UNDERSTOOD"
      case sentiment if sentiment < 1.0 => "VERY_NEGATIVE"
      case sentiment if sentiment < 2.0 => "NEGATIVE"
      case sentiment if sentiment < 3.0 => "NEUTRAL"
      case sentiment if sentiment < 4.0 => "POSITIVE"
      case sentiment if sentiment < 5.0 => "VERY_POSITIVE"
      case sentiment if sentiment > 5.0 => "NOT_UNDERSTOOD"
    }

  }

}
