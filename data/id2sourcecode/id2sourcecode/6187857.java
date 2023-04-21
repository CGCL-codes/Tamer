    private RssChannelBean getChannel() throws Exception {
        if (rssType != TYPE_ATOM) return this.getChannelRss(); else return this.getChannelAtom();
    }
