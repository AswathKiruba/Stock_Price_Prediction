package TimeSeries

import java.time.{ZoneId, ZonedDateTime}

import com.cloudera.sparkts.models.ARIMA
import com.cloudera.sparkts.{DateTimeIndex, DayFrequency, TimeSeriesRDD}
import org.apache.spark.sql._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}


import scala.language.postfixOps
import com.cloudera.sparkts.stats.TimeSeriesStatisticalTests
import TimeSeries.models
import org.apache.spark.sql.functions.unix_timestamp
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._


object TimeSeriesAnalysis extends App{


  def loadObservations(sqlContext: SQLContext, path: String): DataFrame = {
    val stocks = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("treatEmptyValuesAsNulls", "true")
      .option("inferSchema", "true")
      .option("mode", "DROPMALFORMED")
      .load(path)
    stocks
  }

    System.setProperty("hadoop.home.dir", "C:/Users/meena/Documents/spark/Hadoop")
    //Create spark conf
    lazy val conf = {
      new SparkConf(false).setMaster("local[*]").setAppName("Stock-prediction").set("spark.logconf", "true")
    }

    //Create spark session only when required
    lazy val spark = SparkSession.builder().appName("Stock-prediction").master("local[*]").getOrCreate();

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val tickerObs = loadObservations(sqlContext,"C:/Users/meena/Documents/Stock_Prediction/all_stocks_5yr.csv")

    tickerObs.show(5)

    //feature selection
   // val format = "yyyy-MM-dd HH:mm:ss"
    val feature = tickerObs.drop("open","high","low","volume")
    val formatted= tickerObs.withColumn("date",to_utc_timestamp(tickerObs("date"),"yyyy-MM-dd"))
    //features.show(5)
    // Create an daily DateTimeIndex
    val minDate = formatted.selectExpr("min(date)").collect()(0).getTimestamp(0)
    val maxDate = formatted.selectExpr("max(date)").collect()(0).getTimestamp(0)
    val zone = ZoneId.systemDefault()
    val dtIndex = DateTimeIndex.uniformFromInterval(
      ZonedDateTime.of(minDate.toLocalDateTime, zone), ZonedDateTime.of(maxDate.toLocalDateTime, zone), new DayFrequency(1)
    )

    // Align the ticker data on the DateTimeIndex to create a TimeSeriesRDD
    val tsRdd = TimeSeriesRDD.timeSeriesRDDFromObservations(dtIndex, feature, "date", "Name", "close")

    // Count the number of series (number of symbols)
    println(tsRdd.count())

    // Impute missing values using linear interpolation
    val filled = tsRdd.fill("linear")

    // Compute return rates
    val returnRates = filled.returnRates()

    // Compute Durbin-Watson stats for each series
    val dwStats = returnRates.mapValues(TimeSeriesStatisticalTests.dwtest)

    println(dwStats.map(_.swap).min)
    println(dwStats.map(_.swap).max)

  def getActualPrice(): Array[Array[Double]] = {
    val stock: DataFrame = formatted.select(formatted("date"), formatted("close"))
    val priceActual: Array[Double] = stock.collect().flatMap((row: Row) => Array(row.getDouble(1)))
    val actualPrices =Array(priceActual)
    actualPrices
  }
  //call models
  models.trainAndPredictPrice(tsRdd)

}
