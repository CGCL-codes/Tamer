    public static void writeInputToStream(InputStream is, OutputStream os) {
        if (is == null) throw new IllegalArgumentException("is == null");
        if (os == null) throw new IllegalArgumentException("os == null");
        byte[] bytes = new byte[128];
        int size;
        try {
            while ((size = is.read(bytes)) != -1) os.write(bytes, 0, size);
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
