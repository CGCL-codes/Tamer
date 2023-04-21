    public static byte[] fromPlainToDigest(byte[] passwd, String alg) throws NoSuchAlgorithmException {
        MessageDigest digestAlgorithm = MessageDigest.getInstance(alg);
        byte checkSum[] = null;
        digestAlgorithm.reset();
        digestAlgorithm.update(passwd);
        checkSum = digestAlgorithm.digest();
        digestAlgorithm.reset();
        return checkSum;
    }
