    protected static byte[] buildByteArray(String secret) {
        byte buf[] = secret.getBytes();
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance(SHA_1);
        } catch (NoSuchAlgorithmException e) {
        }
        if (algorithm == null) {
            buf = null;
        } else {
            algorithm.reset();
            algorithm.update(buf);
            buf = algorithm.digest();
        }
        return buf;
    }
