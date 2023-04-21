    public AudioInputStream getAudioInputStream(URL url) throws IOException, UnsupportedAudioFileException {
        return getAudioInputStream(new BufferedInputStream(url.openStream()));
    }
