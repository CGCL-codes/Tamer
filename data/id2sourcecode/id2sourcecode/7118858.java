    public BufferedReader getInputStream() throws IOException {
        String encoding = getEncoding();
        return new BufferedReader(new InputStreamReader(url.openStream(), encoding));
    }
