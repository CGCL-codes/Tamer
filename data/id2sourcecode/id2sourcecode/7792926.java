        SinkChannel(final RandomAccessFile file, final SelectorProvider provider, final long size) throws IOException {
            super(provider);
            assert this.blocking;
            this.file = file;
            this.fileChannel = file.getChannel();
            this.lock = new ReentrantLock();
            this.notEmpty = this.lock.newCondition();
            this.writePos = size;
        }
