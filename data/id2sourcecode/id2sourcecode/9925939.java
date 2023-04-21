    private byte[] makeRFC2866RequestAuthenticator(byte code, byte identifier, short length, byte[] requestAttributes) {
        byte[] requestAuthenticator = new byte[16];
        for (int i = 0; i < 16; i++) {
            requestAuthenticator[i] = 0;
        }
        this.md5MessageDigest.reset();
        this.md5MessageDigest.update((byte) code);
        this.md5MessageDigest.update((byte) identifier);
        this.md5MessageDigest.update((byte) (length >> 8));
        this.md5MessageDigest.update((byte) (length & 0xff));
        this.md5MessageDigest.update(requestAuthenticator, 0, requestAuthenticator.length);
        this.md5MessageDigest.update(requestAttributes, 0, requestAttributes.length);
        this.md5MessageDigest.update(this.sharedSecret.getBytes());
        return this.md5MessageDigest.digest();
    }
