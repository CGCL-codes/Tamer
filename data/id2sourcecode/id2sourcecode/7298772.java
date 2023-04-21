    private void runProducerAndConsumer(int numBlocks, long writerInitialSleepDuration, long writerIntervalDuration, long readerInitialSleepDuration, long readerIntervalDuration) throws Exception {
        Pipe pipe = new GrowableInMemoryPipe("myPipe");
        Object[] blocks = new Object[numBlocks];
        for (int i = 0; i < numBlocks; ++i) {
            blocks[i] = "MyString" + i;
        }
        BlockWriterCallable blockWriterCallable = new BlockWriterCallable(writerInitialSleepDuration, writerIntervalDuration, blocks, pipe);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(blockWriterCallable);
        Thread.sleep(readerInitialSleepDuration);
        for (int i = 0; i < numBlocks; ++i) {
            Thread.sleep(readerIntervalDuration);
            Object block = pipe.read();
            assertTrue("Read block " + (i + 1) + " must be instance of String", block instanceof String);
            String blockAsString = (String) block;
            assertEquals("Block " + (i + 1), blocks[i], blockAsString);
        }
        assertEquals("Final block must be NO_MORE_DATA block", ControlBlock.NO_MORE_DATA, pipe.read());
    }
