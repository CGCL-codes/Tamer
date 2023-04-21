    public AudioInputStream convert(AudioInputStream stream, File temporaryFile) throws JavaLayerException, UnsupportedAudioFileException, IOException {
        if (!isPcm(stream.getFormat().getEncoding())) {
            stream = convertToPcm(stream);
        }
        if (stream.getFormat().getSampleRate() != targetFormat.getSampleRate()) {
            stream = convertToEqualsSampleRate(stream, targetFormat.getSampleRate());
        }
        if (stream.getFormat().getChannels() != targetFormat.getChannels()) {
            stream = convertChannels(stream, targetFormat.getChannels());
        }
        stream = saveToTemporaryFile(stream, temporaryFile);
        return stream;
    }
