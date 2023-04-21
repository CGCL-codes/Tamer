    public long getSampleCount() {
        long total = (_audioInputStream.getFrameLength() * getFormat().getFrameSize() * 8) / getFormat().getSampleSizeInBits();
        return total / getFormat().getChannels();
    }
