    protected void loadInfo(AudioFileFormat aff) throws UnsupportedAudioFileException {
        type = aff.getType().toString();
        AudioFormat format = aff.getFormat();
        channels = format.getChannels();
        samplerate = format.getSampleRate();
        bitspersample = format.getSampleSizeInBits();
        framesize = format.getFrameSize();
        bitrate = Math.round(bitspersample * samplerate * channels / 1000);
    }
