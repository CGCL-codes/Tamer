    private byte[] getMD5Digest(byte[] values) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.update(values, 0, values.length);
        return complete.digest();
    }
