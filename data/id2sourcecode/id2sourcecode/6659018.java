    public static byte[] generateKeyID(Key key) {
        if (null == key) return null;
        return CCNDigestHelper.digest(key.getEncoded());
    }
