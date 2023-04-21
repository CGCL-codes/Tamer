    protected void loadInfo(AudioFileFormat aff) throws UnsupportedAudioFileException {
        String type = aff.getType().toString();
        if (!type.equalsIgnoreCase("flac")) throw new UnsupportedAudioFileException("Not Flac audio format");
        AudioFormat af = aff.getFormat();
        channels = af.getChannels();
        samplerate = (int) af.getSampleRate();
        bitspersample = af.getSampleSizeInBits();
    }
