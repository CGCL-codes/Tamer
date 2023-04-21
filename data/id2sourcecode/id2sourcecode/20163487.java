    public static void writeData(DataOutput out, ByteBuffer data) throws IOException {
        if ((out instanceof WritableByteChannel) && (data.isReadOnly() || (data.remaining() >= WRAP_LIMIT))) {
            ByteBuffer ro = (data.isReadOnly() || trustClass(out.getClass())) ? data : data.asReadOnlyBuffer();
            WritableByteChannel ch = (WritableByteChannel) out;
            try {
                while (data.hasRemaining()) ch.write(data);
            } finally {
                data.position(ro.position());
            }
        } else if (out instanceof RandomAccessFile) {
            WritableByteChannel ch = ((RandomAccessFile) out).getChannel();
            while (data.hasRemaining()) ch.write(data);
        } else {
            for (int remaining = data.remaining(); --remaining > 0; ) out.writeByte(data.get());
        }
    }
