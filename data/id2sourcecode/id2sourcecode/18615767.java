    public List getFavourites() {
        return this.channelDAO.getChannels(IChannelDAO.FAVOURITES);
    }
