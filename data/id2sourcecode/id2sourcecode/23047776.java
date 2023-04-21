    public Page getChannelsByPartId(PageRequest pageRequest) {
        return pageQueryMysql("SiteChannels.getChannelsByPartIdlist", "SiteChannels.getCannelsByPartIdcount", pageRequest);
    }
