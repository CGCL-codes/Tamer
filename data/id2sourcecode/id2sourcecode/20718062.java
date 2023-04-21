        PipedFileChannelOutputStreamWorker(CountDownLatch doneSignal, File file, int out_capacity) {
            super(doneSignal, out_capacity);
            try {
                this.fc = new FileOutputStream(file).getChannel();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
