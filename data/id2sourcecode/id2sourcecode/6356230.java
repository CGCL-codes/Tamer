    private static final byte[] hmacSha1Hash(byte[] salt, String hostname) {
        SHA1 sha1 = new SHA1();
        if (salt.length != sha1.getDigestLength()) throw new IllegalArgumentException("Salt has wrong length (" + salt.length + ")");
        HMAC hmac = new HMAC(sha1, salt, salt.length);
        try {
            hmac.update(hostname.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException ignore) {
            hmac.update(hostname.getBytes());
        }
        byte[] dig = new byte[hmac.getDigestLength()];
        hmac.digest(dig);
        return dig;
    }
