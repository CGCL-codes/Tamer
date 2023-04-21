    public synchronized long setData(String name, byte[] data) {
        long l = _hashcode(digestGen.digest(data));
        this.setDigest(name, l);
        return l;
    }
