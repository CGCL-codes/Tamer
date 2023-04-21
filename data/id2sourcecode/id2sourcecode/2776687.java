    public String checksum(String string, String algorithm) throws NoSuchAlgorithmException, IOException {
        InputStream strin = new ByteArrayInputStream(string.getBytes());
        byte[] buffer = new byte[128];
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        int i;
        do {
            i = strin.read(buffer);
            if (i > 0) {
                digest.update(buffer, 0, i);
            }
        } while (i != -1);
        return toHexString(digest.digest());
    }
