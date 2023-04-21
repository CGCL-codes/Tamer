    private static File createTempfile(InputStream is, String suffix) throws IOException {
        File file = File.createTempFile("xSocketTest", suffix);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] transfer = new byte[4096];
        int read = 0;
        do {
            read = is.read(transfer);
            if (read > 0) {
                fos.write(transfer, 0, read);
            }
        } while (read > 0);
        fos.close();
        return file;
    }
