    public Channel getChannel(Long id) {
        List results = findChannelQueryById.execute(new Object[] { id });
        if (results.size() > 0) {
            return (Channel) results.get(0);
        }
        return null;
    }
