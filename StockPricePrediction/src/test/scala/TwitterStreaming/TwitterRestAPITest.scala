package TwitterStreaming


import org.scalatest.{FlatSpec, Matchers}
import TwitterStreaming.SentimentAnalysisUtil.replaceSpecialChar


class TwitterRestAPITest extends  FlatSpec with Matchers{

  behavior of "replaceSpecialChar"

  it should "work" in {
    replaceSpecialChar("#&Apple#") shouldBe "Apple"
  }

  behavior of "calculateSentiment"

  it should "Amazon stock" in {
    TwitterRestAPI.calculateSentiment("Amazon Inc stock",10,false) shouldBe "NEGATIVE"
  }

  it should "Apple stock" in {
    TwitterRestAPI.calculateSentiment("Apple Inc stock",10,false) shouldBe "NEGATIVE"
  }

}
