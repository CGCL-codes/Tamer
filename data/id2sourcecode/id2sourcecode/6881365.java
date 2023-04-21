    @Override
    public void write(final RtmpMessage message) {
        final RtmpHeader header = message.getHeader();
        if (header.isAggregate()) {
            final ChannelBuffer in = message.encode();
            while (in.readable()) {
                final FlvAtom flvAtom = new FlvAtom(in);
                final int absoluteTime = flvAtom.getHeader().getTime();
                channelTimes[primaryChannel] = absoluteTime;
                write(flvAtom);
                logWriteProgress();
            }
        } else {
            final int channelId = header.getChannelId();
            channelTimes[channelId] = seekTime + header.getTime();
            if (primaryChannel == -1 && (header.isAudio() || header.isVideo())) {
                logger.info("first media packet for channel: {}", header);
                primaryChannel = channelId;
            }
            if (header.getSize() <= 2) {
                return;
            }
            write(new FlvAtom(header.getMessageType(), channelTimes[channelId], message.encode()));
            if (channelId == primaryChannel) {
                logWriteProgress();
            }
        }
    }
