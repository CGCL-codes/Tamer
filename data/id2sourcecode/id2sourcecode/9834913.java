    public static byte[] calculateNTOWFv2(String domain, String username, String password) {
        try {
            MessageDigest md4 = createMD4();
            md4.update(password.getBytes(UNICODE_ENCODING));
            Mac hmacMD5 = createHmacMD5(md4.digest());
            hmacMD5.update(username.toUpperCase().getBytes(UNICODE_ENCODING));
            hmacMD5.update(domain.getBytes(UNICODE_ENCODING));
            return hmacMD5.doFinal();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
