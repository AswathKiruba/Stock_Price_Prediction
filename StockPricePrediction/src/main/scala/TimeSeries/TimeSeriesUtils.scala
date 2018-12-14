package TimeSeries

import org.apache.spark.{SparkContext}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}

import scala.collection.mutable.ListBuffer
import TimeSeries.TimeSeriesAnalysis.conf

object TimeSeriesUtils {

  def getRMSE(actual: Array[Array[Double]],forecasted: Array[(String, Vector)], noOfDays: Int):Unit={
    val forecast = forecasted.map(_._2)
    val totalErrorSquared: ListBuffer[Double] = ListBuffer()
    for(j<- 0 until actual.length; i<- 0 until noOfDays){
      val errorSquare = Math.pow(forecast(j)(i)-actual(j)(i),2)
      totalErrorSquared += errorSquare
    }
    println("RMSE: " + (Math.sqrt(totalErrorSquared.sum/(noOfDays*9))))
  }

  def savePrediction(name: List[String], price: Array[Double]):Unit={
    val tup = (name,price).zipped.toArray
    val schema = StructType(StructField("Names",StringType,false)::StructField("Price",DoubleType,false)::Nil)
    val sc = SparkContext.getOrCreate(conf)
    val sqlContext = new SQLContext(sc)
    val rdd = sc.parallelize(tup).map(x=>Row(x._1,x._2.asInstanceOf[Number].doubleValue()))
    /**
      * uncomment to save the prediction result in a csv
      */
    //sqlContext.createDataFrame(rdd,schema).coalesce(1).write.partitionBy("Names").format("com.databricks.spark.csv").mode(SaveMode.Overwrite).save("prediction")
  }

  def getTop3Companies(forecasted:Array[(String, Vector)]):Array[(Double, String)]={
    val priceDiff = forecasted.map(x=>x._2).map(x=>x(x.size-1)-x(0))
    val name = forecasted.map(x=>x._1)
    val tup = (priceDiff,name).zipped.toArray.sortWith(_._1>_._1)

    val schema = StructType(
      StructField("Names", StringType, false) ::
        StructField("Profit", DoubleType, false) :: Nil)

    val sc = SparkContext.getOrCreate(conf)
    val sqlContext = new SQLContext(sc)
    val rdd = sc.parallelize (tup).map (x => Row(x._2, x._1.asInstanceOf[Number].doubleValue()))
    /**
      * uncomment to save the profit result in a csv
      */
   // sqlContext.createDataFrame(rdd, schema).coalesce(1).write.format("com.databricks.spark.csv").mode(SaveMode.Overwrite).save("predictedProfit")
    tup
  }

  def getAccuracy(actual: Array[Array[Double]],forecastedList: Array[(String, Vector)], noOfDays: Int ):Unit={

    val forecast = forecastedList.map(_._2)
    val accuracy: ListBuffer[Double] = ListBuffer()
    for (j <- 0 until actual.length; i <- 0 until noOfDays) {
      val errorSquare = Math.abs(forecast(j)(i) - actual(j)(i))/actual(j)(i)
      accuracy += errorSquare
    }
    println("Accuracy: " + (100-((accuracy.sum/(noOfDays*9)) * 1000)) + "%")
  }

  def createMultipleCompanyValues[String](n: Int, l: List[String]):List[String] = {
    l flatMap {e => List.fill(n)(e) }
  }

  def smoothening(values:List[Double],period:Int):List[Double] = {
    val first = (values take period).sum / period
    val subtract = values map (_ / period)
    val add = subtract drop period
    val addAndSubtract = add zip subtract map Function.tupled(_ - _)
    val res = (addAndSubtract.foldLeft(first :: List.fill(period - 1)(0.0)) {
      (acc, add) => (add + acc.head) :: acc
    }).reverse

    /**
      * uncomment to save the smoothing result in a csv
       */

//    val schema = StructType(StructField("Price",DoubleType,false)::Nil)
//    val sc = SparkContext.getOrCreate(conf)
//    val sqlContext = new SQLContext(sc)
//    val rdd = sc.parallelize(res).map(x=>Row(x.asInstanceOf[Number].doubleValue()))
//    sqlContext.createDataFrame(rdd,schema).coalesce(1).write.format("com.databricks.spark.csv").mode(SaveMode.Overwrite).save("smoothening")

    res
  }
}