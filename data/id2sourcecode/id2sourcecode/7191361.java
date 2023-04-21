    private void getDoesNotBlockPutTest(String configName) throws Exception {
        Configuration cfg = createConfiguration(configName);
        JBossCacheRegionFactory regionFactory = CacheTestUtil.startRegionFactory(cfg, getCacheTestSupport());
        avoidConcurrentFlush();
        final QueryResultsRegion region = regionFactory.buildQueryResultsRegion(getStandardRegionName(REGION_PREFIX), cfg.getProperties());
        region.put(KEY, VALUE1);
        assertEquals(VALUE1, region.get(KEY));
        final Fqn rootFqn = getRegionFqn(getStandardRegionName(REGION_PREFIX), REGION_PREFIX);
        final Cache jbc = getJBossCache(regionFactory);
        final CountDownLatch blockerLatch = new CountDownLatch(1);
        final CountDownLatch writerLatch = new CountDownLatch(1);
        final CountDownLatch completionLatch = new CountDownLatch(1);
        final ExceptionHolder holder = new ExceptionHolder();
        Thread blocker = new Thread() {

            public void run() {
                Fqn toBlock = new Fqn(rootFqn, KEY);
                GetBlocker blocker = new GetBlocker(blockerLatch, toBlock);
                try {
                    jbc.addCacheListener(blocker);
                    BatchModeTransactionManager.getInstance().begin();
                    region.get(KEY);
                    BatchModeTransactionManager.getInstance().commit();
                } catch (Exception e) {
                    holder.e1 = e;
                    rollback();
                } finally {
                    jbc.removeCacheListener(blocker);
                }
            }
        };
        Thread writer = new Thread() {

            public void run() {
                try {
                    writerLatch.await();
                    BatchModeTransactionManager.getInstance().begin();
                    region.put(KEY, VALUE2);
                    BatchModeTransactionManager.getInstance().commit();
                } catch (Exception e) {
                    holder.e2 = e;
                    rollback();
                } finally {
                    completionLatch.countDown();
                }
            }
        };
        blocker.setDaemon(true);
        writer.setDaemon(true);
        boolean unblocked = false;
        try {
            blocker.start();
            writer.start();
            assertFalse("Blocker is blocking", completionLatch.await(100, TimeUnit.MILLISECONDS));
            writerLatch.countDown();
            assertTrue("Writer finished promptly", completionLatch.await(100, TimeUnit.MILLISECONDS));
            blockerLatch.countDown();
            unblocked = true;
            if ("PESSIMISTIC".equals(jbc.getConfiguration().getNodeLockingSchemeString()) && "REPEATABLE_READ".equals(jbc.getConfiguration().getIsolationLevelString())) {
                assertEquals(VALUE1, region.get(KEY));
            } else {
                assertEquals(VALUE2, region.get(KEY));
            }
            if (holder.a1 != null) throw holder.a1; else if (holder.a2 != null) throw holder.a2;
            assertEquals("blocker saw no exceptions", null, holder.e1);
            assertEquals("writer saw no exceptions", null, holder.e2);
        } finally {
            if (!unblocked) blockerLatch.countDown();
        }
    }
