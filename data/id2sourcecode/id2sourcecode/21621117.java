    protected byte[] getSHA1(byte[] digest) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
