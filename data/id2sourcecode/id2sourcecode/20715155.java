    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        InputStream is = null;
        if (url != null) {
            try {
                is = url.openStream();
            } catch (IOException e) {
            }
        }
        return is;
    }
