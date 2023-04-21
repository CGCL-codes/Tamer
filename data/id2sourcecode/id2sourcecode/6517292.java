    private void controlRunner() throws InterruptedException {
        try {
            connector = factory.connect(spec, listener);
        } catch (LoginFailureException ex) {
            error.compareAndSet(null, ex.error);
            throw new InterruptedException();
        } catch (Throwable ex) {
            error.compareAndSet(null, newError("connection failed: " + ex.getMessage()));
            throw new InterruptedException();
        }
        checkControlInterrupted();
        sessionMarshaler = new SessionMarshaler(false, connector.getSessionId());
        final CountDownLatch latch = new CountDownLatch(2);
        isWriteRunning.set(true);
        executor.execute(new Runnable() {

            public void run() {
                try {
                    setThreadName("writer-thread");
                    writeThread();
                } finally {
                    latch.countDown();
                }
            }
        });
        isReadRunning.set(true);
        executor.execute(new Runnable() {

            public void run() {
                try {
                    setThreadName("reader-thread");
                    readThread();
                } finally {
                    latch.countDown();
                }
            }
        });
        try {
            while (true) {
                checkControlInterrupted();
                while (taskQueue.size() == 0 && readQueue.size() == 0) {
                    synchronized (isControlRunning) {
                        isControlRunning.wait();
                    }
                    checkControlInterrupted();
                }
                Runnable task;
                while ((task = taskQueue.poll()) != null) {
                    task.run();
                    checkControlInterrupted();
                }
                ByteBuffer buffer;
                while ((buffer = readQueue.poll()) != null) {
                    handleMessage(buffer);
                    checkControlInterrupted();
                    while ((task = taskQueue.poll()) != null) {
                        task.run();
                        checkControlInterrupted();
                    }
                }
            }
        } finally {
            interruptWriteThread();
            interruptReadThread();
            connector.close();
            latch.await();
        }
    }
