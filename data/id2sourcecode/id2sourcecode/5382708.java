    private int getAudioFormatBytesPerSecond() {
        int bps = audioFormat.getChannels();
        bps *= audioFormat.getSampleRate();
        bps *= (audioFormat.getSampleSizeInBits() / 8);
        return bps;
    }
