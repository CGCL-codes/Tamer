    byte[] readResource(URL url) throws IOException {
        InputStream in = new BufferedInputStream(url.openStream());
        try {
            int totalSize = 0;
            List<Buf> buffers = new LinkedList<Buf>();
            final int BUF_SIZE = 1024;
            byte[] buf = new byte[BUF_SIZE];
            int read;
            do {
                read = in.read(buf);
                if (read > 0) {
                    totalSize += read;
                    buffers.add(new Buf(buf, read));
                    buf = new byte[BUF_SIZE];
                }
            } while (read > 0);
            buf = new byte[totalSize];
            int pos = 0;
            for (Buf buf2 : buffers) {
                System.arraycopy(buf2.data, 0, buf, pos, buf2.length);
                pos += buf2.length;
            }
            return buf;
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                log.error("An exception while closing InputStream", ex);
            }
        }
    }
