    private static String md2Hash(String str) {
        MessageDigest md2 = null;
        try {
            md2 = MessageDigest.getInstance("MD2");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] digest = md2.digest(str.getBytes());
        if (digest == null || (digest.length == 0)) return "";
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            res.append(Integer.toHexString(digest[i] & 0xFF));
        }
        return res.toString();
    }
