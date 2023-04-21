    public static void unpackArchive(URL url, File targetDir) throws IOException {
        InputStream is = url.openStream();
        try {
            unpackArchive(is, targetDir);
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
        }
    }
