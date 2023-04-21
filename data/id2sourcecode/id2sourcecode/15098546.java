    private void writeRecordInternal(final ByteBuffer aRecordBuffer, final long recordNr, final boolean sync) throws IOException {
        this.myRecordBufferCache.put(recordNr, aRecordBuffer);
        try {
            synchronized (this.myFileChannel) {
                aRecordBuffer.rewind();
                long newPosition = recordNr * getRecordLength();
                if (this.myFileChannel == null || !this.myFileChannel.isOpen()) {
                    this.myFileChannel = new RandomAccessFile(getFileName(), "rw").getChannel();
                }
                if (this.myFileChannel.position() != newPosition) {
                    this.myFileChannel.position(newPosition);
                }
                int written = this.myFileChannel.write(aRecordBuffer);
                while (written != getRecordLength()) {
                    LOG.log(Level.INFO, "incomplete write! only " + written + " bytes of " + getRecordLength() + " bytes. writing remaining bytes...");
                    written += this.myFileChannel.write(aRecordBuffer);
                }
            }
            releaseRecord(aRecordBuffer);
        } catch (ClosedChannelException e) {
            LOG.log(Level.WARNING, "Channel closed while writing", e);
            this.myFileChannel = null;
            this.myFileChannel = new RandomAccessFile(getFileName(), "rw").getChannel();
            writeRecordInternal(aRecordBuffer, recordNr, sync);
        }
    }
