    private static final byte[] rawFingerPrint(String type, String keyType, byte[] hostkey) {
        Digest dig = null;
        if ("md5".equals(type)) {
            dig = new MD5();
        } else if ("sha1".equals(type)) {
            dig = new SHA1();
        } else throw new IllegalArgumentException("Unknown hash type " + type);
        if ("ssh-rsa".equals(keyType)) {
        } else if ("ssh-dss".equals(keyType)) {
        } else throw new IllegalArgumentException("Unknown key type " + keyType);
        if (hostkey == null) throw new IllegalArgumentException("hostkey is null");
        dig.update(hostkey);
        byte[] res = new byte[dig.getDigestLength()];
        dig.digest(res);
        return res;
    }
