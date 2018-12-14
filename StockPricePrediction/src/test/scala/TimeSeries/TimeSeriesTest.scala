package TimeSeries

import org.scalatest.{FlatSpec, Matchers}

class TimeSeriesTest  extends FlatSpec with Matchers {

  behavior of "Smoothing"

  it should "Smoothen the input List" in{

    TimeSeriesUtils.smoothening(List(2.0, 4.0, 7.0, 6.0, 3.0),4) shouldBe List(0.0, 0.0, 0.0, 4.75, 5.0)

  }

  behavior of "Spark"

  it should "spark app name should be Stock prediction" in {

    TimeSeriesAnalysis.conf.get("spark.app.name") shouldBe "Stock-prediction"

    }

  behavior of "companies"

  it should "create tags of companies multiple instance" in {

    TimeSeriesUtils.createMultipleCompanyValues(2,List("Deloitte","Google")) shouldBe List("Deloitte","Deloitte","Google","Google")
  }

  it should "create tags of companies of single instance" in {

    TimeSeriesUtils.createMultipleCompanyValues(2,List("Deloitte")) shouldBe  List("Deloitte","Deloitte")
  }

  

  }
