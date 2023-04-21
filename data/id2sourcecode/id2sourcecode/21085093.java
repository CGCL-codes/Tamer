    public String createHash(String argument) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(argument.getBytes());
        return convertToHexValue(digest);
    }
