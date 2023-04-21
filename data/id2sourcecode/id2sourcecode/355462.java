    private String hash(String password) throws UnsupportedEncodingException {
        final StringBuilder hashString = new StringBuilder();
        byte[] plaintext = password.getBytes("UTF-8");
        byte[] ciphertext;
        try {
            MessageDigest hashAlgo = MessageDigest.getInstance("MD5");
            hashAlgo.update(plaintext);
            ciphertext = hashAlgo.digest();
            for (byte block : ciphertext) {
                hashString.append(Character.forDigit((block >> 4) & 0xf, 16));
                hashString.append(Character.forDigit(block & 0xf, 16));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not find hashing algorithm. " + e.getMessage());
        }
        return null;
    }
