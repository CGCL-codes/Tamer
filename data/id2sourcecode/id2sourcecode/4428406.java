    public WavTrackImpl(URL url) throws UnsupportedAudioFileException, IOException, UnsupportedFormatException {
        stream = AudioSystem.getAudioInputStream(url);
        duration = (long) (stream.getFrameLength() / stream.getFormat().getFrameRate() * 1000);
        format = getFormat(stream);
        if (format == null) {
            throw new UnsupportedFormatException(format);
        }
        frameSize = (int) (period * format.getChannels() * format.getSampleSizeInBits() * format.getSampleRate() / 8000);
    }
