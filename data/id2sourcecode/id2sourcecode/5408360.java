    static void copy(InputStream source, OutputStream dest) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int read = 0;
            while (read >= 0) {
                dest.write(buffer, 0, read);
                read = source.read(buffer);
            }
            dest.flush();
        } finally {
            closeStream(source);
            closeStream(dest);
        }
    }
