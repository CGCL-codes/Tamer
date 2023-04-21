    protected Track[] doRead(URL url) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return this.doRead(inputStream);
        } finally {
            WWIO.closeStream(inputStream, url.toString());
        }
    }
