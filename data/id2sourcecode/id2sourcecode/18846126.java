    public static int getNumberOfChannels(String soundFileName) throws IOException, UnsupportedAudioFileException {
        File soundFile = BlueSystem.findFile(soundFileName);
        AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(soundFile);
        AudioFormat format = aFormat.getFormat();
        return format.getChannels();
    }
