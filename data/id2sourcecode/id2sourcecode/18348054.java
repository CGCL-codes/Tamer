    public static void writeUrlToTempFile(URL url, File file) throws IOException {
        ERXFileUtilities.writeInputStreamToFile(url.openStream(), file);
    }
