    protected String generateMd5(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = toBytes(string);
            StringBuilder result = new StringBuilder();
            for (byte b : messageDigest.digest(bytes)) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 isn't available on this JVM", e);
        }
    }
