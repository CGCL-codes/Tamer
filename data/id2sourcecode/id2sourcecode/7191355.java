    private void putDoesNotBlockGetTest(String configName) throws Exception {
        Configuration cfg = createConfiguration(configName);
        JBossCacheRegionFactory regionFactory = CacheTestUtil.startRegionFactory(cfg, getCacheTestSupport());
        avoidConcurrentFlush();
        final QueryResultsRegion region = regionFactory.buildQueryResultsRegion(getStandardRegionName(REGION_PREFIX), cfg.getProperties());
        region.put(KEY, VALUE1);
        assertEquals(VALUE1, region.get(KEY));
        final CountDownLatch readerLatch = new CountDownLatch(1);
        final CountDownLatch writerLatch = new CountDownLatch(1);
        final CountDownLatch completionLatch = new CountDownLatch(1);
        final ExceptionHolder holder = new ExceptionHolder();
        Thread reader = new Thread() {

            public void run() {
                try {
                    BatchModeTransactionManager.getInstance().begin();
                    assertTrue(VALUE2.equals(region.get(KEY)) == false);
                    BatchModeTransactionManager.getInstance().commit();
                } catch (AssertionFailedError e) {
                    holder.a1 = e;
                    rollback();
                } catch (Exception e) {
                    holder.e1 = e;
                    rollback();
                } finally {
                    readerLatch.countDown();
                }
            }
        };
        Thread writer = new Thread() {

            public void run() {
                try {
                    BatchModeTransactionManager.getInstance().begin();
                    region.put(KEY, VALUE2);
                    writerLatch.await();
                    BatchModeTransactionManager.getInstance().commit();
                } catch (Exception e) {
                    holder.e2 = e;
                    rollback();
                } finally {
                    completionLatch.countDown();
                }
            }
        };
        reader.setDaemon(true);
        writer.setDaemon(true);
        writer.start();
        assertFalse("Writer is blocking", completionLatch.await(100, TimeUnit.MILLISECONDS));
        reader.start();
        assertTrue("Reader finished promptly", readerLatch.await(100, TimeUnit.MILLISECONDS));
        writerLatch.countDown();
        assertTrue("Reader finished promptly", completionLatch.await(100, TimeUnit.MILLISECONDS));
        assertEquals(VALUE2, region.get(KEY));
        if (holder.a1 != null) throw holder.a1; else if (holder.a2 != null) throw holder.a2;
        assertEquals("writer saw no exceptions", null, holder.e1);
        assertEquals("reader saw no exceptions", null, holder.e2);
    }
