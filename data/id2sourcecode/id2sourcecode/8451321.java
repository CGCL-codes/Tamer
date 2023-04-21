    private String getMd5Digest(byte[] bytes) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 cryptographic algorithm is not available.", e);
        }
        byte[] messageDigest = md.digest(bytes);
        BigInteger number = new BigInteger(1, messageDigest);
        StringBuffer sb = new StringBuffer('0');
        sb.append(number.toString(16));
        return sb.toString();
    }
