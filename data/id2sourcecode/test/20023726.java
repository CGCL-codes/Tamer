    public static String md5Encode(String params) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer sig = new StringBuffer();
            for (byte b : md.digest(params.getBytes())) {
                sig.append(Integer.toHexString((b & 0xf0) >>> 4));
                sig.append(Integer.toHexString(b & 0x0f));
            }
            return sig.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("JDK installation problem; MD5 not found.");
        }
    }
