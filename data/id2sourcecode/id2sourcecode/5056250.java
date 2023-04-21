    public static int write(DataOutput out, ByteBuffer data) throws IOException {
        WritableByteChannel ch;
        if (out instanceof WritableByteChannel) ch = (WritableByteChannel) out; else if (out instanceof RandomAccessFile) ch = ((RandomAccessFile) out).getChannel(); else ch = null;
        if (ch != null) {
            int count = data.remaining();
            while (data.hasRemaining()) ch.write(data);
            return count;
        } else return DataIOImpl.write(out, data);
    }
