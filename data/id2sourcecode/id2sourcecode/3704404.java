    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(new BufferedInputStream(url.openStream()));
    }
