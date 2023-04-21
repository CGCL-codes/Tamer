    protected int getChannelCount(String address) {
        Integer iCount = channelMap.get(address);
        if (iCount == null) {
            logger.warn(address + ": how come we don't have a channel map for it? Assuming 2 until able to resolve");
            iCount = new Integer(2);
        }
        return iCount.intValue();
    }
