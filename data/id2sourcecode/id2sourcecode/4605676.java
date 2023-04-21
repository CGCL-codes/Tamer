    public Model read(String url, String base, String lang) {
        try {
            InputStream is = new URL(url).openStream();
            try {
                read(is, base, lang);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new WrappedIOException(e);
        }
        return this;
    }
