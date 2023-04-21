    public static File getFile(String filename, boolean override) throws MdnException {
        InputStream is = null;
        OutputStream os = null;
        if (filename == null) throw new MdnException("error.FileNotFoundException", new FileNotFoundException());
        File outputFile = new File(filename);
        if (outputFile.exists() && override == false) return outputFile;
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        if (url == null) throw new MdnException("error.FileNotFoundException", new FileNotFoundException(filename));
        try {
            outputFile = writeFile(url.openStream(), filename);
        } catch (IOException e) {
            throw new MdnException("error.IOException", e);
        }
        return outputFile;
    }
