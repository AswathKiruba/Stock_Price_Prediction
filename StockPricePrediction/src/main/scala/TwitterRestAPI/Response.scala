package TwitterRestAPI

import spray.json.DefaultJsonProtocol
import scala.util._


/**
  *Reference: https://medium.com/se-notes-by-alexey-novakov/yet-another-akka-streams-post-realtime-twitter-top-words-88e1c1b1fa2e
 */

case class Tweets(text: String,lang: String,created_at: String,retweet_count: Int,user: User, entities: Entities)

case class User(id: Int, favourites_count: Int, location: String, name: String)

case class Entities(hashtags: List[Hashtag])

case class Hashtag(text: String)

case class Response(statuses: List[Tweets],search_metadata: Metadata)

case class Metadata(count: Int)

object TweetProtocol extends DefaultJsonProtocol {
  implicit val jsonUser = jsonFormat4(User.apply)
  implicit val jsonHashtag = jsonFormat1(Hashtag.apply)
  implicit val jsonEntities = jsonFormat1(Entities.apply)
  implicit val jsonTweet = jsonFormat6(Tweets.apply)
  implicit val jsonMetadata = jsonFormat1(Metadata.apply)
  implicit val jsonResponse = jsonFormat2(Response.apply)
}

object Response {
  import spray.json._

  trait IngestibleResponse extends Ingestible[Response] {

    def fromString(filter: String): Try[Response] = {
      import TweetProtocol._
      Try(filter.parseJson.convertTo[Response])
    }
  }

  implicit object IngestibleResponse extends IngestibleResponse

}


object Tweet {
  import spray.json._

  trait IngestibleTweet extends Ingestible[Tweets] {

    def fromString(filter: String): Try[Tweets] = {
      import TweetProtocol._
      Try(filter.parseJson.convertTo[Tweets])
    }
  }

  implicit object IngestibleTweet extends IngestibleTweet

}
