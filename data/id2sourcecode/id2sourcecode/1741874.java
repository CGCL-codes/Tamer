    public static InputStream findFileAsInputStream(String fileName) throws IOException {
        File findedFile = findFile(fileName);
        if (findedFile != null) return new FileInputStream(findedFile);
        URL url = SimComUtil.findFileInConfigBundle(fileName);
        if (url != null) return url.openStream();
        throw new RuntimeException("File not found for " + fileName);
    }
