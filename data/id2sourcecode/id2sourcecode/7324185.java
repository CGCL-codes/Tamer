    public boolean checkPassword(String password) {
        String A1 = getA1Digest(password);
        String digestValue = A1 + precomputedDigest;
        byte[] valueBytes = digestValue.getBytes();
        String serverDigest = null;
        synchronized (md5Helper) {
            serverDigest = md5Encoder.encode(md5Helper.digest(valueBytes));
        }
        return serverDigest.equals(clientDigest);
    }
