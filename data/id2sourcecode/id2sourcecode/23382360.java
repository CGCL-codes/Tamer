    private static synchronized byte[] digestData(byte[] data) {
        md5Digest.reset();
        md5Digest.update(data);
        return md5Digest.digest();
    }
