    private byte[] makeRFC2865ResponseAuthenticator(byte code, byte identifier, short length, byte[] requestAuthenticator, byte[] responseAttributeBytes) {
        this.md5MessageDigest.reset();
        this.md5MessageDigest.update((byte) code);
        this.md5MessageDigest.update((byte) identifier);
        this.md5MessageDigest.update((byte) (length >> 8));
        this.md5MessageDigest.update((byte) (length & 0xff));
        this.md5MessageDigest.update(requestAuthenticator, 0, requestAuthenticator.length);
        this.md5MessageDigest.update(responseAttributeBytes, 0, responseAttributeBytes.length);
        this.md5MessageDigest.update(this.sharedSecret.getBytes());
        return this.md5MessageDigest.digest();
    }
