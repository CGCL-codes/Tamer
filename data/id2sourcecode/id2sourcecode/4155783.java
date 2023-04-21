    public JMultipartHttpFilePost(URL url) throws IOException {
        this(url.openConnection());
    }
