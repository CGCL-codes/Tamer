    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("getAudioInputStream(URL url)");
        }
        InputStream inputStream = url.openStream();
        try {
            return getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException e) {
            if (inputStream != null) {
                inputStream.close();
            }
            throw e;
        } catch (IOException e) {
            if (inputStream != null) {
                inputStream.close();
            }
            throw e;
        }
    }
