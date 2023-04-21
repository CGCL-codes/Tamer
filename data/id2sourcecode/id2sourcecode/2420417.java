    public byte[] hash(byte[] inputData) throws NestedException {
        assert inputData.length != 0;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            return sha.digest(inputData);
        } catch (NoSuchAlgorithmException ex) {
            throw new NestedException("Error loading SHA Algorithm.", ex);
        }
    }
