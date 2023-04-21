    public byte[] encode(String str) {
        byte[] result;
        synchronized (messageDigest) {
            result = messageDigest.digest(str.getBytes());
            messageDigest.notifyAll();
        }
        return result;
    }
