    private synchronized void updateChannel() throws FeedManagerException {
        try {
            String feedUrl = this.feed.getLocation().toString();
            URL aURL = null;
            try {
                aURL = new URL(feedUrl);
            } catch (java.net.MalformedURLException e) {
                logger.error("Could not create URL for " + feedUrl);
            }
            URLConnection conn = null;
            try {
                conn = aURL.openConnection();
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setInstanceFollowRedirects(true);
                    HttpHeaderUtils.setUserAgent(httpConn, "Informa Java API");
                    HttpHeaderUtils.setETagValue(httpConn, this.httpHeaders.getETag());
                    HttpHeaderUtils.setIfModifiedSince(httpConn, this.httpHeaders.getIfModifiedSince());
                    httpConn.connect();
                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                        logger.info("cond. GET for feed at url " + feedUrl + ": no change");
                        this.feed.setLastUpdated(new Date());
                        this.lastUpdate = System.currentTimeMillis();
                        return;
                    }
                    logger.info("cond. GET for feed at url " + feedUrl + ": changed");
                    logger.debug("feed at url " + feedUrl + " new values : ETag" + HttpHeaderUtils.getETagValue(httpConn) + " if-modified :" + HttpHeaderUtils.getLastModified(httpConn));
                    this.httpHeaders.setETag(HttpHeaderUtils.getETagValue(httpConn));
                    this.httpHeaders.setIfModifiedSince(HttpHeaderUtils.getLastModified(httpConn));
                }
            } catch (java.lang.ClassCastException e) {
                logger.warn("problem cast to HttpURLConnection (reading from a file?) " + feedUrl, e);
            }
            ChannelIF channel = null;
            if (conn == null) {
                channel = FeedParser.parse(getChannelBuilder(), feedUrl);
            } else {
                channel = FeedParser.parse(getChannelBuilder(), conn.getInputStream());
            }
            this.feed.setChannel(channel);
            this.feed.setLastUpdated(new Date());
            this.lastUpdate = System.currentTimeMillis();
            logger.info("feed updated " + feedUrl);
        } catch (IOException e) {
            throw new FeedManagerException(e);
        } catch (ParseException e) {
            throw new FeedManagerException(e);
        }
    }
