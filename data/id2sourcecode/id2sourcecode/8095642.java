    public void fillBlock() throws LimitReachedException {
        if (pool0Count >= MIN_POOL_SIZE && System.currentTimeMillis() - lastReseed > 100) {
            reseedCount++;
            byte[] seed = new byte[0];
            for (int i = 0; i < NUM_POOLS; i++) if (reseedCount % (1 << i) == 0) generator.addRandomBytes(pools[i].digest());
            lastReseed = System.currentTimeMillis();
            pool0Count = 0;
        }
        generator.nextBytes(buffer);
    }
