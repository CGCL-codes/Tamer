    public SampleThread(String fileName, float volume, boolean wert) throws FormatProblemException {
        super();
        this.volume = volume;
        try {
            in = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        din = null;
        try {
            AudioFormat baseFormat = in.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            play = true;
        } catch (NullPointerException e) {
            throw new FormatProblemException();
        }
    }
