    public InputStream getInputStream() throws IOException {
        if (isCached()) {
            return new ByteArrayInputStream(pictureData);
        } else {
            return url.openConnection().getInputStream();
        }
    }
