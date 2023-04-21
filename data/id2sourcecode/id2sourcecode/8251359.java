    private void doConcurrencyTest(final boolean doAdd) throws InterruptedException, Exception {
        final CountDownLatch readerLatch = new CountDownLatch(1);
        final CountDownLatch writerLatch = new CountDownLatch(1);
        final Value<Exception> failed = new Value<Exception>();
        if (!doAdd) {
            String identity = "foo";
            IField field = new BooleanField(identity);
            candidate.add(field);
        }
        Thread reader = new Thread(new Runnable() {

            public void run() {
                try {
                    final Iterator<IField> iter = candidate.getFieldsToWrite(true).iterator();
                    writerLatch.countDown();
                    while (iter.hasNext()) {
                        readerLatch.await();
                        candidate.getComponentIdentities();
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
                String identity = "foo";
                IField field = new BooleanField(identity);
                if (doAdd) {
                    candidate.add(field);
                } else {
                    candidate.remove(field);
                }
                readerLatch.countDown();
            }
        });
        reader.start();
        writer.start();
        reader.join();
        writer.join();
        if (failed.get() != null) {
            throw failed.get();
        }
    }
