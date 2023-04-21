    public static void write(OutputStream out, byte[] data, int off, int len) throws IOException {
        CheckBounds.offset(data, off, len);
        if (out instanceof XOutputStream) {
            ((XOutputStream) out).writeSecurely(data, off, len);
        } else if (len <= WRAP_LIMIT) {
            for (int hi = off + len; off < hi; off++) out.write(data[off]);
        } else if (trustClass(out.getClass())) {
            out.write(data, off, len);
        } else if (out instanceof FileOutputStream) {
            ByteBuffer buf = ByteBuffer.wrap(data, off, len).asReadOnlyBuffer();
            WritableByteChannel ch = ((FileOutputStream) out).getChannel();
            while (buf.hasRemaining()) ch.write(buf);
        } else if (out instanceof WritableByteChannel) {
            ByteBuffer buf = ByteBuffer.wrap(data, off, len).asReadOnlyBuffer();
            WritableByteChannel ch = (WritableByteChannel) out;
            while (buf.hasRemaining()) ch.write(buf);
        } else {
            int capacity = Math.min(2048, len);
            byte[] a = new byte[capacity];
            while (len > 0) {
                int step = Math.min(capacity, len);
                System.arraycopy(data, off, a, 0, step);
                out.write(a, 0, step);
                len -= step;
                off += step;
            }
        }
    }
