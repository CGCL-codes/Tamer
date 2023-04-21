    protected static String getSignatureDigest(String signature, MessageDigest messageDigest) {
        byte[] digest = messageDigest.digest(signature.toString().getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }
