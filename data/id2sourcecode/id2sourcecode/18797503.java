    public static final void play(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(path);
        if (file.exists()) {
            AudioInputStream in = null;
            try {
                in = AudioSystem.getAudioInputStream(file);
                AudioInputStream din = null;
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                rawplay(decodedFormat, din);
            } finally {
                Closeables.closeQuietly(in);
            }
        }
    }
