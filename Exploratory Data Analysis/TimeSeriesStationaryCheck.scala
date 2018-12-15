// Databricks notebook source
 Time Series is a collection of data points collected at constant time intervals. These are analyzed to determine the long term trend so as to forecast the future or perform some other form of analysis. The two things which makes Time Series different from a regular regression problem are :

             1. It is time dependent. So the basic assumption of a linear regression model that the observations are independent doesn’t hold in this case.

             2. Along with an increasing or decreasing trend, most TS have some form of seasonality trends, i.e. variations specific to a particular time frame. For example, if you see the sales of a woolen jacket over time, you will invariably find higher sales in winter seasons.
Because of the inherent properties of a TS, there are various steps involved in analyzing it. These are discussed in detail below.

In the below example we will be using AAPL stocks

// COMMAND ----------

// create a dataframe for the Apple stock
val AAPLStock = sqlContext.read.format("com.databricks.spark.csv")
.option("header", "true")
.option("treatEmptyValuesAsNulls", "true")
.option("inferSchema", "true")
.option("mode","DROPMALFORMED")
.load("/FileStore/tables/AAPL_data.csv")

// COMMAND ----------

//Summary statistics
AAPLStock.describe("open","high","close").show()

A TS is said to be stationary if its statistical properties such as mean, variance remain constant over time.  Most of the TS models work on the assumption that the TS is stationary. Intuitively, we can sat that if a TS has a particular behaviour over time, there is a very high probability that it will follow the same in the future. Also, the theories related to stationary series are more mature and easier to implement as compared to non-stationary series.

Stationarity is defined using very strict criterion. However, for practical purposes we can assume the series to be stationary if it has constant statistical properties over time, ie. the following:

1.constant mean
2.constant variance
3.an autocovariance that does not depend on time.

// COMMAND ----------

display(AAPLStock.select("close","date"))

// COMMAND ----------





