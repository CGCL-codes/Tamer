    private String getA1Digest(String password) {
        String digestValue = userName + ":" + realm + ":" + password;
        byte[] valueBytes = digestValue.getBytes();
        byte[] digest = null;
        synchronized (md5Helper) {
            digest = md5Helper.digest(valueBytes);
        }
        return md5Encoder.encode(digest);
    }
