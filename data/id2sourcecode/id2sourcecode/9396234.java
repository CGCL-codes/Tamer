    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream in = url.openStream();
        try {
            return getAudioInputStream(in);
        } finally {
            if (in != null) in.close();
        }
    }
