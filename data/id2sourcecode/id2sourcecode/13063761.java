    private void initAudioLine(AudioInputStream audioStream) throws LineUnavailableException {
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
        if (!AudioSystem.isLineSupported(info)) {
            AudioFormat sourceFormat = audioFormat;
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), PLAYER_SAMPLE_SIZE_BITS, sourceFormat.getChannels(), sourceFormat.getChannels() * (PLAYER_SAMPLE_SIZE_BITS / 8), sourceFormat.getSampleRate(), sourceFormat.isBigEndian());
            FlacFormatConversionProvider flacCoverter = new FlacFormatConversionProvider();
            audioStream = flacCoverter.getAudioInputStream(targetFormat, audioStream);
            audioFormat = audioStream.getFormat();
        }
        sourceLine = getSourceDataLine(audioFormat);
        sourceLine.start();
        LineWriterThread lwThread = new LineWriterThread(audioStream, audioFormat.getFrameSize());
        lwThread.start();
    }
