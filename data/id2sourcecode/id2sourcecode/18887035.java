    public void getFileContent() throws IOException {
        byte[] buf = new byte[2045];
        FileOutputStream fos = new FileOutputStream(this.file);
        try {
            while (true) {
                int pad = rawIn.read();
                int size = rawIn.read() | (rawIn.read() << 8);
                if (size == 0) break;
                int block, readlen = 0;
                while ((block = rawIn.read(buf, readlen, size - readlen)) != -1) {
                    readlen += block;
                    if (readlen == size) break;
                }
                if (readlen != size) throw new IOException("stream closed");
                offset += readlen;
                fos.write(buf, 0, readlen);
                fos.flush();
                if (offset >= filesize) break;
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }
