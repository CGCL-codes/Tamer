    private boolean verifyCanvasSignature(String str, String expectedSig) {
        byte[] hash;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(StringUtils.toBytes(str));
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("The platform does nto support MD5", e);
        }
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(Integer.toHexString((b & 0xf0) >>> 4));
            result.append(Integer.toHexString(b & 0x0f));
        }
        return result.toString().equals(expectedSig);
    }
