    private static synchronized byte[] digestData(InputStream data) {
        md5Digest.reset();
        int b;
        try {
            while (((b = data.read()) >= 0)) {
                md5Digest.update((byte) b);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return md5Digest.digest();
    }
