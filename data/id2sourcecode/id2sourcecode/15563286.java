    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream urlStream = null;
        BufferedInputStream bis = null;
        AudioFileFormat fileFormat = null;
        AudioFormat format = null;
        urlStream = url.openStream();
        try {
            bis = new BufferedInputStream(urlStream, bisBufferSize);
            fileFormat = getAudioFileFormat(bis);
        } finally {
            urlStream.close();
        }
        return fileFormat;
    }
