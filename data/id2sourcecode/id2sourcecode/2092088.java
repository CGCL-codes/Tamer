    public void decrypt(String sharedKey) throws Exception {
        byte[] key = sharedKey != null ? sharedKey.getBytes("UTF-8") : new byte[] {};
        byte[] decryptedMessage = Security.decrypt(key, this.data);
        ByteArrayInputStream stream = new ByteArrayInputStream(decryptedMessage);
        byte[] digest = new byte[Security.getDigestSize()];
        byte[] message = null;
        stream.read(digest, 0, digest.length);
        message = new byte[stream.available()];
        stream.read(message);
        message = new String(message).trim().getBytes();
        byte[] digest2 = Security.digest(message);
        if (digest2.length != digest.length) {
            throw new DecodingException();
        }
        for (int i = 0; i < digest.length; i++) {
            if (digest[i] != digest2[i]) {
                throw new DecodingException();
            }
        }
        this.data = message;
        this.encrypted = false;
    }
