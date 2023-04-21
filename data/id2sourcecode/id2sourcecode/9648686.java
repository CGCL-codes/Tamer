    @Override
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        final InputStream in = url.openStream();
        try {
            return getAudioFileFormat(in);
        } finally {
            if (in != null) in.close();
        }
    }
