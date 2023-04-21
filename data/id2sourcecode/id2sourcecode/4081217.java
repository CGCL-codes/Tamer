    public Reader getReader() throws IOException {
        if (null != file) {
            return new FileReader(file);
        } else {
            return new InputStreamReader(url.openStream());
        }
    }
