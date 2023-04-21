    public Channel getChannel(UriPathElement urlId) throws UserException, ProgrammerException {
        Channel channel = (Channel) Hiber.session().createQuery("from Channel channel where channel.supply = :supply and channel.id = :channelId").setEntity("supply", this).setLong("channelId", Long.parseLong(urlId.getString())).uniqueResult();
        if (channel == null) {
            throw UserException.newNotFound();
        }
        return channel;
    }
