    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        final InputStream in = url.openStream();
        try {
            return getAudioInputStream(in);
        } catch (UnsupportedAudioFileException e) {
            if (in != null) in.close();
            throw e;
        } catch (IOException e) {
            if (in != null) in.close();
            throw e;
        }
    }
