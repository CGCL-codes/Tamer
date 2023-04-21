    public static String md5SumOf(final String plaintext) throws RippleException {
        try {
            synchronized (MD5_DIGEST) {
                MD5_DIGEST.update(plaintext.getBytes(UTF_8));
            }
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RippleException(e);
        }
        byte[] digest = MD5_DIGEST.digest();
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
