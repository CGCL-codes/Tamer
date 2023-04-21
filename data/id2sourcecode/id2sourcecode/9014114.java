    public Reader getReader(URL url) throws IOException {
        if (url != null) {
            return new InputStreamReader(url.openStream());
        } else {
            return null;
        }
    }
