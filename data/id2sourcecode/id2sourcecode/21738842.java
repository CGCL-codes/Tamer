    public boolean verifyHash(final byte[] hash) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(fPhoto);
            sha1hash = md.digest();
            final String actualHash = ByteConverter.hexify(sha1hash);
            return actualHash.equals(ByteConverter.hexify(hash));
        } catch (NoSuchAlgorithmException e) {
        }
        throw new IllegalStateException();
    }
