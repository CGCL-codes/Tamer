    @SuppressWarnings("unchecked")
    public List<ITorrentInfo> getTorrentInfo(IEpisode episode) {
        List<ITorrentInfo> infos = new ArrayList<ITorrentInfo>();
        for (IRssFeedInfo feedInfo : getRssFeedInfo()) {
            if (feedInfo.isEnabledForShow(episode.getShow())) {
                URL feed = resolveURL(feedInfo, episode);
                Element channel = null;
                if (feed != null) {
                    channel = getChannelElement(feed);
                }
                if (channel != null) {
                    for (Iterator i = channel.elementIterator("item"); i.hasNext(); ) {
                        Element item = (Element) i.next();
                        ITorrentInfo info = getTorrentInfo(episode, item, feedInfo);
                        if (info != null) {
                            infos.add(info);
                        }
                    }
                }
            }
        }
        return infos;
    }
