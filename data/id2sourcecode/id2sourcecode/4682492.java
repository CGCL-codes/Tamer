    public static InputStream openFileFromJar(String fileName) throws IOException {
        String path = "/uk/co/whisperingwind/docs/" + fileName;
        URL url = path.getClass().getResource(path);
        return url.openStream();
    }
