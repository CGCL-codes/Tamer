    public void testMultiThreadedInterval() throws Exception {
        AtomicIntervalGuard<String> ati = new AtomicIntervalGuard<String>(15);
        AtomicCounter counter = new AtomicCounter(0);
        SampleThreadIntervalWriter[] writer = new SampleThreadIntervalWriter[15];
        for (int i = 0; i < writer.length; i++) {
            writer[i] = new SampleThreadIntervalWriter(i, counter, ati, 500000);
            ati.report(i, 0, 0);
            writer[i].start();
        }
        long startMillis = System.currentTimeMillis();
        for (; ; ) {
            long seqno = counter.incrAndGetSeqno();
            if (seqno >= 500000) break;
            ati.waitMinTime(Math.max(seqno - 5000, 0));
            if (seqno % 50000 == 0) {
                double elapsed = (System.currentTimeMillis() - startMillis) / 1000.0;
                logger.info("Processed seqno=" + seqno + " elapsed=" + elapsed);
            }
        }
        double elapsed = (System.currentTimeMillis() - startMillis) / 1000.0;
        logger.info("Processed seqno=" + counter.getSeqno() + " elapsed=" + elapsed);
        for (int i = 0; i < writer.length; i++) {
            writer[i].join(60000);
            if (writer[i].throwable != null) {
                throw new Exception("Writer terminated abnormally: writer=" + i + " seqno=" + writer[i].seqno, writer[i].throwable);
            }
            if (!writer[i].done) {
                throw new Exception("Writer did not terminate: writer=" + i + " seqno=" + writer[i].seqno);
            }
            assertEquals("Checking writer[" + i + "] seqno", 500000, writer[i].seqno);
        }
    }
