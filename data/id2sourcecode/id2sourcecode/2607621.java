    private void writeField(RandomAccessFile file, ByteBuffer bb, field_t field) throws java.io.IOException {
        FileChannel fc = file.getChannel();
        fh.rows = field.rows;
        fh.cols = field.cols;
        fh.elementType = field.data.getElementDescriptor().getCode();
        fh.elementSize = field.data.getElementDescriptor().getSize();
        fh.compression = field.data.getElementDescriptor().getCode() == ElementDescriptor.CHAR_DESCRIPTOR.getCode() ? 0x4 : 0x9;
        long currp = fc.position();
        bb.clear();
        bb.putInt(0xe);
        bb.flip();
        fc.write(bb);
        long headerp = fc.position();
        bb.clear();
        bb.putInt(0);
        fh.write(bb);
        bb.flip();
        fc.write(bb);
        long headerSize = fc.position() - 4 - headerp;
        currp = fc.position();
        if (fh.cols == -1) throw new UnsupportedOperationException("Unknown number of columns not supported.");
        fh.blockBytes = fh.rows * fh.cols * fh.elementSize;
        ByteBuffer fieldBuffer = ByteBuffer.allocateDirect(fh.blockBytes);
        fieldBuffer.order(ByteOrder.LITTLE_ENDIAN);
        field.data.writeBinary(fieldBuffer);
        if (fieldBuffer.position() != fieldBuffer.capacity()) throw new IllegalStateException("Buffer not filled.  " + fieldBuffer.position() + " != " + fieldBuffer.capacity());
        fieldBuffer.flip();
        fc.write(fieldBuffer);
        int bytesWritten = fh.blockBytes;
        while (bytesWritten % 8 != 0) {
            file.write(0);
            ++bytesWritten;
        }
        currp = fc.position();
        fc.position(headerp);
        bb.clear();
        bb.putInt((int) (currp - headerp - 4));
        fh.write(bb);
        bb.flip();
        fc.write(bb);
        fc.position(currp);
    }
