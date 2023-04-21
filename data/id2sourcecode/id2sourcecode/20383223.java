    public static String hash(String input, byte[] salt, String algorithm, String charsetName) {
        try {
            byte[] inputBytes;
            if (charsetName == null) inputBytes = input.getBytes(); else inputBytes = input.getBytes(charsetName);
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(salt);
            md.update(inputBytes);
            byte[] digest = md.digest();
            return digestToHash(digest);
        } catch (Exception e) {
            throw new HashingException("Hashing of input failed.", e);
        }
    }
