// Databricks notebook source
Stock market data can be interesting to analyze and as a further incentive, strong predictive models can have large financial payoff. The amount of financial data on the web is seemingly endless. A large and well structured dataset on a wide array of companies can be hard to come by. Here the dataset contains historical stock prices (last 5 years) for all companies currently found on the S&P 500 index

All the files have the following columns: 

Date - in format: yy-mm-dd

Open - price of the stock at market open (this is NYSE data so all in USD)

High - Highest price reached in the day

Low Close - Lowest price reached in the day

Volume - Number of shares traded

Name - the stock's ticker name

// COMMAND ----------

//Load the data from file as csv
val markets = spark
  .read.format("csv")
  .option("header", "true")
  .load("/FileStore/tables/all_stocks_5yr.csv")


// COMMAND ----------

markets.describe("open","high","close").show()

// COMMAND ----------

// load data as a table
val df = sqlContext.sql("SELECT * from  stocks_5yr")

// COMMAND ----------

// Display the data frame
df.show()

// COMMAND ----------

//count the missing values by summing the boolean output of the isNull() method with spark sql 
import org.apache.spark.sql.functions.{sum, col}
df.select(df.columns.map(c => sum(col(c).isNull.cast("int")).alias(c)): _*).show

// COMMAND ----------

// Handling missing value with spark sql
import org.apache.spark.ml.feature.Imputer

val imputer = new Imputer()
  .setInputCols(df.columns)
  .setOutputCols(df.columns.map(c => s"${c}_imputed"))
  .setStrategy("mean")

imputer.fit(df).transform(df)

// COMMAND ----------

// finding missing value using Spark DataFrame
val open_missing = markets.where($"open".isNull)

// COMMAND ----------

// Drop rows with Null on them
markets.na.drop(minNonNulls = 7).show()

// COMMAND ----------

Covariance is a measure of how two variables change with respect to each other. A positive number would mean that there is a tendency that as one variable increases, the other increases as well. A negative number would mean that as one variable increases, the other variable has a tendency to decrease. Correlation is a normalized measure of covariance that is easier to understand, as it provides quantitative measurements of the statistical dependence between two random variables.In our dataset, the columns open and close are positively correlated

// COMMAND ----------

df.stat.corr("open","close")

// COMMAND ----------

df.stat.corr("open","high")

// COMMAND ----------

// MAGIC %sql show tables

// COMMAND ----------

display(df.describe())

// COMMAND ----------

//Histogram of column low. 
%sql select low from stocks_5yr

// COMMAND ----------

// Trend Analysis of coulmn high with respect to date
%sql select date , high from stocks_5yr

// COMMAND ----------

// Correlation between column low and high. This exhibits a positive correlation
%sql select low , high from stocks_5yr

// COMMAND ----------

//Box Plot of the column Open. No outliers were present
%sql select open from stocks_5yr

// COMMAND ----------

display(df.groupBy("Name").avg("high").limit(10))

// COMMAND ----------


