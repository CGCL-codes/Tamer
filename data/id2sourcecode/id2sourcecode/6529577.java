    public int getFormat() {
        if (_audioFormat != null) {
            if (_audioFormat.getChannels() == 1 && _audioFormat.getSampleSizeInBits() == 8) {
                return FORMAT_8BIT_MONO;
            } else if (_audioFormat.getChannels() == 1 && _audioFormat.getSampleSizeInBits() == 16) {
                return FORMAT_16BIT_MONO;
            } else if (_audioFormat.getChannels() == 2 && _audioFormat.getSampleSizeInBits() == 8) {
                return FORMAT_8BIT_STEREO;
            } else if (_audioFormat.getChannels() == 2 && _audioFormat.getSampleSizeInBits() == 16) {
                return FORMAT_16BIT_STEREO;
            } else {
                return FORMAT_UNKNOWN;
            }
        }
        return FORMAT_UNKNOWN;
    }
