    public void copyChannel(int sourceChannel, int targetChannel) {
        float[] source = getChannel(sourceChannel);
        float[] target = getChannel(targetChannel);
        System.arraycopy(source, 0, target, 0, getSampleCount());
    }
