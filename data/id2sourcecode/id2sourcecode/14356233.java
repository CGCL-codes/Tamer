    public static long getMD5Checksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(file);
        try {
            is = new DigestInputStream(is, md);
        } finally {
            is.close();
        }
        byte[] digest = md.digest();
        return new BigInteger(1, digest).longValue();
    }
