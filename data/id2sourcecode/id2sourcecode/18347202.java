    public long getDigest(byte[] bytes) {
        md.reset();
        md.update(bytes);
        byte[] toDigest = md.digest();
        long sum = unsignedIntToLong(toDigest);
        return sum;
    }
