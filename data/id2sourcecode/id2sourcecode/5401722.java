    public static PDDocument load(URL url, RandomAccess scratchFile) throws IOException {
        return load(url.openStream(), scratchFile);
    }
