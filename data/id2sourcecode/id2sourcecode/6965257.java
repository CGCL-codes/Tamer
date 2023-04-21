    public AudioFileFormat getAudioFileFormat(URL url) throws IOException, UnsupportedAudioFileException {
        return getAudioFileFormat(new BufferedInputStream(url.openStream()));
    }
