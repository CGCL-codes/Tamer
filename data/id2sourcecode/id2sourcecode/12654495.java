    @Override
    public URLConnection openConnection(URL url) throws IOException {
        return new URLConnectionForRMI(url);
    }
