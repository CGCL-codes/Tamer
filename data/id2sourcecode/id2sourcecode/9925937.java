    private byte[] makeRFC2865RequestAuthenticator() {
        byte[] requestAuthenticator = new byte[16];
        Random r = new Random();
        for (int i = 0; i < 16; i++) {
            requestAuthenticator[i] = (byte) r.nextInt();
        }
        this.md5MessageDigest.reset();
        this.md5MessageDigest.update(this.sharedSecret.getBytes());
        this.md5MessageDigest.update(requestAuthenticator);
        return this.md5MessageDigest.digest();
    }
