    public AudioProcessor(IStreamCoder p_coder) {
        logger.setLevel(Level.WARN);
        coder = p_coder;
        audioFormat = new AudioFormat(coder.getSampleRate(), (int) IAudioSamples.findSampleBitDepth(coder.getSampleFormat()), coder.getChannels(), true, false);
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            mLine = (SourceDataLine) AudioSystem.getLine(info);
            mLine.open(audioFormat);
            mLine.start();
        } catch (LineUnavailableException e) {
            errorAndThrowException("could not open audio line: " + e.toString());
        }
    }
