    @Test
    public void testAddListener_concurrency() throws Exception {
        final IEventListener listener1 = mocks.mock(IEventListener.class);
        final IEventListener listener2 = mocks.mock(IEventListener.class);
        mocks.checking(new Expectations() {

            {
                one(listener1).addedAsListenerFor(candidate);
                one(listener2).addedAsListenerFor(candidate);
            }
        });
        candidate.addListener(listener1);
        final CountDownLatch iteratorLatch = new CountDownLatch(1);
        final CountDownLatch writerLatch = new CountDownLatch(1);
        final Value<Exception> failed = new Value<Exception>();
        Thread iterator = new Thread(new Runnable() {

            public void run() {
                final Iterator<IEventListener> iter = candidate.getListeners().iterator();
                writerLatch.countDown();
                try {
                    while (iter.hasNext()) {
                        iteratorLatch.await();
                        iter.next();
                    }
                } catch (Exception e) {
                    failed.set(e);
                }
            }
        });
        Thread writer = new Thread(new Runnable() {

            public void run() {
                try {
                    writerLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                candidate.addListener(listener2);
                iteratorLatch.countDown();
            }
        });
        iterator.start();
        writer.start();
        iterator.join();
        writer.join();
        if (failed.get() != null) {
            throw failed.get();
        }
    }
