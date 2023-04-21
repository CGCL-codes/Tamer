    public void pump(InputStream is, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count;
        while ((count = is.read(buffer, 0, buffer.length)) >= 0) this.write(buffer, 0, count);
    }
