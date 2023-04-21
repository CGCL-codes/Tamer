    protected void loadInfo(AudioFileFormat aff) throws UnsupportedAudioFileException {
        String ty = aff.getType().toString();
        if (!ty.equalsIgnoreCase("flac")) {
            throw new UnsupportedAudioFileException("Not Flac audio format");
        }
        AudioFormat af = aff.getFormat();
        channels = af.getChannels();
        samplerate = (int) af.getSampleRate();
        bitspersample = af.getSampleSizeInBits();
    }
