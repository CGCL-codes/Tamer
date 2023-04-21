    public final com.lucene.store.InputStream openFile(String name) throws IOException {
        if (!fileExists(name)) throw new FileNotFoundException(base + name);
        java.io.InputStream raw = classLoader.getResourceAsStream(base + name + ".t");
        byte[] buf = new byte[128];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n;
        while ((n = raw.read(buf)) > 0) baos.write(buf, 0, n);
        final byte[] data = baos.toByteArray();
        class ResourceInputStream extends com.lucene.store.InputStream {

            int pos;

            ResourceInputStream() {
                length = data.length;
            }

            public final void readInternal(byte[] b, int offset, int len) throws IOException {
                System.arraycopy(data, pos, b, offset, len);
                pos += len;
            }

            public final void seekInternal(long pos) throws IOException {
                this.pos = (int) pos;
            }

            public void close() {
            }
        }
        return new ResourceInputStream();
    }
