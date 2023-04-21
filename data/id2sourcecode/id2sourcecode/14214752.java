    protected static void openJavaSound(IStreamCoder aAudioCoder) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(), (int) IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()), aAudioCoder.getChannels(), true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        mLine = (SourceDataLine) AudioSystem.getLine(info);
        mLine.open(audioFormat);
        mLine.start();
    }
