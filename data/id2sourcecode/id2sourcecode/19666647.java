    public PngImage(URL url) throws IOException {
        InputStream is = url.openConnection().getInputStream();
        init(new BufferedInputStream(is, BUFFER_SIZE));
    }
