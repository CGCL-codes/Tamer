    private Clip createClip() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        Clip clip = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getAudioData());
            AudioFormat format = ais.getFormat();
            AudioFormat.Encoding encoding = format.getEncoding();
            if ((encoding == AudioFormat.Encoding.ULAW) || (encoding == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                ais = AudioSystem.getAudioInputStream(tmp, ais);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            ais.close();
        } catch (IllegalArgumentException iae) {
            throw new VBoxException(iae.getMessage());
        }
        return clip;
    }
