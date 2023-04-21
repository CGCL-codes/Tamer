    public String getChannelType() {
        return getCategoryWithScheme(this.getCategories(), YouTubeNamespace.CHANNELTYPE_SCHEME).getTerm();
    }
