    protected String createDigest(String message) {
        byte[] messageBytes = message.getBytes();
        String digest;
        synchronized (this.messageDigest) {
            byte[] digestBytes = this.messageDigest.digest(messageBytes);
            digest = this.encode(digestBytes);
        }
        return digest;
    }
