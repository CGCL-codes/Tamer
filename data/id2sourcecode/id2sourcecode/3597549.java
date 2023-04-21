    protected static void readFully(RFile file, int expected) throws IOException {
        InputStream in = new RFileInputStream(file);
        int count = IOUtils.toByteArray(in).length;
        in.close();
        if (count != expected) {
            throw new IOException("Expected " + expected + " bytes but read " + count);
        }
    }
