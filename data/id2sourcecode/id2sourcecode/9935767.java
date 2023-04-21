    public static InputStream openInputStream(String resource) {
        InputStream in = null;
        URL url = findResource(resource);
        if (url != null) {
            try {
                in = url.openStream();
            } catch (IOException e) {
            }
        }
        return in;
    }
