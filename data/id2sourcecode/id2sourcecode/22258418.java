    private String encode(MessageDigest md, String str) {
        md.reset();
        byte[] digest = md.digest(str.getBytes());
        StringBuffer sbuf = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            sbuf.append(hexChars[(digest[i] >>> 4) & 0xF]);
            sbuf.append(hexChars[digest[i] & 0xF]);
        }
        return sbuf.toString();
    }
