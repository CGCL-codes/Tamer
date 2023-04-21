    public static String sha1SumOf(final String plaintext) throws RippleException {
        try {
            synchronized (SHA1_DIGEST) {
                SHA1_DIGEST.update(plaintext.getBytes(UTF_8));
            }
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RippleException(e);
        }
        byte[] digest = SHA1_DIGEST.digest();
        String coded = "";
        for (byte b : digest) {
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hex = hex.substring(hex.length() - 2);
            coded += hex;
        }
        return coded;
    }
