    private byte[] getDigest(byte[] def, Configuration conf) {
        try {
            MessageDigest alg = MessageDigest.getInstance(conf.digestAlgorithm);
            return alg.digest(def);
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException(e);
        }
    }
