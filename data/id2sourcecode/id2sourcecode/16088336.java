    private static Map<String, Object> createProperties(AudioSource source) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat audioFileFormat;
        Map<String, Object> properties = new HashMap<String, Object>();
        if (source.getFile() != null) {
            audioFileFormat = AudioSystem.getAudioFileFormat(source.getFile());
        } else if (source.getStream() != null) {
            audioFileFormat = AudioSystem.getAudioFileFormat(source.getStream());
        } else return properties;
        if (audioFileFormat instanceof TAudioFileFormat) {
            properties = GenericsUtils.scanForMap(((TAudioFileFormat) audioFileFormat).properties(), String.class, Object.class, ScanMode.REMOVE);
            Map<String, Object> newMap = new HashMap<String, Object>(properties);
            properties = newMap;
        }
        if (audioFileFormat.getByteLength() > 0) properties.put(AUDIO_LENGTH_BYTES, audioFileFormat.getByteLength());
        if (audioFileFormat.getFrameLength() > 0) properties.put(AUDIO_LENGTH_FRAMES, audioFileFormat.getFrameLength());
        if (audioFileFormat.getType() != null) properties.put(AUDIO_TYPE, (audioFileFormat.getType().toString()));
        AudioFormat audioFormat = audioFileFormat.getFormat();
        if (audioFormat.getFrameRate() > 0) properties.put(AUDIO_FRAMERATE_FPS, audioFormat.getFrameRate());
        if (audioFormat.getFrameSize() > 0) properties.put(AUDIO_FRAMESIZE_BYTES, audioFormat.getFrameSize());
        if (audioFormat.getSampleRate() > 0) properties.put(AUDIO_SAMPLERATE_HZ, audioFormat.getSampleRate());
        if (audioFormat.getSampleSizeInBits() > 0) properties.put(AUDIO_SAMPLESIZE_BITS, audioFormat.getSampleSizeInBits());
        if (audioFormat.getChannels() > 0) properties.put(AUDIO_CHANNELS, audioFormat.getChannels());
        if (audioFormat instanceof TAudioFormat) {
            Map<String, Object> addproperties = GenericsUtils.scanForMap(((TAudioFormat) audioFormat).properties(), String.class, Object.class, ScanMode.REMOVE);
            properties.putAll(addproperties);
        }
        return properties;
    }
