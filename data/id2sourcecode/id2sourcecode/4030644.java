    public Channel getChannelById(Long id) {
        return (Channel) generalDao.fetch(id, ChannelEntity.class);
    }
