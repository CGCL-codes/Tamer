    public AudioWriter newAudioWriter() {
        clipFile = newFilename();
        AudioFormat format = new AudioFormat(FrinikaConfig.sampleRate, 16, ((IOAudioProcess) audioInProcess).getChannelFormat().getCount(), true, false);
        try {
            return new AudioWriter(clipFile, format);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
