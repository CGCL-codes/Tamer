    public byte[] getSeed() {
        byte[] seed = md.digest(pool);
        add(seed);
        newbytes = 0;
        index = index % POOL_SIZE;
        return seed;
    }
