    @Override
    protected void setUp() {
        (new Random()).nextBytes(data);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            b = md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            assertTrue(false);
        }
        d = new Dealer(KEYSIZE);
        final long start = System.currentTimeMillis();
        long elapsed;
        d.generateKeys(K, L);
        elapsed = System.currentTimeMillis() - start;
        System.out.println("\tKey Gen total (ms): " + elapsed);
        gk = d.getGroupKey();
        keys = d.getShares();
    }
