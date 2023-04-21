    @Override
    public Map<String, Object> getMetadata(ProgressMonitor mon) throws Exception {
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(resourceURI.toURL());
        AudioFormat audioFormat = fileFormat.getFormat();
        Map<String, Object> properties = new HashMap(audioFormat.properties());
        properties.put("encoding", audioFormat.getEncoding());
        properties.put("endianness", audioFormat.isBigEndian() ? "bigEndian" : "littleEndian");
        properties.put("channels", audioFormat.getChannels());
        properties.put("frame rate", audioFormat.getFrameRate());
        properties.put("bits", audioFormat.getSampleSizeInBits());
        return properties;
    }
