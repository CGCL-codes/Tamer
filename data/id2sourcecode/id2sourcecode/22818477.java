    public static byte[] makeRFC2866RequestAuthenticator(final MessageDigest md5, final String sharedSecret, byte code, byte identifier, int length, byte[] requestAttributes) {
        byte[] requestAuthenticator = new byte[16];
        for (int i = 0; i < 16; i++) {
            requestAuthenticator[i] = 0;
        }
        md5.reset();
        md5.update((byte) code);
        md5.update((byte) identifier);
        md5.update((byte) (length >> 8));
        md5.update((byte) (length & 0xff));
        md5.update(requestAuthenticator, 0, requestAuthenticator.length);
        md5.update(requestAttributes, 0, requestAttributes.length);
        md5.update(sharedSecret.getBytes());
        return md5.digest();
    }
