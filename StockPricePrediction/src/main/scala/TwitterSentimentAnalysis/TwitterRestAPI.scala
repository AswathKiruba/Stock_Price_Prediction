package TwitterStreaming


import com.github.nscala_time.time.Imports.DateTime
import com.github.nscala_time.time.StaticDateTimeFormat
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import com.github.nscala_time.time.Imports._


import scala.io.{Codec, Source}

object TwitterRestAPI {
  val ConsumerKey = "zckox0ybm1UheRy7DdirLYB18"
  val ConsumerSecret = "Nx0oDdxfZrRFepfbuMLFqMRvjAhYduaHPwlRCW5r0oHFypwmVn"
  val AccessToken = "988311065896374272-ECPkJhP3GYbJ38RTUDMDNQJuhmeiAWg"
  val AccessSecret = "LuL3oMyVNDpMpVTEmjokScshuoR5ZoozHB42fkcJsS81m"

  System.setProperty("twitter4j.oauth.consumerKey", ConsumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", ConsumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", AccessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", AccessSecret)


  def restCall(i: DateTime,k: String, count: Int): String = {
    /*
     *reference: https://dzone.com/articles/access-twitter-rest-api-v11
     */
    val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
    consumer.setTokenWithSecret(AccessToken, AccessSecret)
    val url = "https://api.twitter.com/1.1/search/tweets.json?q=" + k + "&count=" + count + "&until=" + i.toString(StaticDateTimeFormat.forPattern("yyyy-MM-dd"))
    val request = new HttpGet(url)
    consumer.sign(request)
    val client = HttpClientBuilder.create().build()
    val response = client.execute(request)
    IOUtils.toString(response.getEntity().getContent())
  }


  def getFromKeyword(k: String, count: Int = 90): String = {
    val today= DateTime.now
    val ss = for (i <- 1 to 7) yield restCall(today-i.days,k,count)
    ss.mkString("\n")
  }

  def calculateSentiment(k: String = "", count: Int = 90, catchlog: Boolean = true): Double = {
    val ingest = new Ingest[Response]()
    implicit val codec = Codec.UTF8
    val source = Source.fromString(getFromKeyword(k.replaceAll(" ","%20"),count))
    val realTweet = for (tweet <- ingest(source).toSeq) yield tweet
    val processedTweet = realTweet.flatMap(_.toOption)
    val sentiment = processedTweet.map(r => r.statuses)
    val finalTweet = sentiment.flatten
    val totalSentiment = finalTweet.par.map(s => SentimentAnalysisUtil.detectSentimentScore(s.text,catchlog))
    val finalScore = totalSentiment.sum/totalSentiment.size
    finalScore
  }

}