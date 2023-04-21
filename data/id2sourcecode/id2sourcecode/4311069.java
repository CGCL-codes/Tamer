    public static String generateSignature(List<String> params, String secret) {
        StringBuffer buffer = new StringBuffer();
        Collections.sort(params);
        for (String param : params) {
            buffer.append(param);
        }
        buffer.append(secret);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md.digest(buffer.toString().getBytes())) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.err.println("MD5 does not appear to be supported" + ex);
            return "";
        }
    }
