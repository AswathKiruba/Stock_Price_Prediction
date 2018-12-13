package TwitterStreaming

import org.scalatest.{FlatSpec, Matchers}

class SentimentUtilTest extends FlatSpec with Matchers {

  behavior of "detectSentiment"

  it should "POSITIVE Sentiment" in {

    SentimentAnalysisUtil.detectSentiment("It was a nice movie.",false) shouldBe SentimentAnalysisUtil.POSITIVE

  }

  it should "NEUTRAL Sentiment" in {

    SentimentAnalysisUtil.detectSentiment("I don't want to leave.",false) shouldBe SentimentAnalysisUtil.NEUTRAL

  }

  it should "NEGATIVE Sentiment" in {

    SentimentAnalysisUtil.detectSentiment("I hate watching from last row.",false) shouldBe SentimentAnalysisUtil.NEGATIVE

  }

  behavior of "detectSentimentScore"


  it should "detect VERY_NEGATIVE 1.0" in {
    SentimentAnalysisUtil.detectSentimentScore("I hate watching from last row.",false) shouldBe 1.0
  }

  it should "detect NEGATIVE 2.0" in {
    SentimentAnalysisUtil.detectSentimentScore("I don't want to leave.",false) shouldBe 2.0
  }

  it should "detect NEUTRAL 3.0" in {
    SentimentAnalysisUtil.detectSentimentScore("It was nice meeting him.", false) shouldBe 3.0
  }

  it should "detect POSITIVE 4.0" in {
    SentimentAnalysisUtil.detectSentimentScore("It was an amazing movie.", false) shouldBe 4.0
  }





}
