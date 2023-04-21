    private String getSHA1(byte[] input) throws RampartException {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            throw new RampartException("noSHA1availabe", e1);
        }
        sha.reset();
        sha.update(input);
        byte[] data = sha.digest();
        return Base64.encode(data);
    }
