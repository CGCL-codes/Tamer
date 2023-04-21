    private String computeHash(String str) {
        String algorithm = "MD5";
        try {
            MessageDigest md5 = MessageDigest.getInstance(algorithm);
            byte[] end = md5.digest(str.getBytes());
            StringBuffer endString = new StringBuffer();
            for (int i = 0; i < end.length; i++) {
                int tmp = end[i] & 0xFF;
                endString.append(tmp < 16 ? "0" : "").append(Integer.toHexString(tmp));
            }
            return endString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("FATAL: Your system does not support '" + algorithm + "' hashing");
        }
    }
