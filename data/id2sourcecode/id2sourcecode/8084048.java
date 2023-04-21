    public int getChannelStatus(Integer index) {
        int status = 0;
        Object obj = channelIndexStatusHash.get(index);
        if (obj != null) {
            status = ((Integer) obj).intValue();
        }
        return status;
    }
