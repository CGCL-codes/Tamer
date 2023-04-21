    private void consume2Bytes(IOType io, int low, int high) {
        RecorderChannel channel = getChannelByName(io.getPublicName());
        if (channel != null) {
            int value = 256 * high + low;
            channel.append(value > 0xefff ? value - 0xffff - 1 : value);
        }
    }
