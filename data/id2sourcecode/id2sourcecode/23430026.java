    public static InputStream getResourceAsStream(String name) throws FileNotFoundException {
        URL url = getResource(name);
        if (url == null) throw new FileNotFoundException(name);
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new FileNotFoundException(name + " (Error occured while opening file)");
        }
    }
