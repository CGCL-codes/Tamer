    private static String fromInputStrem(InputStream stream, MessageDigest algo) throws IOException {
        try {
            byte[] buf = new byte[1 * 1024 * 1024];
            int len = stream.read(buf);
            while (len > 0) {
                algo.update(buf, 0, len);
                len = stream.read(buf);
            }
            return asHex(algo.digest());
        } finally {
            stream.close();
        }
    }
