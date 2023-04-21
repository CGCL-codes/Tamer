    public AudioBuffer(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream aistream = AudioSystem.getAudioInputStream(file);
        sampleRate = aistream.getFormat().getSampleRate();
        int chs = aistream.getFormat().getChannels();
        AudioFormat format = new AudioFormat(sampleRate, 16, chs, true, true);
        DataInputStream distream = new DataInputStream(new BufferedInputStream(AudioSystem.getAudioInputStream(format, aistream)));
        int n = (int) aistream.getFrameLength();
        data = new double[chs][];
        for (int i = 0; i < chs; ++i) data[i] = new double[n];
        for (int j = 0; j < n; ++j) for (int i = 0; i < chs; ++i) data[i][j] = distream.readShort() / ((double) -Short.MIN_VALUE);
    }
