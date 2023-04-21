    private SocketChannel getChannel(int id) {
        LOG.debug("selecting channel for session id: " + id);
        if (!channelMap.containsKey(id)) {
            throw new IllegalArgumentException("illegal ID");
        }
        return channelMap.get(id);
    }
