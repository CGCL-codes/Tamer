    public static RSSFeed newFromURL(String url) {
        RSSFeed feed = null;
        try {
            feed = newFromStream(new URL(url).openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed;
    }
