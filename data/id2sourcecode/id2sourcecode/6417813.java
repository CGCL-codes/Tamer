    public Source(final URL url) throws IOException {
        this(new EncodingDetector(url.openConnection()));
    }
