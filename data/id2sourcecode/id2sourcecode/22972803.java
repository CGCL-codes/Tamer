    public static byte[] digest(byte[] digestBytes) {
        byte[] returnValue;
        returnValue = new byte[messageDigest.getDigestLength()];
        messageDigest.update(digestBytes, 0, digestBytes.length);
        try {
            messageDigest.digest(returnValue, 0, returnValue.length);
        } catch (DigestException de) {
        }
        return returnValue;
    }
