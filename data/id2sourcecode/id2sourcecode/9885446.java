    public static long createCRC32Checksum(URL url) throws IOException {
        InputStream in = url.openStream();
        try {
            return createCRC32Checksum(in);
        } finally {
            in.close();
        }
    }
