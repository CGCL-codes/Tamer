                }
                mInstance = new NewsfeedCache();
            }
        }
        return mInstance;
    }

    /**
     * Returns a Channel object for the supplied RSS newsfeed URL.
     * 
     * @param feedUrl
     *            RSS newsfeed URL.
     * @return FlockFeedI for specified RSS newsfeed URL.
     */
    public SyndFeed getChannel(String feedUrl) {
        SyndFeed feed = null;
        try {
            if (!aggregator_enabled) {
                return null;
            }
            if (aggregator_cache_enabled) {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: use Cache for " + feedUrl);
                }
                feed = (SyndFeed) mCache.get(feedUrl);
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: got from Cache");
                }
                if (feed == null) {
                    try {
                        SyndFeedInput feedInput = new SyndFeedInput();
                        feed = feedInput.build(new InputStreamReader(new URL(feedUrl).openStream()));
                    } catch (Exception e1) {
                        mLogger.info("Error parsing RSS: " + feedUrl);
                    }
                }
                mCache.put(feedUrl, feed);
                mLogger.debug("Newsfeed: not in Cache");
            } else {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: not using Cache for " + feedUrl);
                }
                try {
                    URLConnection connection = new URL(feedUrl).openConnection();
                    connection.connect();
                    String contentType = connection.getContentType();
                    String charset = "UTF-8";
                    if (contentType != null) {
                        int charsetStart = contentType.indexOf("charset=");
                        if (charsetStart >= 0) {
                            int charsetEnd = contentType.indexOf(";", charsetStart);
                            if (charsetEnd == -1) charsetEnd = contentType.length();
                            charsetStart += "charset=".length();
                            charset = contentType.substring(charsetStart, charsetEnd);
                            try {
                                byte[] test = "test".getBytes(charset);
                            } catch (UnsupportedEncodingException codingEx) {
                                charset = "UTF-8";
                            }
                        }
