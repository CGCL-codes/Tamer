    private byte[] getHash(String algo, byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algo);
        return md.digest(data);
    }
