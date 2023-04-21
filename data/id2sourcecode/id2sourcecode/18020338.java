    public void setInput(URL url) throws IOException {
        setBaseURL(url);
        setInput(new ANTLRInputStream(url.openStream()));
    }
