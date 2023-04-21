        PipedFileChannelInputStreamWorker(CountDownLatch doneSignal, File file, int in_capacity) {
            super(doneSignal, in_capacity);
            try {
                this.fc = new FileInputStream(file).getChannel();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
