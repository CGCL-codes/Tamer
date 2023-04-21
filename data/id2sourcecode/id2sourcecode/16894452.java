    private byte[] cipherDigest() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String s = new StringBuffer(name).append('&').append(pwds).append('!').toString();
        return md.digest((s + getSalt(s)).getBytes());
    }
