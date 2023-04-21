    public synchronized void generateIdentity() {
        BigInteger seed = new BigInteger(SEED_BITS_COUNT, rndGen);
        byte checkSum[] = null;
        digestAlgorithm.reset();
        digestAlgorithm.update(seed.toByteArray());
        checkSum = digestAlgorithm.digest();
        digestAlgorithm.reset();
        this.checkSum = checkSum;
    }
