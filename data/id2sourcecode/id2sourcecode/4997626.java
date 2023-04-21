    public DedupFileChannel getChannel() throws IOException {
        if (!Main.safeClose) {
            if (this.staticChannel == null) {
                this.staticChannel = new DedupFileChannel(mf);
            }
            return this.staticChannel;
        } else {
            channelLock.lock();
            try {
                if (this.isClosed() || this.buffers.size() == 0) this.initDB();
                DedupFileChannel channel = new DedupFileChannel(mf);
                this.buffers.add(channel);
                return channel;
            } catch (IOException e) {
                throw e;
            } finally {
                channelLock.unlock();
            }
        }
    }
