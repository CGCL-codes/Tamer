    public void addFeed(String title, String url) {
        RssChannel channel = new RssChannel();
        channel.setName(title);
        channel.setUrl(url);
        channel.setActive(RssSettings.DEFAULT_ACTIVE);
        channel.setPollInterval(RssSettings.DEFAULT_POLLINTERVAL);
        channel.setArticlesInView(RssSettings.DEFAULT_ARTICLESCOUNT);
        channel.setRememberArticlesEnabled(RssSettings.DEFAULT_REMEMBER);
        channel.setBeepEnabled(RssSettings.DEFAULT_BEEP);
        getChannelModel().add(getChannelModel().getRootNode(), channel);
    }
