    public static String sha1(String s) {
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sha1.reset();
        byte[] b = sha1.digest(s.getBytes());
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int j = ((int) b[i]) & 0xFF;
            buf.append(hex.charAt(j / 16));
            buf.append(hex.charAt(j % 16));
        }
        return buf.toString();
    }
