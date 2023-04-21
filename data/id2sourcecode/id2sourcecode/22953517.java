    public static InputStream getTemplateInputStream(String templateDir, String path) throws IOException {
        URL url = BUNDLE.getEntry(templateDir + "/" + path);
        return url.openStream();
    }
