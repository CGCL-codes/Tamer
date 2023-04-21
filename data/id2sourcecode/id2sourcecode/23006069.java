    private int getChannelType(String type) {
        int result;
        if ("application/rss+xml".equals(type)) {
            result = News.CHANNEL_TYPE_RSS;
        } else if ("application/atom+xml".equals(type)) {
            result = News.CHANNEL_TYPE_RSS;
        } else {
            ChannelParser parser = new ChannelParser(this);
            String rpc = feedLink;
            Log.v(_TAG, "url " + rpc);
            InputStream is = parser.fetch(rpc);
            Integer cType = parser.parse(is);
            if (cType == null) {
                result = News.CHANNEL_TYPE_UNSUPPORTED;
            } else {
                result = cType;
            }
        }
        return result;
    }
