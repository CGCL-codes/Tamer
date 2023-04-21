    @Override
    public float getAudioData(int channel, long offset) {
        if (offset < fsb.getSampleCount()) return fsb.getChannel(channel)[(int) offset];
        return Enqueueable.NO_MORE_AUDIO_DATA;
    }
