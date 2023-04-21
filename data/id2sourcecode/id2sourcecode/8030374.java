    public String calculateHash(String key) {
        synchronized (digest) {
            digest.reset();
            digest.update(key.getBytes());
            BigInteger number = new BigInteger(digest.digest());
            return number.toString(16);
        }
    }
