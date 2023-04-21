    public void captureAudio() {
        try {
            audioFormat = getAudioFormat();
            LOG.info("sample rate         " + sampleRate);
            LOG.info("channels            " + channels);
            LOG.info("sample size in bits " + sampleSizeInBits);
            LOG.info("signed              " + signed);
            LOG.info("bigEndian           " + bigEndian);
            LOG.info("data rate is " + sampleRate * sampleSizeInBits / 8 + " bytes per second");
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            buffer = new FloatSampleBuffer(targetDataLine.getFormat().getChannels(), bufferSize, targetDataLine.getFormat().getSampleRate());
            captureThread = new CaptureThread(this);
            captureThread.start();
        } catch (Exception e) {
            LOG.error(Service.stackToString(e));
        }
    }
