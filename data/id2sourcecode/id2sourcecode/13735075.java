    private static void loadOGG(AudioBuffer buffer, URL file) throws IOException {
        OggInputStream oggInput = new OggInputStream(file, -1);
        ByteBuffer data = read(oggInput);
        int channels = oggInput.getChannelCount();
        int bitRate = oggInput.getBitRate();
        int depth = oggInput.getDepth();
        int bytes = data.limit();
        float time = bytes / (bitRate * channels * depth * .125f);
        buffer.setup(data, channels, bitRate, time, depth);
        logger.log(Level.INFO, "ogg loaded - time: {0} channels: {1} rate: {2} depth: {3} bytes: {4}", new Object[] { time, channels, bitRate, depth, bytes });
        data.clear();
        oggInput.close();
    }
