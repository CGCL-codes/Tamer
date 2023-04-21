    private static String getSignature(String url) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not available", e);
        }
        StringBuilder msg = new StringBuilder(400);
        msg.append(SHARED_SECRET).append(url.replace("&", "").replace("=", ""));
        BigInteger hash = new BigInteger(1, md.digest(msg.toString().getBytes()));
        String s = hash.toString(16);
        return (s.length() % 2 != 0) ? "0" + s : s;
    }
