    private static void loadWAV(AudioBuffer buffer, URL file) throws IOException {
        WavInputStream wavInput = new WavInputStream(file);
        ByteBuffer data = read(wavInput);
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            ShortBuffer tmp2 = data.duplicate().order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            while (tmp2.hasRemaining()) data.putShort(tmp2.get());
            data.rewind();
        }
        int channels = wavInput.getChannelCount();
        int bitRate = wavInput.getBitRate();
        int depth = wavInput.getDepth();
        int bytes = data.limit();
        float time = bytes / (bitRate * channels * depth * .125f);
        buffer.setup(data, channels, bitRate, time, depth);
        logger.log(Level.INFO, "wav loaded - time: {0} channels: {1} rate: {2} depth: {3} bytes: {4}", new Object[] { time, channels, bitRate, depth, bytes });
        data.clear();
        wavInput.close();
    }
