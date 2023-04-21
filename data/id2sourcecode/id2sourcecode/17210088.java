    private void openAudio(IStreamCoder aAudioCoder) {
        AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(), (int) IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()), aAudioCoder.getChannels(), true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            mLine = (SourceDataLine) AudioSystem.getLine(info);
            mLine.open(audioFormat);
            mLine.start();
        } catch (IllegalArgumentException iae) {
            audioCoder = null;
        } catch (LineUnavailableException e) {
            return;
        }
    }
