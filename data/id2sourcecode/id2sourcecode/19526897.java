    protected void readInputStream(OutputStream anOS, InputStream anIS) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int read;
        while ((read = anIS.read(buffer)) >= 0) {
            anOS.write(buffer, 0, read);
        }
        anOS.flush();
    }
