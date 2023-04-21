    private byte[] readURL(URL url) throws IOException {
        return readInput(new BufferedInputStream(url.openStream()));
    }
