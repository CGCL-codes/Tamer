    public static String toMd5(String target, String charsetName) throws UnsupportedEncodingException {
        try {
            final byte[] md5 = MessageDigest.getInstance("MD5").digest(target.getBytes(charsetName));
            final char[] md5Chars = new char[32];
            int i = 0;
            for (final byte b : md5) {
                md5Chars[i++] = HEXDIGITS[(b >> 4) & 0xF];
                md5Chars[i++] = HEXDIGITS[b & 0xF];
            }
            return new String(md5Chars);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("No MD5 algorithm, unable to compute MD5");
        }
    }
