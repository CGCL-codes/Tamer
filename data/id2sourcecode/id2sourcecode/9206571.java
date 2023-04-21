    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream is = url.openStream();
        try {
            return getAudioFileFormat(is);
        } finally {
            is.close();
        }
    }
