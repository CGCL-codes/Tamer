    public Charset detectCodepage(URL url) throws IOException {
        return this.detectCodepage(new BufferedInputStream(url.openStream()), Integer.MAX_VALUE);
    }
