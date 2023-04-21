    public static RssFeeds getRssFeeds(String urlString) {
        TechFeedsLogger.debug("RssUtils::getRssFeeds()::START");
        String temp = null;
        RssFeeds rssFeeds = null;
        try {
            System.setProperty("http.proxyHost", "proxy.in.ml.com");
            System.setProperty("http.proxyPort", "8083");
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            rssFeeds = RSSDomParser.parseRss(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TechFeedsLogger.debug("RssUtils::getRssFeeds()::END");
        return rssFeeds;
    }
