    private void setFormat(AudioFormat format) {
        if (format.getChannels() > 2) {
            throw new IllegalArgumentException("Only mono and stereo audio supported.");
        }
        if (AudioFloatConverter.getConverter(format) == null) throw new IllegalArgumentException("Audio format not supported.");
        this.format = format;
    }
