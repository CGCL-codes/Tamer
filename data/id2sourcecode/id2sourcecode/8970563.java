    private InputStream getInputStream() throws IOException {
        if (currentSourceType == SourceType.URL) {
            return url.openStream();
        } else {
            inputStream.reset();
            return inputStream;
        }
    }
