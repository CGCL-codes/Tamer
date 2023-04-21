    public List getChannelsLikeUrl(String url) {
        return this.channelDAO.findChannelsLikeUrl("%" + url.toLowerCase() + "%");
    }
