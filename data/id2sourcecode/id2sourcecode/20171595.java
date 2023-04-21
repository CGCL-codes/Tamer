    public static String getHash(String text, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            byte temp[] = md.digest(text.getBytes());
            md = null;
            return new String(temp);
        } catch (Exception e) {
            throw new RuntimeException("Hash Algorithm \"" + algorithm + "\" not found!");
        }
    }
