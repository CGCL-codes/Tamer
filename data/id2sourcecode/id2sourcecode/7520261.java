    private static java.io.InputStream getMEFromZipFile(String fileName) throws Exception {
        java.io.InputStream me = null;
        try {
            me = new java.io.FileInputStream(fileName);
        } catch (Exception ex) {
            URL url = new URL(fileName);
            me = url.openStream();
        }
        java.util.zip.ZipInputStream zf;
        zf = new java.util.zip.ZipInputStream(me);
        zf.getNextEntry();
        me = zf;
        return me;
    }
