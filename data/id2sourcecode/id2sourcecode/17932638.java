    public SampleThread(String fileName, float volume) throws FormatProblemException {
        super();
        this.volume = volume;
        File file = new File(fileName);
        try {
            in = AudioSystem.getAudioInputStream(file);
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
