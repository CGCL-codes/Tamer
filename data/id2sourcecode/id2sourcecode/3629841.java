    public static String getHashcode(final InputStream file, final String alg) throws NoSuchAlgorithmException, IOException {
        byte[] msg = IOUtils.toByteArray(file);
        MessageDigest md = MessageDigest.getInstance(alg);
        md.update(msg);
        byte[] digest = md.digest();
        return new BigInteger(1, digest).toString(16);
    }
