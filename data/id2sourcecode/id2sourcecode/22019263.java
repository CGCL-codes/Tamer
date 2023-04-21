    public XMLBeanInfo(final URL url) throws IOException {
        super();
        if (url == null) {
            throw new IllegalArgumentException();
        }
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(url.openStream());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
