import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

import Utilities._
import TwitterTask._
object Main {

  def main(args: Array[String]): Unit = {
    setupTwitter()

    val appName:String="Twitter-Stream-Analysis"
    val bootstrap:String="local[*]"
    val batchSize=1
    val windowSize=10
    val slidingWindowSize=5
    val checkpoint="/home/hduser/Desktop/ABD"

    //buat Konfigurasi
    val conf=new SparkConf().setAppName(appName).setMaster("local[4]")
    val ssc=new StreamingContext(conf,Minutes(batchSize))
    setupLogging()

    //buat Dstream dan filter
    val filter=Seq("work","home","WFH","food","delivery","pesan","makanan")
    val tweets=TwitterUtils.createStream(ssc,None,filter)

    //Analytics
    //countTweet(tweets,5,10)
    trendingTopic(tweets,10,5)
    ssc.checkpoint(checkpoint)
    ssc.start()
    ssc.awaitTermination()
  }
}
