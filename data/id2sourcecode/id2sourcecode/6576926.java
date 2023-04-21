    private synchronized SecretKeySpec getLocalSecretKey() throws InvalidKeySpecException, UnsupportedEncodingException {
        String ck = new String(md5.digest("$b1ame$is$n0game$".getBytes("ISO-8859-1")), "ISO-8859-1") + new String(sh1.digest("$b1ame$is$n0game$".getBytes("ISO-8859-1")), "ISO-8859-1");
        byte[] key = new byte[16];
        System.arraycopy(ck.getBytes("ISO-8859-1"), 0, key, 0, 16);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        return skeySpec;
    }
