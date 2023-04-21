    private ArrayList<ChannelBuffer> getBufferList(int index, int length) {
        int localReaderIndex = index;
        int localWriterIndex = this.writerIndex();
        int sliceId = sliceId(localReaderIndex);
        int maxlength = localWriterIndex - localReaderIndex;
        if (maxlength < length) {
            maxlength = capacity() - localReaderIndex;
            if (maxlength < length) {
                throw new IllegalArgumentException("Length is bigger than available.");
            }
        }
        ArrayList<ChannelBuffer> bufferList = new ArrayList<ChannelBuffer>(slices.length);
        ChannelBuffer buf = slices[sliceId].duplicate();
        buf.readerIndex(localReaderIndex - indices[sliceId]);
        buf.writerIndex(slices[sliceId].writerIndex());
        while (length > 0) {
            int leftInBuffer = buf.capacity() - buf.readerIndex();
            if (length <= leftInBuffer) {
                buf.writerIndex(buf.readerIndex() + length);
                bufferList.add(buf);
                length = 0;
                break;
            } else {
                bufferList.add(buf);
                length -= leftInBuffer;
                sliceId++;
                buf = slices[sliceId].duplicate();
                buf.readerIndex(0);
                buf.writerIndex(slices[sliceId].writerIndex());
            }
        }
        return bufferList;
    }
