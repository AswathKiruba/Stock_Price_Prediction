package TimeSeries


import com.cloudera.sparkts.TimeSeriesRDD
import com.cloudera.sparkts.models.ARIMA
import TimeSeries.TimeSeriesUtils.createMultipleCompanyValues

object models {
  def trainAndPredictPrice(tsRdd:TimeSeriesRDD[String]):Array[String]={

    val noOfDays = 30
    val df = tsRdd.mapSeries{vector => {
      val newVec = new org.apache.spark.mllib.linalg.DenseVector(vector.toArray.map(x => if(x.equals(Double.NaN)) 0 else x))

      //Train the values
      val arimaModel = ARIMA.fitModel(1, 0, 0, newVec)

      //Get the forecasted value and convert into the dense vector
      val forecasted = arimaModel.forecast(newVec, noOfDays)

      new org.apache.spark.mllib.linalg.DenseVector(forecasted.toArray.slice(forecasted.size-(noOfDays+1), forecasted.size-1))
      }
    }
    val listOfCompanies: List[String] = df.collectAsTimeSeries().keys.toList
    val companyValues = createMultipleCompanyValues(noOfDays,listOfCompanies)
    val priceList = df.collectAsTimeSeries().data.values
    val priceForecast = df.collect()
    val actualPrice = TimeSeriesAnalysis.getActualPrice()
    TimeSeriesUtils.getRMSE(actualPrice,priceForecast,8)
    TimeSeriesUtils.savePrediction(companyValues,priceList)
    TimeSeriesUtils.getTop3Companies(priceForecast)
    val companies = df.collect().map(_._1)
    TimeSeriesUtils.getAccuracy(actualPrice,priceForecast,30)
    companies



  }

}
