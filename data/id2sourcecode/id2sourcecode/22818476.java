    public static byte[] makeRFC2865ResponseAuthenticator(final MessageDigest md5, final String sharedSecret, byte code, byte identifier, short length, byte[] requestAuthenticator, byte[] responseAttributeBytes) {
        md5.reset();
        md5.update((byte) code);
        md5.update((byte) identifier);
        md5.update((byte) (length >> 8));
        md5.update((byte) (length & 0xff));
        md5.update(requestAuthenticator, 0, requestAuthenticator.length);
        md5.update(responseAttributeBytes, 0, responseAttributeBytes.length);
        md5.update(sharedSecret.getBytes());
        return md5.digest();
    }
