    public String md5string(final String password) {
        final String digits = "0123456789abcdef";
        final StringBuffer rValue = new StringBuffer();
        byte[] b;
        try {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            b = md.digest(password.getBytes());
        } catch (final java.security.NoSuchAlgorithmException e) {
            b = "".getBytes();
        }
        for (byte i = 0; i < b.length; i++) {
            rValue.append(digits.charAt((b[i] >> 4) & 0x0f));
            rValue.append(digits.charAt(b[i] & 0x0f));
        }
        return rValue.toString();
    }
