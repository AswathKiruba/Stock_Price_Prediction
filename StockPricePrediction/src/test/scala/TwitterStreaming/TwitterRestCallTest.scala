package TwitterStreaming


import TwitterRestAPI.TwitterRestCall
import org.scalatest.{FlatSpec, Matchers}
import TwitterStreaming.SentimentAnalysisUtil.replaceSpecialChar


class TwitterRestCallTest extends  FlatSpec with Matchers{

  behavior of "replaceSpecialChar"

  it should "work" in {
    replaceSpecialChar("#&Apple#") shouldBe "Apple"
  }

  behavior of "calculateSentiment"

  it should "Amazon stock" in {
    TwitterRestCall.calculateSentiment("Amazon Inc stock",10,false) shouldBe "NEGATIVE"
  }

  it should "Apple stock" in {
    TwitterRestCall.calculateSentiment("Apple Inc stock",10,false) shouldBe "NEGATIVE"
  }


}
