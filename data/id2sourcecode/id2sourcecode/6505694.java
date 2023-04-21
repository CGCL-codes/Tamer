    private void slow_pool_reseed() {
        byte[] slow_hash = slow_pool.digest();
        fast_pool.update(slow_hash, 0, slow_hash.length);
        fast_pool_reseed();
        slow_entropy = 0;
        Integer ZERO = new Integer(0);
        for (Enumeration enumeration = entropySeen.keys(); enumeration.hasMoreElements(); ) entropySeen.put(enumeration.nextElement(), ZERO);
    }
