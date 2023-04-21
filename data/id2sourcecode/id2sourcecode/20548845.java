    public static String generateHash(MessageDigest pMessageDigest, byte[] pInputByteBuffer) {
        pMessageDigest.reset();
        pMessageDigest.update(pInputByteBuffer);
        byte[] digest = pMessageDigest.digest();
        StringBuilder hexStringBuilder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hexString = Integer.toHexString(0xFF & digest[i]);
            hexStringBuilder.append(hexString);
        }
        return hexStringBuilder.toString();
    }
