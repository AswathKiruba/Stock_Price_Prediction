# Stock_Price_Prediction

This is the CSYE7200 Big Data Systems Engineering Using Scala Final Project for Team 9 Fall 2018

Team Members:

Meenakshi Muthiah muthiah.m@husky.neu.edu

Aswathnarayan Kirubakaran muthukrishnankirub.a@husky.neu.edu

# Abstract
The stock market is subject to rapid changes. It is very difficult to predict what will happen to the stock market in general or the stocks of a particular company. A significant amount of money can be earned or saved when the stock market trend for the day itself or the next day can be predicted. In this project to predict future stock price we apply supervised learning algorithms using Scala and Spark. 

# Methodology

## Exploratory Data analysis using Databricks

1. Handled missing data using SparkSQL and DataFrames
2. Calculated correlation and summary statistics
3. Visualized the Trend, correlation, outlier, distribution of the input data

## Time Series Analysis

1. Stationary Check by series plot 
2. Durbin Watson Test for autocorrelation in the residuals from a statistical regression analysis

## Twitter Sentiment Analysis

### Spark Twitter streaming and visualization using Kibana and ElasticSearch

1. Utilized Spark Streaming to receive the stream of tweets
2. Performed the sentiment analyis using Stanford NLP library
3. Stored the results (author, sentiment, tweet, date) in ElasticSearch
4. Visualized the top authors, sentiment count using Kibana
![twittersentiment](https://user-images.githubusercontent.com/30961303/49879752-ae224b80-fdf8-11e8-90c1-ee6f12b56bec.JPG)

### Twitter Sentiment Analysis using TwitterRestAPI

1. Utilized TwitterRestAPI to receive the tweets of past 3 days
2. Performed the sentiment analyis using Stanford NLP library

## Time Series Forecasting

1. *Feature Engineering* : Converted the stock prices to vectors
2. *Models*: Utilized ARIMA and EWMA model for stock price forecasting 
3. *Evaluation Metrics*: Accuracy and RMSE
