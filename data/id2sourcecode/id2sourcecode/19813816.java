    public int getChannelAbsoluteTime(Header header) {
        final int channelId = header.getChannelId();
        Integer channelTime = channelTimeMap.get(channelId);
        if (channelTime == null) {
            logger.debug("first packet!");
            channelTime = seekTime;
        }
        if (videoChannel == -1 && header.getPacketType() == Type.VIDEO_DATA) {
            videoChannel = channelId;
            logger.info("video channel id is: " + videoChannel);
        }
        if (header.isRelative()) {
            channelTime = channelTime + header.getTime();
        } else {
            channelTime = seekTime + header.getTime();
        }
        channelTimeMap.put(channelId, channelTime);
        if (header.getPacketType() == Type.VIDEO_DATA) {
            logVideoProgress(channelTime);
        }
        return channelTime;
    }
