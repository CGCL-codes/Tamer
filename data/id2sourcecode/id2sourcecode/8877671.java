    public static InputSource getInputSource(String addr, String charset) throws IOException, MalformedURLException {
        URL url = new URL(addr);
        InputSource source = null;
        source = new InputSource(new BufferedInputStream(url.openStream(), BUF_SIZE));
        source.setEncoding(charset);
        return source;
    }
