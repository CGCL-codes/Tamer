    public ChannelIF getChannel(String feedUrl) {
        ChannelIF channel = null;
        try {
            boolean enabled = mConfig.getEnableAggregator().booleanValue();
            if (!enabled) {
                return null;
            }
            if (mConfig.getRssUseCache().booleanValue()) {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: use Cache for " + feedUrl);
                }
                channel = (ChannelIF) mCache.get(feedUrl);
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: got from Cache");
                }
                if (channel == null) {
                    try {
                        channel = RSSParser.parse(new ChannelBuilder(), new URL(feedUrl));
                    } catch (ParseException e1) {
                        mLogger.info("Error parsing RSS: " + feedUrl);
                    }
                }
                mCache.put(feedUrl, channel);
                mLogger.debug("Newsfeed: not in Cache");
            } else {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("Newsfeed: not using Cache for " + feedUrl);
                }
                try {
                    channel = RSSParser.parse(new ChannelBuilder(), new URL(feedUrl));
                } catch (ParseException e1) {
                    mLogger.info("Error parsing RSS: " + feedUrl);
                }
            }
        } catch (IOException ioe) {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("Newsfeed: Unexpected exception", ioe);
            }
        }
        return channel;
    }
