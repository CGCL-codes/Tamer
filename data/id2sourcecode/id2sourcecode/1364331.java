    private static AudioFormat getChanneledAudioFormat(AudioFormat audioFormat, int nChannels) {
        AudioFormat channeledAudioFormat = new AudioFormat(audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), nChannels, (audioFormat.getSampleSizeInBits() / 8) * nChannels, audioFormat.getFrameRate(), audioFormat.isBigEndian());
        return channeledAudioFormat;
    }
