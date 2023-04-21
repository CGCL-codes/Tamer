    public static byte[] makeRFC2865RequestAuthenticator(final MessageDigest md5, final String sharedSecret) {
        byte[] requestAuthenticator = new byte[16];
        Random r = new Random();
        for (int i = 0; i < 16; i++) {
            requestAuthenticator[i] = (byte) r.nextInt();
        }
        md5.reset();
        md5.update(sharedSecret.getBytes());
        md5.update(requestAuthenticator);
        return md5.digest();
    }
