    @Override
    public void writeTo(OutputStream out, long offset, long length) throws IOException {
        randomAccessFile.seek(offset);
        byte[] buffer = new byte[32 * 1024];
        long remaining = length;
        while (remaining > 0) {
            int readed = randomAccessFile.read(buffer, 0, (int) Math.min(remaining, buffer.length));
            if (file.lastModified() != lastModified) throw new FileChangedException();
            if (readed == -1) {
                throw new IllegalArgumentException("randomAccessFile to short");
            }
            out.write(buffer, 0, readed);
            remaining -= readed;
        }
    }
