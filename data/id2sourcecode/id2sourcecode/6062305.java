    public byte[] generateFingerprint(byte[] ba, Algorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return md.digest(ba);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return null;
    }
