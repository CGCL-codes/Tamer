    @Override
    public void onMediaTransfer(Frame frame) throws IOException {
        System.out.println("Receive " + frame.getFormat() + ", len=" + frame.getLength() + ", header=" + frame.getHeader());
        if (first) {
            first = false;
            AudioFormat fmt = (AudioFormat) frame.getFormat();
            if (fmt == null) {
                return;
            }
            float sampleRate = (float) fmt.getSampleRate();
            int sampleSizeInBits = fmt.getSampleSize();
            int channels = fmt.getChannels();
            int frameSize = (fmt.getSampleSize() / 8);
            float frameRate = 1;
            boolean bigEndian = false;
            Encoding encoding = getEncoding(fmt.getName().toString());
            frameSize = (channels == AudioSystem.NOT_SPECIFIED || sampleSizeInBits == AudioSystem.NOT_SPECIFIED) ? AudioSystem.NOT_SPECIFIED : ((sampleSizeInBits + 7) / 8) * channels;
            audioFormat = new javax.sound.sampled.AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, sampleRate, bigEndian);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
            } catch (LineUnavailableException e) {
                this.stop();
            } catch (IllegalArgumentException e) {
                this.stop();
            }
        }
        byte[] data = frame.getData();
        try {
            sourceDataLine.write(data, frame.getOffset(), frame.getLength());
        } catch (IllegalArgumentException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
