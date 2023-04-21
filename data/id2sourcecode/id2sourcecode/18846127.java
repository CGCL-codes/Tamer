    public static float getDurationInSeconds(String soundFileName) throws IOException, UnsupportedAudioFileException {
        File soundFile = BlueSystem.findFile(soundFileName);
        AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(soundFile);
        AudioFormat format = aFormat.getFormat();
        float duration = aFormat.getByteLength() / (format.getSampleRate() * (format.getSampleSizeInBits() / 8) * format.getChannels());
        return duration;
    }
