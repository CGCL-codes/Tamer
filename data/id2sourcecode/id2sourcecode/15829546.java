    @Override
    public void buildRss(final RSSChannel rssChannel, StringBuffer buf) {
        final IItSiteApplicationModule applicationModule = getApplicationModule();
        IQueryEntitySet<Map<String, Object>> qsData = null;
        if (applicationModule != null) qsData = applicationModule.queryBean(getSQL(), getValues()); else qsData = queryData();
        if (qsData != null) {
            Map<String, Object> data;
            while ((data = qsData.next()) != null) {
                if (qsData.position() > 100) {
                    return;
                }
                final ChannelItem channelItem = rssChannel.getChannelItem();
                channelItem.title = (String) data.get("title");
                channelItem.link = ItSiteUtil.url + getLink(data.get("id"));
                final Object content = data.get("content");
                if (content == null) {
                    channelItem.description = channelItem.title;
                } else {
                    channelItem.description = HTMLUtils.truncateHtml(HTMLUtils.createHtmlDocument((String) content, true), 100, false, false, false);
                }
                channelItem.category = getCatalog(data.get("catalogId"));
                final IUser user = ItSiteUtil.getUserById(data.get("userId"));
                channelItem.author = user == null ? "" : user.getText();
                channelItem.pubDate = ((Date) data.get("createDate")).toGMTString();
            }
        }
    }
