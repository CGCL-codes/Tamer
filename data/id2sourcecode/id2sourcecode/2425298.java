    public JavaSoundDecoderStream(final File file) throws IOException {
        _file = file;
        try {
            _fileFormat = AudioSystem.getAudioFileFormat(_file);
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e);
        }
        final AudioFormat baseFormat = _fileFormat.getFormat();
        LOGGER.log(Level.FINE, "Source Format: " + baseFormat);
        int sampleSize;
        AudioFormat.Encoding encoding;
        if (baseFormat.getSampleSizeInBits() == 8) {
            sampleSize = 8;
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
        } else {
            sampleSize = 16;
            encoding = AudioFormat.Encoding.PCM_SIGNED;
        }
        _decodedFormat = new AudioFormat(encoding, baseFormat.getSampleRate(), sampleSize, baseFormat.getChannels(), baseFormat.getChannels() * sampleSize / 8, baseFormat.getSampleRate(), false);
        resetStream();
    }
