    public static String digest(String str, String digestType) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(digestType);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new IllegalArgumentException(StringUtil.replaceVariables("Could not get digest with algorithm: %1", digestType), nsae);
        }
        final byte[] digest = md.digest(str.getBytes());
        final StringBuffer hexString = new StringBuffer();
        for (final byte element : digest) {
            String plainText = Integer.toHexString(0xFF & element);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }
