    public static String getChecksum(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(bytes);
        byte digest[] = messageDigest.digest();
        StringBuilder hexView = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String intAsHex = Integer.toHexString(0xFF & digest[i]);
            if (intAsHex.length() == 1) {
                hexView.append('0');
            }
            hexView.append(intAsHex);
        }
        return hexView.toString();
    }
