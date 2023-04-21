    public static String getChecksum(String algorithm, InputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        MessageDigest checksum = null;
        try {
            checksum = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        }
        int numRead;
        do {
            numRead = stream.read(buffer);
            if (numRead > 0) {
                checksum.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        stream.close();
        return convertToHex(checksum.digest());
    }
