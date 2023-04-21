    public static void copy(InputStream from, OutputStream to) {
        byte[] buf = new byte[512];
        int numRead;
        try {
            while ((numRead = from.read(buf)) != -1) to.write(buf, 0, numRead);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
