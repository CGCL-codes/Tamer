    public JPIPChannel getChannel(String cid) {
        if (channels.containsKey(cid)) return channels.get(cid);
        return null;
    }
