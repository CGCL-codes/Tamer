    public static String generateMD5(byte[] bytes) throws NoSuchAlgorithmException, IOException {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(bytes);
            byte[] md5Digest = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (byte b : md5Digest) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(hex);
            }
            md5 = buffer.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5 Algorithm Not found");
            throw ex;
        }
        return md5;
    }
