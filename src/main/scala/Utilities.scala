import org.apache.log4j.{Level, Logger}

object Utilities {
  /**
   * mengkonfigurasikan kredensial twitter untuk sistem streaming
   */
  def setupTwitter()={
    val consumerKey="DjBtGDEVWtU29OcMieDUN8L1r"
    val consumerSecret="tVUGYm5j6PzrsOaMO0kVHHajOxlwwDxiWFPbbTLvkowcwfXQ7P"
    val accessToken="3321061040-OXVrtcLVilqBQUIMDnvlMs0wh4bO2MWRdthcAA9"
    val accessTokenSecret="E86TWY1XNoVpecpld15lN6hEXIGQjeTF5FBJdQK36mlhg"

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
