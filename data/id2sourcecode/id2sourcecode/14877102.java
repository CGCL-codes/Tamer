    public void save(String target, InputStream data, int bufferSize) throws IOException {
        OutputStream writer = new FileOutputStream(target);
        byte[] bytes = new byte[bufferSize];
        int readn = -1;
        while ((readn = data.read(bytes)) != -1) {
            writer.write(bytes, 0, readn);
        }
        writer.flush();
        writer.close();
    }
