    private AudioFormat convertFormat(AudioFormat format, boolean changeSign, boolean changeEndian) {
        AudioFormat.Encoding enc = PCM_SIGNED;
        if (format.getEncoding().equals(PCM_UNSIGNED) != changeSign) {
            enc = PCM_UNSIGNED;
        }
        return new AudioFormat(enc, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), format.isBigEndian() ^ changeEndian);
    }
