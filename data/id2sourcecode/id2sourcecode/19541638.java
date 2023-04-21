    public static void saveLinks(List<Link> links, String siteId, ChannelMapper channelMapper) {
        List<Channel> channels = channelMapper.getChannelsBySiteId(siteId);
        Set<String> setUrls = new HashSet<String>();
        if (null != channels && !channels.isEmpty()) {
            for (Channel chl : channels) {
                setUrls.add(chl.getUrl());
            }
        }
        for (Link lnk : links) {
            String url = lnk.getHref();
            if (!setUrls.contains(url)) {
                Channel chnl = new Channel();
                chnl.setSiteId(siteId);
                chnl.setUrl(url);
                chnl.setChannel(lnk.getText());
                channelMapper.insert(chnl);
            }
        }
    }
