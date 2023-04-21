    public static String getChecksum(File file, String algorithm) throws IOException {
        if (algorithm.equals(Constants.DUMMY_CHECKSUM_ALGORITHM)) {
            if (file.exists() && file.isFile()) {
                return Constants.DUMMY_CHECKSUM_VALUE;
            }
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        MessageDigest checksum;
        try {
            checksum = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Given algorithm is not currently supported.", e);
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
