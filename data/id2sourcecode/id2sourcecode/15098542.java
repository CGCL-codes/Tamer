    public ByteBuffer getRecordForReading(final long aRecordNumber) throws IOException {
        ByteBuffer cached = this.myRecordBufferCache.get(aRecordNumber);
        if (cached != null) {
            cached = cached.duplicate();
            cached.rewind();
            return cached;
        }
        ByteBuffer record = getRecordForWriting(aRecordNumber);
        if (record instanceof MappedByteBuffer) {
            return record;
        }
        record.rewind();
        if (this.myFileChannel == null || !this.myFileChannel.isOpen()) {
            this.myFileChannel = new RandomAccessFile(this.myFileName, "rw").getChannel();
        }
        try {
            synchronized (this.myFileChannel) {
                long newPosition = aRecordNumber * getRecordLength();
                if (this.myFileChannel.position() != newPosition) {
                    this.myFileChannel.position(newPosition);
                }
                int reat = 0;
                while (reat < getRecordLength()) {
                    int readNow = this.myFileChannel.read(record);
                    if (readNow < 0) {
                        this.myRecordCount = aRecordNumber;
                        throw new EOFException("record " + record + " seems to be beyong the end of the file");
                    }
                    reat += readNow;
                }
            }
            this.myRecordBufferCache.put(aRecordNumber, record);
        } catch (ClosedChannelException e) {
            LOG.log(Level.SEVERE, "Cannot read record " + aRecordNumber + " at " + (aRecordNumber * getRecordLength()), e);
            this.myFileChannel = null;
            return getRecordForReading(aRecordNumber);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read record " + aRecordNumber + " at " + (aRecordNumber * getRecordLength()), e);
        }
        record.rewind();
        return record;
    }
