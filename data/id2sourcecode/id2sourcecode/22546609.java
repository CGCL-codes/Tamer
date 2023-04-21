    @Override
    public void onMediaTransfer(Buffer buffer) throws IOException {
        if (first) {
            first = false;
            AudioFormat fmt = (AudioFormat) buffer.getFormat();
            float sampleRate = (float) fmt.getSampleRate();
            int sampleSizeInBits = fmt.getSampleSizeInBits();
            int channels = fmt.getChannels();
            int frameSize = (fmt.getFrameSizeInBits() / 8);
            float frameRate = (float) fmt.getFrameRate();
            boolean bigEndian = fmt.getEndian() == 1;
            Encoding encoding = getEncoding(fmt.getEncoding());
            frameSize = (channels == AudioSystem.NOT_SPECIFIED || sampleSizeInBits == AudioSystem.NOT_SPECIFIED) ? AudioSystem.NOT_SPECIFIED : ((sampleSizeInBits + 7) / 8) * channels;
            audioFormat = new javax.sound.sampled.AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, sampleRate, bigEndian);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
            } catch (LineUnavailableException e) {
                logger.error(e);
                this.failed(NotifyEvent.RX_FAILED, e);
                this.stop();
            } catch (IllegalArgumentException e) {
                logger.error(e);
                this.failed(NotifyEvent.RX_FAILED, e);
                this.stop();
            }
        }
        byte[] data = buffer.getData();
        try {
            sourceDataLine.write(data, buffer.getOffset(), buffer.getLength());
        } catch (IllegalArgumentException e) {
            logger.error(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e);
        }
    }
