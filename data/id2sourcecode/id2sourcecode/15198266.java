    public static Reader createReader(URL url, Charset charset) throws IOException {
        final InputStreamReader reader = new InputStreamReader(url.openStream(), charset);
        return reader;
    }
