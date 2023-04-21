    public static void playSoundBuffer(byte[] wavData) {
        jmri.jmrit.sound.WavBuffer wb = new jmri.jmrit.sound.WavBuffer(wavData);
        float sampleRate = wb.getSampleRate();
        int sampleSizeInBits = wb.getSampleSizeInBits();
        int channels = wb.getChannels();
        boolean signed = wb.getSigned();
        boolean bigEndian = wb.getBigEndian();
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        SourceDataLine line;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            log.error("line not supported: " + info);
            return;
        }
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException ex) {
            log.error("error opening line: " + ex);
            return;
        }
        line.start();
        line.write(wavData, 0, wavData.length);
    }
