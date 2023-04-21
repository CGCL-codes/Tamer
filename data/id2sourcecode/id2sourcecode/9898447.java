    public static Objeto3D load(URL url, int flags, Matrix4d mt) throws IOException, ParsingErrorException {
        return load(new InputStreamReader(url.openStream()), flags, mt);
    }
