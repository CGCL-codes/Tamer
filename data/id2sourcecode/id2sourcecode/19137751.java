    public void loadFromFile() {
        XBWave wave = new XBWave();
        wave.loadFromFile(new File(getFileName()));
        wavePanel.setWave(wave);
        targetFormat = wavePanel.getWave().getAudioFormat();
        targetDataLineInfo = new DataLine.Info(SourceDataLine.class, wavePanel.getWave().getAudioFormat());
        audioInputStream = wavePanel.getWave().getAudioInputStream();
        if (!AudioSystem.isLineSupported(targetDataLineInfo)) {
            AudioFormat pcm = new AudioFormat(targetFormat.getSampleRate(), 16, targetFormat.getChannels(), true, false);
            audioInputStream = AudioSystem.getAudioInputStream(pcm, audioInputStream);
            targetFormat = audioInputStream.getFormat();
            targetDataLineInfo = new DataLine.Info(SourceDataLine.class, targetFormat);
        }
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(targetDataLineInfo);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
