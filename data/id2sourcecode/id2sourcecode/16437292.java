    public void addClip(String s) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (samples.containsKey(s)) {
            return;
        }
        URL url = getClass().getResource(s);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(loadStream(url.openStream()));
        Sample sample = new Sample();
        sample.format = audioInputStream.getFormat();
        sample.size = (int) (sample.format.getFrameSize() * audioInputStream.getFrameLength());
        sample.audio = new byte[sample.size];
        sample.info = new DataLine.Info(Clip.class, sample.format, sample.size);
        audioInputStream.read(sample.audio, 0, sample.size);
        samples.put(s, sample);
    }
