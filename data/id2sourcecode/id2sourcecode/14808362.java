    public synchronized byte[] getDigest(String aKey, MessageDigest algorithm) throws java.io.UnsupportedEncodingException {
        byte[] key = aKey.getBytes("UTF-8");
        algorithm.reset();
        algorithm.update(key);
        byte[] digest = algorithm.digest();
        return digest;
    }
