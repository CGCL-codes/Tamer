    private ChannelBuffer incrDecrResponseString(Integer ret) {
        if (ret == null) {
            StatsCounter.bytes_read.addAndGet(11);
            return NOT_FOUND.duplicate();
        } else {
            StatsCounter.bytes_read.addAndGet(11);
            ChannelBuffer buffer = ChannelBuffers.copiedBuffer(valueOf(ret) + "\r\n", USASCII);
            StatsCounter.bytes_read.addAndGet(buffer.writerIndex());
            return buffer;
        }
    }
