    public ViewerAudioClip(URL url) {
        AudioFormat af = null;
        AudioInputStream ais = null;
        SourceDataLine line = null;
        try {
            ais = AudioSystem.getAudioInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        af = ais.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        boolean isSupported = AudioSystem.isLineSupported(info);
        if (!isSupported) {
            AudioFormat tf = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), 16, af.getChannels(), af.getChannels() << 1, af.getSampleRate(), false);
            ais = AudioSystem.getAudioInputStream(tf, ais);
            af = ais.getFormat();
            info = new DataLine.Info(SourceDataLine.class, af);
        }
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clip = new ClipImpl(af, ais, line);
    }
