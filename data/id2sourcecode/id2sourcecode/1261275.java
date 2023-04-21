    public SHA1Sum(byte[] bytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(bytes, 0, bytes.length);
        sha1sum = digest.digest();
    }
