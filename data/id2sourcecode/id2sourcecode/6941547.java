    public static byte[] hash(byte data[]) {
        synchronized (DIGEST) {
            DIGEST.reset();
            return DIGEST.digest(data);
        }
    }
