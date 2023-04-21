    public AudioClip(URL url) throws IOException {
        this.url = url;
        InputStream in = url.openStream();
        this.data = IOUtilities.readToEnd(in);
        in.close();
    }
