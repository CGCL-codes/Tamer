    public void serializeChannels(ComputeByteBuffer computeByteBuffer) throws IOException {
        Vector<FluxChannelMap> channels = scene.getChannels();
        serializeChannels(channels, computeByteBuffer);
    }
