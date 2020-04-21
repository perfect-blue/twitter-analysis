import org.apache.log4j.{Level, Logger}

object Utilities {
  /**
   * mengkonfigurasikan kredensial twitter untuk sistem streaming
   */
  def setupTwitter()={
    val consumerKey="xxxxxxxxxxxxxxxx"
    val consumerSecret="xxxxxxxxxxxxxxxxxxxxxxxxx"
    val accessToken="xxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    val accessTokenSecret="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

    System.setProperty("twitter4j.oauth.consumerKey",consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret",consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken",accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
  }

  /**
   * konfigurasi logger sehingga hanya menampilkan pesan ERROR saja
   * untuk menghindari log spam
   */
  def setupLogging()={
    val logger = Logger.getRootLogger()
    logger.setLevel(Level.ERROR)
  }
}
