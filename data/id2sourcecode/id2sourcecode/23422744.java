    private void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[4096];
        for (int read = from.read(buffer); read > -1; read = from.read(buffer)) {
            to.write(buffer, 0, read);
        }
    }
