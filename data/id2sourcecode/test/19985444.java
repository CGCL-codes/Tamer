    public static String getMessageDigest(byte[] bMessage, DigestType digestType) throws CryptoException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(digestType.name());
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(MessageFormat.format(FPortecle.RB.getString("NoCreateDigest.exception.message"), digestType), ex);
        }
        byte[] bFingerPrint = messageDigest.digest(bMessage);
        StringBuilder sb = StringUtil.toHex(bFingerPrint, 2, ":");
        return sb.toString();
    }
