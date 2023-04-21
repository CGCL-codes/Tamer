    @Override
    public void discardReadBytes() {
        int localReaderIndex = this.readerIndex();
        if (localReaderIndex == 0) {
            return;
        }
        int localWriterIndex = this.writerIndex();
        int localLength = capacity() - localReaderIndex;
        ArrayList<ChannelBuffer> list = getBufferList(localReaderIndex, localLength);
        ChannelBuffer first = list.get(0);
        int firstCapacity = first.writerIndex() - first.readerIndex();
        ChannelBuffer firstbuf = ChannelBuffers.buffer(order, firstCapacity);
        firstbuf.writeBytes(first);
        list.set(0, firstbuf);
        ChannelBuffer buffer = ChannelBuffers.buffer(localReaderIndex);
        list.add(buffer);
        int localMarkedReaderIndex = localReaderIndex;
        try {
            resetReaderIndex();
            localMarkedReaderIndex = this.readerIndex();
        } catch (IndexOutOfBoundsException e) {
        }
        int localMarkedWriterIndex = localWriterIndex;
        try {
            resetWriterIndex();
            localMarkedWriterIndex = this.writerIndex();
        } catch (IndexOutOfBoundsException e) {
        }
        setFromList(list, order);
        localMarkedReaderIndex = Math.max(localMarkedReaderIndex - localReaderIndex, 0);
        localMarkedWriterIndex = Math.max(localMarkedWriterIndex - localReaderIndex, 0);
        this.readerIndex(localMarkedReaderIndex);
        this.writerIndex(localMarkedWriterIndex);
        markReaderIndex();
        markWriterIndex();
        localWriterIndex = Math.max(localWriterIndex - localReaderIndex, 0);
        localReaderIndex = 0;
        this.readerIndex(localReaderIndex);
        this.writerIndex(localWriterIndex);
    }
