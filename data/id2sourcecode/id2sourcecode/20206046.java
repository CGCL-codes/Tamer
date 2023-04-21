    static AudioFormat newFormat(AudioInputStream in) {
        AudioFormat inFormat = in.getFormat();
        return new AudioFormat(inFormat.getEncoding(), 2 * inFormat.getSampleRate(), inFormat.getSampleSizeInBits(), inFormat.getChannels(), inFormat.getFrameSize(), 2 * inFormat.getFrameRate(), inFormat.isBigEndian());
    }
