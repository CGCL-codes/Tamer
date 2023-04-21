    public static MD5Hash digest(byte[] data, int start, int len) {
        byte[] digest;
        MessageDigest digester = DIGESTER_FACTORY.get();
        digester.update(data, start, len);
        digest = digester.digest();
        return new MD5Hash(digest);
    }
