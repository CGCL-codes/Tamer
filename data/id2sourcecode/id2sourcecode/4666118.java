    private static String digest(long code, int part) {
        byte[] buffer = Long.toHexString(code).getBytes();
        byte[] digest = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(buffer);
            digest = sha.digest();
        } catch (NoSuchAlgorithmException e) {
            digest = buffer;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = digest.length - 1; i >= 0; i--) {
            int b = Math.abs(digest[i]);
            String h = Long.toHexString(b);
            buf.append(h);
        }
        return buf.toString().substring(0, part);
    }
