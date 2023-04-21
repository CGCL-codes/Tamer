    public static Guid SHA1(String stuff) {
        Guid guid = null;
        MessageDigest digest = Crypto.messageDigestThreadLocal.get();
        digest.reset();
        digest.update(stuff.getBytes());
        byte[] key = digest.digest();
        guid = new Guid();
        guid.setFrom20bytes(key, 0);
        return guid;
    }
