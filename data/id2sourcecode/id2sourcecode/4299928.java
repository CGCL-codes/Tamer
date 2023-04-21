    @Override
    public CorporaEntry store(InputStream inputStream, AudioFormat audioFormat, AudioFormat requiredFormat) {
        AudioInputStream audioIn = new AudioInputStream(new BufferedInputStream(inputStream), audioFormat, AudioSystem.NOT_SPECIFIED);
        if (audioFormat.getEncoding() != requiredFormat.getEncoding() || audioFormat.getSampleRate() != requiredFormat.getSampleRate() || audioFormat.getSampleSizeInBits() != requiredFormat.getSampleSizeInBits() || audioFormat.getChannels() != requiredFormat.getChannels() || audioFormat.getFrameSize() != requiredFormat.getFrameSize() || audioFormat.getFrameRate() != requiredFormat.getFrameRate() || audioFormat.isBigEndian() != requiredFormat.isBigEndian()) {
            LOG.debug("[doPost] Resampling");
            audioIn = new WamiResampleAudioInputStream(requiredFormat, audioIn);
        }
        CorporaEntry entry = store(audioIn);
        return entry;
    }
