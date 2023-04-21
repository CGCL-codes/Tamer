    protected byte[] toAESKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = myKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return key;
    }
