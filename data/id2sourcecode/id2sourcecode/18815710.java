    public SF2Soundbank(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            readSoundbank(is);
        } finally {
            is.close();
        }
    }
