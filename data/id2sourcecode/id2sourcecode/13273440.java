    public static void writeInputStreamToFile(InputStream io, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        try {
            byte[] buf = new byte[256];
            int read = 0;
            while ((read = io.read(buf)) > 0) {
                fos.write(buf, 0, read);
            }
        } catch (IOException ex) {
            log.error("Problem writing stream to file", ex);
            throw ex;
        } finally {
            if (fos != null) fos.close();
        }
    }
