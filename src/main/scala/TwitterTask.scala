import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.{Minutes, Seconds}
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import twitter4j.Status

object TwitterTask{

  case class TweetData(status:String,retweet:Int,currentUserRetweets:Long,favorite:Int,createdAt:String,language:String,
                       inReplyScreen:String,inReplyID:Long,quoteID:Long,
                       rangeStart:Int,rangeEnd:Int)

  case class HashtagRecord(hashtag:String, count:Int)

  //TODO: hitung jumlah kata terbanyak
  def countWord(tweets: ReceiverInputDStream[Status]):Unit={
    tweets.foreachRDD((rdd,time)=>{
      val sqlContext = SparkSession
        .builder()
        .appName("Tweets Analysis Table")
        .getOrCreate()
      import sqlContext.implicits._

      val tweetDataFrame=rdd.map{tweets=>
        val status=tweets.getText
        val retweet=tweets.getRetweetCount
        val currentUserRetweet=tweets.getCurrentUserRetweetId
        val favorite=tweets.getFavoriteCount
        val createdAt=tweets.getCreatedAt().toInstant.toString
        val language=tweets.getLang

        //val contributor=tweets.getContributors
        //val witheld=tweets.getWithheldInCountries
        val inReplyScreen=tweets.getInReplyToScreenName
        val inReplyID=tweets.getInReplyToUserId
        //val quote=tweets.getQuotedStatus
        val quoteID=tweets.getQuotedStatusId

        val displayTextRangeStart=tweets.getDisplayTextRangeStart
        val displayTextRangeEnd=tweets.getDisplayTextRangeEnd


        TweetData(status,retweet,currentUserRetweet,favorite,createdAt,language,
          inReplyScreen,inReplyID,quoteID,
          displayTextRangeStart,displayTextRangeEnd)
      }.toDF()
      tweetDataFrame.createOrReplaceTempView("TweetTable")
      val result=sqlContext.sql("SELECT count(status) AS JumlahTweet FROM TweetTable")
      result.show()
      result.coalesce(3).write.mode(SaveMode.Append).option("header","true").csv("/home/hduser/Desktop/ABD/"+"tweetcount")
    })
  }

  //TODO: hitung jumlah tweet pada window tertentu
  def countTweet(tweets: ReceiverInputDStream[Status],slideDuration:Int,windowSize:Int):Unit={
    val status=tweets.map(tweet=>tweet.getText)
    val countStatus=status.countByWindow(Seconds(slideDuration),Seconds(windowSize))
    countStatus.print()
  }

  def trendingTopic(tweets: ReceiverInputDStream[Status],windowSize:Int,slidingSize:Int)={
    val statuses = tweets.map(tweet=>tweet.getText)
    val words = statuses.flatMap(status=>status.split(" "))
    val hashtags= words.filter(word=>word.contains('#'))
    val hashtagPairs=hashtags.map(hashtag=>(hashtag,1))

    //reduce data pada 5 menit terakhir, setiap 1 detik
    val windowedHashtags=hashtagPairs.reduceByKeyAndWindow((a,b)=>a+b, (a,b)=>a-b,Minutes(windowSize),Minutes(slidingSize))
    val sortedHastags = windowedHashtags.transform(rdd=>rdd.sortBy(x=>x._2,false))

    //mengubah RDD ke spark SQL
    sortedHastags.foreachRDD((rdd,time)=>{
      val sqlContext = SparkSession
        .builder()
        .appName("tweet Analysis Table")
        .getOrCreate()
      import sqlContext.implicits._

      val hashtagDataFrame=rdd.map(hashtag=>HashtagRecord(hashtag._1,hashtag._2)).toDF()
      hashtagDataFrame.createOrReplaceTempView("HashtagTable")
      val result=sqlContext.sql("SELECT hashtag, count FROM HashtagTable ORDER BY count DESC")
      result.show()
      result.coalesce(3).write.mode(SaveMode.Append).option("header","true").csv("/home/hduser/Desktop/ABD/"+"trendingTopics")

    })

  }
}
