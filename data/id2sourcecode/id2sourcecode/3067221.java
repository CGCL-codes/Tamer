    private InputStream getResourceInputStream(String path) throws IOException {
        URL url = getClass().getResource(path);
        if (url != null) return url.openStream(); else return new FileInputStream(new File("." + path));
    }
