package TwitterStreaming


import spray.json.DefaultJsonProtocol
import scala.util._

/**
  *Reference: https://medium.com/se-notes-by-alexey-novakov/yet-another-akka-streams-post-realtime-twitter-top-words-88e1c1b1fa2e
 */

case class Tweet(text: String,lang: String,created_at: String,retweet_count: Int,user: User, entities: Entities)

case class User(id: Int, favourites_count: Int, location: String, name: String)

case class Entities(hashtags: List[Hashtag])

case class Hashtag(text: String)

case class Response(statuses: List[Tweet],search_metadata: Metadata)

case class Metadata(count: Int)

object TweetProtocol extends DefaultJsonProtocol {
  implicit val jsonUser = jsonFormat4(User.apply)
  implicit val jsonHashtag = jsonFormat1(Hashtag.apply)
  implicit val jsonEntities = jsonFormat1(Entities.apply)
  implicit val jsonTweet = jsonFormat6(Tweet.apply)
  implicit val jsonMetadata = jsonFormat1(Metadata.apply)
  implicit val jsonResponse = jsonFormat2(Response.apply)
}

object Response {
  import spray.json._

  trait IngestibleResponse extends Ingestible[Response] {

    def fromString(w: String): Try[Response] = {
      import TweetProtocol._
      Try(w.parseJson.convertTo[Response])
    }
  }

  implicit object IngestibleResponse extends IngestibleResponse

}


object Tweet {
  import spray.json._

  trait IngestibleTweet extends Ingestible[Tweet] {

    def fromString(w: String): Try[Tweet] = {
      import TweetProtocol._
      Try(w.parseJson.convertTo[Tweet])
    }
  }

  implicit object IngestibleTweet extends IngestibleTweet

}
