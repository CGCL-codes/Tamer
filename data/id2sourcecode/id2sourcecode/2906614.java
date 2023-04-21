    public static Clip openAudioStream(InputStream stream) throws Exception {
        if (stream == null) {
            String message = Logging.getMessage("nullValue.InputStreamIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        Clip clip = null;
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(WWIO.getBufferedInputStream(stream));
            AudioFormat format = ais.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                ais = AudioSystem.getAudioInputStream(tmp, ais);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(), ((int) ais.getFrameLength() * format.getFrameSize()));
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
        } finally {
            if (ais != null) {
                ais.close();
            }
        }
        return clip;
    }
