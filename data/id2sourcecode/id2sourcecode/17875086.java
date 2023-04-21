    public PeerID(byte[] b, boolean hash) {
        if (hash) {
            MessageDigest md = CryptoUtils.getMessageDigest();
            id = md.digest(b);
        } else {
            id = b;
        }
    }
