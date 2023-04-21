    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(url.openStream());
    }
