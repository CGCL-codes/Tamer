    private byte[] getHash(byte[] entrada, String hashFunction) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(hashFunction);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
        return md.digest(entrada);
    }
