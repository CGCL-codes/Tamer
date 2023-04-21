    public static void streamFile(OutputStream os, InputStream is, int bufSize) throws IOException {
        byte[] data = new byte[bufSize];
        try {
            int size = -1;
            while ((size = is.read(data)) > 0) os.write(data, 0, size);
        } finally {
            os.flush();
            CloseUtils.safeClose(os);
            CloseUtils.safeClose(is);
        }
    }
