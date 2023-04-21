    public static byte[] createChecksum(InputStream in, String algorithm) throws NoSuchAlgorithmException, IOException {
        if (algorithm == null) {
            throw new NullPointerException();
        }
        if (algorithm.equals("CRC32")) {
            return String.valueOf(createCRC32Checksum(in)).getBytes();
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        int length;
        while ((length = in.read(buffer)) > -1) {
            digest.update(buffer, 0, length);
        }
        in.close();
        return digest.digest();
    }
