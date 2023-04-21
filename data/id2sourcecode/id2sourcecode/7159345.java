    public void transform(URL url, OutputStream out) throws IOException {
        InputStream in = url.openStream();
        transform(in, out);
        in.close();
    }
