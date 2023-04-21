    public String countHashCode(Throwable t) {
        String hash = null;
        try {
            MessageDigest hashCreator = MessageDigest.getInstance("MD5");
            countHashCode(t, hashCreator);
            hash = new BigInteger(1, hashCreator.digest()).toString(Character.MAX_RADIX);
            hashCreator.reset();
        } catch (Throwable th) {
            JBrt.commitInternal(th);
        }
        return hash;
    }
