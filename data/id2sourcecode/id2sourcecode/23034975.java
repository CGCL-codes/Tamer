    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fileFormat = null;
        InputStream urlStream = url.openStream();
        try {
            fileFormat = getCOMM(urlStream, false);
        } finally {
            urlStream.close();
        }
        return fileFormat;
    }
