        public void run() {
            boolean interrupted = false;
            while (!interrupted) {
                try {
                    long start = threadFixture.buffer.take();
                    threadFixture.readerThrottle.throttle();
                    long now = System.nanoTime();
                    threadFixture.timingController.completeTest(true, 1, now - start);
                } catch (InterruptedException e) {
                    e = null;
                    interrupted = true;
                }
            }
            threadFixture.shouldStop = true;
            writerThread.interrupt();
        }
